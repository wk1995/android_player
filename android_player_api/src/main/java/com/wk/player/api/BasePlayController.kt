package com.wk.player.api

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import android.view.View
import com.wk.player.bean.*
import com.wk.player.constants.LiveCode
import java.lang.Float
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.getValue
import kotlin.lazy


/**
 * 控制全局播放逻辑，保存当前播放器各个状态
 * 由于类似全局播放器这类的游离的组件，需要BasePlayController来控制，
 * */
abstract class BasePlayController<R : BasePlayComponentDataBean> : IPlayController, IVodPlayer,
    ILivePlayer {

    companion object {
        private const val TAG = "BasePlayController"
    }

    /**
     * 组件缓存
     * */
    protected val componentMap by lazy {
        HashMap<AbsControlComponent<in View, R>, Boolean>()
    }

    var mPlayComponentDataBean: R = getDefaultComponentDataBean()

    protected val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    /**
     * 直播播放器
     * */
    private var livePlayer: ILivePlayer? = null

    /**
     * 点播播放器
     * */
    private var vodPlayer: IVodPlayer? = null

    var mControlWrapper: com.wk.player.ControlWrapper? = null

    /**
     * 当前回放的时间
     * 播放器获取当前播放位置真正获取的还是从播放器View获取的，see[IVodPlayer.getCurrentPlaybackTime]源码实现
     * 但当全局播放器显示的时候 播放器的View必定时摧毁了的。这时候拿不到播放器播放时间。所以需要一个变量来保存该时间
     * */
    protected var mCurrentPlaybackTime: Int = 0

    protected var mTotalPlayBackTime: Int = 0

    /**
     * 当前播放速率
     * */
    protected var mCurrentRate: kotlin.Float = 1.0f
    fun getRate(): kotlin.Float = mCurrentRate

    /**
     * 直播间id
     * */
    var liveId: String = ""

    private val mBasePlayerObserver by lazy {
        object : BasePlayerObserver() {
            override fun onPlayEvent(player: IMediaPlayer?, event: Int, param: Bundle?) {
                super.onPlayEvent(player, event, param)
                this@BasePlayController.onPlayEvent(player, event, param)
            }


            override fun onError(
                player: IMediaPlayer?, code: Int, msg: String?, extraInfo: Bundle?
            ) {
                super.onError(player, code, msg, extraInfo)
                this@BasePlayController.onError(player, code, msg, extraInfo)
            }

            override fun onVideoPlaying(
                player: IMediaPlayer?, firstPlay: Boolean, extraInfo: Bundle?
            ) {
                super.onVideoPlaying(player, firstPlay, extraInfo)
                if (isPlaying()) {
                    changePlayState(com.wk.player.PlayState.PLAY_STATE_PLAYING)
                } else {
                    changePlayState(com.wk.player.PlayState.PLAY_STATE_PAUSED)
                }
            }
        }
    }

    private fun isEmpty() = componentMap.isEmpty()

    /**
     * 添加聊天室的监听
     * */
    open fun addGroupListener() {

    }

    open fun removeGroupListener(){

    }


    open fun initData(){
        mCurrentRate=1.0f
        mCurrentPlaybackTime = 0
        mTotalPlayBackTime = 0
        mPlayComponentDataBean=getDefaultComponentDataBean()
    }

    abstract fun toggleShowState()

    fun play(url: String, isLive: Boolean = true):Int {
        changePlayType(
            if (isLive) {
                com.wk.player.PlayType.LIVE
            } else {
                com.wk.player.PlayType.PLAY
            }
        )
        initPlayer()
        return startPlay(url)
    }

    var mRenderView: View? = null

    /**
     * 顺序比较重要，有些组件的显示逻辑依赖多种状态
     * */
    open fun useLastPlayState() {
        log("useLastPlayState")
        changePlayType(getCurrentPlayType(), true)
        changePlayState(getCurrentPlayState(), isForce = true)
    }

    fun resetPlayState() {
        mPlayComponentDataBean.playType = com.wk.player.PlayType.UN_KNOW
        mPlayComponentDataBean.playState = com.wk.player.PlayState.PLAY_STATE_INIT
    }

    /**
     * 初始化播放器
     *
     * 需要知道当前播放类型
     * */
    private fun initPlayer() {
        if (isInValidPlayType()) {
            return
        }
        val context = getContext()
        if (context == null) {
            log("initPlayer context is null")
            return
        }
        if (isLive()) {
            vodPlayer?.destroy(true)
            mCurrentPlaybackTime = 0
            mTotalPlayBackTime = 0
            vodPlayer = null
            livePlayer = getPlayerFactory().createLivePlayer(context)
            livePlayer?.setRenderView(mRenderView)
            livePlayer?.setObserver(mBasePlayerObserver)
        } else {
            livePlayer?.destroy(true)
            livePlayer = null
            vodPlayer = getPlayerFactory().createVodPlayer(context)
            vodPlayer?.setRenderView(mRenderView)
            vodPlayer?.setObserver(mBasePlayerObserver)
        }
    }

    abstract fun getContext(): Application?

    /**
     * 添加组件缓存
     * */
    fun addComponentCache(
        controlComponent: AbsControlComponent<in View, R>, isDissociate: Boolean = false
    ) {
        componentMap[controlComponent] = isDissociate
        controlComponent.setControlWrapper(mControlWrapper)
    }

    abstract fun getPlayerFactory(): AbstractPlayerFactory

    override fun rewindPlay() {
        changePlayProgress(-15)
    }

    override fun forwardPlay() {
        changePlayProgress(15)
    }

    override fun pausePlay() {
        changePlayState(com.wk.player.PlayState.PLAY_STATE_PAUSED)
    }

    override fun resumePlay() {
        resumePlay(false)
    }

    override fun togglePlayState() {
        if (isPlaying()) {
            pausePlay()
        } else {
            resumePlay(false)
        }
    }

    private fun changePlayProgress(changeValue: Int = 0) {
        val nextTime = getCurrentPlaybackTime() + changeValue
        val time = if (changeValue > 0) {
            Float.min(nextTime, getDuration())
        } else {
            Float.max(nextTime, 0f)
        }
        seek(time)
    }

    private fun isLive() = getCurrentPlayType() == com.wk.player.PlayType.LIVE

    private fun isInValidPlayType() = getCurrentPlayType() == com.wk.player.PlayType.UN_KNOW

    /**
     * 改变播放类型，直播还是点播
     * */
    fun changePlayType(playType: com.wk.player.PlayType, isForce: Boolean = false) {
        if (getCurrentPlayType() != playType || isForce) {
            mPlayComponentDataBean?.playType = playType
            ergodicChildComponent { it, _ ->
                it.onPlayTypeChanged(playType)
            }
        }
    }

    protected fun getCurrentPlayType() = mPlayComponentDataBean.playType

    protected fun getCurrentPlayState() =
        mPlayComponentDataBean.playState

    private fun isManagePause(): Boolean {
        //如果主讲暂停了直播，用户不能恢复
        return mPlayComponentDataBean?.playState == com.wk.player.PlayState.PLAY_STATE_PLAYING_PAUSED
    }

    fun changeProcess() {
        ergodicChildComponent { it, _ ->
            it.setProgress(mTotalPlayBackTime, mCurrentPlaybackTime)
        }
    }

    /**
     * 改变播放状态，所有的改变都从这里开始
     * */
    fun changePlayState(
        playState: com.wk.player.PlayState = mPlayComponentDataBean.playState, isForce: Boolean = false
    ) {
        if (isManagePause() && playState == com.wk.player.PlayState.PLAY_STATE_PAUSED && !isForce) {
            log("changePlayState: isManagePause $playState isForce: $isForce")
            return
        }
        val lastPlayState = getCurrentPlayState()
        if (lastPlayState != playState || isForce) {
            mPlayComponentDataBean.playState = playState
            ergodicChildComponent { it, _ ->
                it.onPlayStateChanged(
                    playState,
                    mPlayComponentDataBean
                )
            }
            handlePlayState(lastPlayState, playState,mPlayComponentDataBean)
        }
    }

    abstract fun getDefaultComponentDataBean(): R

    /**
     * 控制播放逻辑
     * @param lastPlayState 上一个播放状态
     * @param  currentPlayState 当前播放状态
     * */
    open fun handlePlayState(lastPlayState: com.wk.player.PlayState, currentPlayState: com.wk.player.PlayState, playInfo: R) {
        Log.d(
            TAG,
            "handlePlayState:   lastPlayState  $lastPlayState  currentPlayState $currentPlayState"
        )
    }


    /**
     * 移除所有的游离控制组件
     * 关于游离控制组件的定义请看 [.addControlComponent] 关于 isDissociate 的解释
     */
    fun removeAllDissociateComponents() {
        val it: MutableIterator<Map.Entry<AbsControlComponent<*, *>, Boolean>> =
            componentMap.entries.iterator()
        while (it.hasNext()) {
            val (key, value) = it.next()
            if (value) {
                key.onDestroy()
                it.remove()
            }

        }
    }

    fun removeControlComponent(needSaveDissociate: Boolean) {
        val it: MutableIterator<Map.Entry<AbsControlComponent<*, *>, Boolean>> =
            componentMap.entries.iterator()
        while (it.hasNext()) {
            val (key, value) = it.next()
            if (needSaveDissociate) {
                if (!value) {
                    removeControlComponent(key)
                    it.remove()
                }
            } else {
                removeControlComponent(key)
                it.remove()
            }

        }
    }

    private fun removeControlComponent(key: AbsControlComponent<*, *>) {
        key.onDestroy()
    }

    /**
     * 移除所有控制组件
     */
    fun removeAllControlComponent() {
        for (it in componentMap.entries) {
            removeControlComponent(it.key)
        }
        componentMap.clear()
    }

    /**
     * 遍历组件操作
     * */
    private inline fun ergodicChildComponentByIterator(deal: (MutableIterator<Map.Entry<AbsControlComponent<*, *>, Boolean>>) -> Any?) {
        val it: MutableIterator<Map.Entry<AbsControlComponent<*, *>, Boolean>> =
            componentMap.entries.iterator()
        while (it.hasNext()) {
            deal(it)
        }
    }


    abstract fun onPlayEvent(player: IMediaPlayer?, event: Int, param: Bundle?)

    abstract fun onError(
        player: IMediaPlayer?, code: Int, msg: String?, extraInfo: Bundle?
    )

    /**
     * 遍历组件操作
     * */
    protected inline fun ergodicChildComponent(process: (AbsControlComponent<in View, R>, Boolean) -> Any?) {
        componentMap.forEach {
            process(it.key, it.value)
        }
    }

    fun ergodicComponent(process: (AbsControlComponent<in View, *>, Boolean) -> Any?) {
        componentMap.forEach {
            process(it.key, it.value)
        }
    }


    override fun startPlay(playUrl: String): Int {
        Log.d(TAG, "playUrl: $playUrl")
        livePlayer?.apply {
            return startPlay(playUrl)
        }
        var currentTime = vodPlayer?.getCurrentPlaybackTime() ?: 0f
        if (currentTime <= 0f) {
            currentTime = mCurrentPlaybackTime.toFloat()
        }
        Log.d(TAG, "currentTime: $currentTime")
        vodPlayer?.setStartTime(currentTime)
        return vodPlayer?.startPlay(playUrl) ?: -1
    }

    override fun stopPlay() {
        livePlayer?.stopPlay()
        vodPlayer?.stopPlay()
    }

    override fun pausePlay(isManagerOperate: Boolean) {
        livePlayer?.pausePlay(isManagerOperate)
        vodPlayer?.pausePlay(isManagerOperate)
    }

    override fun resumePlay(isManagerOperate: Boolean) {
        if (isManagePause() && !isManagerOperate) {
            return
        }
        livePlayer?.resumePlay(false)
        vodPlayer?.resumePlay(false)
    }

    override fun pauseAudio() {
        livePlayer?.pauseAudio()
    }

    override fun resumeAudio() {
        livePlayer?.resumeAudio()
    }

    override fun pauseVideo() {
        livePlayer?.pauseVideo()
    }

    override fun resumeVideo() {
        livePlayer?.resumeVideo()
    }

    override fun screenshot() {
        livePlayer?.screenshot()
        vodPlayer?.screenshot()
    }

    override fun setPlayVolume(volume: Int) {
        livePlayer?.setPlayVolume(volume)
        vodPlayer?.setPlayVolume(volume)
    }

    override fun setCacheParams(minTime: kotlin.Float, maxTime: kotlin.Float): Int {
        return livePlayer?.setCacheParams(minTime, maxTime) ?: LiveCode.LIVE_ERROR_NULL
    }

    override fun changeDelayMode() {
        livePlayer?.changeDelayMode()
    }

    override fun setRenderView(playView: View?) {
        livePlayer?.setRenderView(playView)
        vodPlayer?.setRenderView(playView)
    }

    override fun setRenderRotation(playRotation: PlayRotation) {
        livePlayer?.setRenderRotation(playRotation)
        vodPlayer?.setRenderRotation(playRotation)
    }

    override fun setRenderFillMode(mode: LiveFillMode) {
        livePlayer?.setRenderFillMode(mode)
    }

    override fun isPlaying() = livePlayer?.isPlaying() == true || vodPlayer?.isPlaying() == true

    override fun switchStream(newUrl: String) {
        livePlayer?.switchStream(newUrl)
    }

    override fun getStreamList(): List<LiveStreamInfo>? {
        return livePlayer?.getStreamList()
    }

    override fun enableVolumeEvaluation(intervalMs: Int): Int {
        return livePlayer?.enableVolumeEvaluation(intervalMs) ?: LiveCode.LIVE_ERROR_NULL
    }

    override fun enableObserveVideoFrame(
        enable: Boolean, pixelFormat: LivePixelFormat, bufferType: LiveBufferType
    ): Int {
        return livePlayer?.enableObserveVideoFrame(enable, pixelFormat, bufferType)
            ?: LiveCode.LIVE_ERROR_NULL
    }

    override fun enableObserveAudioFrame(enable: Boolean) {
        livePlayer?.enableObserveAudioFrame(enable)
    }

    override fun enableReceiveSeiMessage(enable: Boolean, payloadType: Int): Int {
        return livePlayer?.enableReceiveSeiMessage(enable, payloadType)
            ?: LiveCode.LIVE_ERROR_NULL
    }

    override fun showDebugView(isShow: Boolean) {
        livePlayer?.showDebugView(isShow)
    }

    override fun setProperty(key: String, value: Any): Int {
        return livePlayer?.setProperty(key, value) ?: LiveCode.LIVE_ERROR_NULL
    }

    override fun setObserver(livePlayerObserver: BasePlayerObserver?) {
        livePlayer?.setObserver(livePlayerObserver)
        vodPlayer?.setObserver(livePlayerObserver)
    }

    override fun destroy(reallyDestroy: Boolean) {
        if (reallyDestroy) {
            mPlayComponentDataBean?.playState = com.wk.player.PlayState.PLAY_STATE_INIT
            liveId = ""
            mCurrentPlaybackTime = 0
            mTotalPlayBackTime = 0
            removeAllControlComponent()
        }
        mRenderView = null
        livePlayer?.destroy(reallyDestroy)
        vodPlayer?.destroy(reallyDestroy)
    }

    override fun setConfig() {
        vodPlayer?.setConfig()
    }

    override fun setSubtitleView() {
        vodPlayer?.setSubtitleView()
    }

    override fun addSubtitleSource(url: String, name: String, mimeType: String?) {
        vodPlayer?.addSubtitleSource(url, name, mimeType)
    }

    override fun selectTrack(trackIndex: Int) {
        vodPlayer?.selectTrack(trackIndex)
    }

    override fun deselectTrack(trackIndex: Int) {
        vodPlayer?.deselectTrack(trackIndex)
    }

    override fun getSubtitleTrackInfo(): List<PlayTrackInfo> {
        return vodPlayer?.getSubtitleTrackInfo() ?: ArrayList()
    }

    override fun getAudioTrackInfo(): List<PlayTrackInfo> {
        return vodPlayer?.getAudioTrackInfo() ?: ArrayList()
    }

    override fun setSubtitleStyle(renderModel: SubtitleRenderModel) {
        vodPlayer?.setSubtitleStyle(renderModel)
    }

    override fun setSurface(surface: Surface?) {
        vodPlayer?.setSurface(surface)
    }

    override fun stopPlay(isNeedClearLastImg: Boolean): Int {
        return vodPlayer?.stopPlay(isNeedClearLastImg) ?: -1
    }

    override fun seek(time: Int) {
        vodPlayer?.seek(time)
    }

    override fun seek(time: kotlin.Float) {
        vodPlayer?.seek(time)
    }

    override fun getCurrentPlaybackTime(): kotlin.Float {
        return vodPlayer?.getCurrentPlaybackTime() ?: -1f
    }

    override fun getBufferDuration(): kotlin.Float {
        return vodPlayer?.getBufferDuration() ?: -1f
    }

    override fun getDuration(): kotlin.Float {
        return vodPlayer?.getDuration() ?: -1f
    }

    override fun getPlayableDuration(): kotlin.Float {
        return vodPlayer?.getPlayableDuration() ?: -1f
    }

    override fun setRenderMode(mode: Int) {
        vodPlayer?.setRenderMode(mode)
    }

    override fun enableHardwareDecode(enable: Boolean): Boolean {
        return vodPlayer?.enableHardwareDecode(enable) ?: false
    }

    override fun setMute(mute: Boolean) {
        vodPlayer?.setMute(mute)
    }

    override fun setRequestAudioFocus(requestFocus: Boolean): Boolean {
        return vodPlayer?.setRequestAudioFocus(requestFocus) ?: false
    }

    override fun setAutoPlay(autoPlay: Boolean) {
        vodPlayer?.setAutoPlay(autoPlay)
    }

    override fun setRate(rate: kotlin.Float) {
        vodPlayer?.setRate(rate)
    }

    override fun getBitrateIndex(): Int {
        return vodPlayer?.getBitrateIndex() ?: -1
    }

    override fun setBitrateIndex(index: Int) {
        vodPlayer?.setBitrateIndex(index)
    }

    override fun getSupportedBitrates(): List<PlayBitrateItem> {
        return vodPlayer?.getSupportedBitrates() ?: ArrayList()
    }


    override fun setMirror(mirror: Boolean) {
        vodPlayer?.setMirror(mirror)
    }

    override fun setStartTime(pos: kotlin.Float) {
        vodPlayer?.setStartTime(pos)
    }

    override fun setToken(token: String?) {
        vodPlayer?.setToken(token)
    }

    override fun setLoop(loop: Boolean) {
        vodPlayer?.setLoop(loop)
    }

    override fun isLoop(): Boolean {
        return vodPlayer?.isLoop() == true
    }

    override fun setStringOption(key: String?, value: Any?) {
        vodPlayer?.setStringOption(key, value)
    }

    override fun getWidth(): Int {
        return vodPlayer?.getHeight() ?: 0
    }

    override fun getHeight(): Int {
        return vodPlayer?.getHeight() ?: 0
    }

    override fun removeObserver() {
        vodPlayer?.removeObserver()
        livePlayer?.removeObserver()
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }
}