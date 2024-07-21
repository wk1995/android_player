package com.wk.player

import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.wk.player.api.BasePlayController
import com.wk.player.api.IMediaPlayer
import com.wk.player.constants.PlayEventConstants
import com.wk.player.tencent.TencentPlayerFactory
import rx.subscriptions.CompositeSubscription

class GestureVideoController : BasePlayController<PlayComponentDataBean>() {

    companion object {
        private const val TAG = "GestureVideoController"

        /**
         * 默认的自动隐藏时间
         * */
        private const val DEFAULT_HIDE_TIME = 3000L

        /**
         * 避免多次创建对象
         * */
        private var mGestureVideoController: GestureVideoController? = null

        fun obtain(): GestureVideoController {
            if (mGestureVideoController == null) {
                mGestureVideoController = GestureVideoController()
            }
            return mGestureVideoController!!
        }
    }


    /**
     * 全局播放状态
     * */
    private var mCurrentGlobalPlayState: GlobalPlayState = GlobalPlayState.GLOBAL_PLAY_STATE_DISMISS

    /**
     * 是否需要显示控件
     * */
    private var needShowChildComponent = false


    /**
     * 隐藏组件逻辑
     * */
    private val hideComponentRunnable: Runnable by lazy {
        val runnable = Runnable { hide() }
        runnable
    }

    private val mShowAnim: Animation by lazy {
        val mShowAnim = AlphaAnimation(0f, 1f)
        mShowAnim.duration = 300
        mShowAnim
    }
    private val mHideAnim: Animation by lazy {
        val mHideAnim = AlphaAnimation(1f, 0f)
        mHideAnim.duration = 300
        mHideAnim
    }

    override fun initData(){
        super.initData()
        joinCount = 0
        changePlayType(PlayType.UN_KNOW, true)
        changePlayState(PlayState.PLAY_STATE_INIT, true)
    }

    fun initGlobalPlay() {
        changeGlobalPlayState(GlobalPlayState.GLOBAL_PLAY_STATE_DISMISS, true)
    }

    /**
     * 处理逻辑 应该写在tencent package中
     * */
    override fun onPlayEvent(player: IMediaPlayer?, event: Int, param: Bundle?) {
        when (event) {
            PlayEventConstants.PLAY_ERR_INVALID_LICENSE -> {
                val application = getContext()
//                ToastUtils.toastShort(application ?: return, "licence 失效")
            }
            PlayEventConstants.PLAY_WARNING_RECONNECT -> {
                val application = getContext()
//                ToastUtils.toastShort(application ?: return, "网络链接失败，请稍后重试")
            }
            PlayEventConstants.PLAY_EVT_PLAY_BEGIN -> {
                changePlayState(PlayState.PLAY_STATE_PLAYBACK)
            }
            PlayEventConstants.PLAY_EVT_PLAY_END -> {
                changePlayState(PlayState.PLAY_STATE_END)
            }
            PlayEventConstants.PLAY_EVT_PLAY_PROGRESS -> {
                param?.apply {
                    var rate = 1.0f
                    //总时长ms
                    var playDurationMs = 0
                    var evtMsg = ""
                    //进度ms
                    var evtPlayProgressMs = 0
                    //获取可播放时长ms
                    var evtPlayableDurationMs = 0
                    //进度s
                    var evtPlayProgress = 0
                    //总时长s
                    var evtPlayDuration = 0
                    //获取可播放时长s
                    var evtPlayableDuration = 0
                    keySet().forEach {
                        when (it) {
                            "EVT_PLAY_DURATION_MS" -> playDurationMs = this[it] as? Int ?: 0
                            "EVT_PLAYABLE_RATE" -> rate = this[it] as? Float ?: 1.0f
                            "EVT_MSG" -> evtMsg = this[it] as? String ?: ""
                            "EVT_PLAY_PROGRESS_MS" -> evtPlayProgressMs =
                                this[it] as? Int ?: 0
                            "EVT_PLAYABLE_DURATION_MS" -> evtPlayableDurationMs =
                                this[it] as? Int ?: 0
                            "EVT_PLAY_DURATION" -> evtPlayDuration = this[it] as? Int ?: 0
                            "EVT_PLAY_PROGRESS" -> evtPlayProgress = this[it] as? Int ?: 0
                            "EVT_PLAYABLE_DURATION" -> evtPlayableDuration =
                                this[it] as? Int ?: 0
                        }
                    }
                    mCurrentPlaybackTime = evtPlayProgress
                    mTotalPlayBackTime = evtPlayDuration
                    ergodicChildComponent { it, _ ->
                        it.setProgress(evtPlayDuration, evtPlayProgress)
                        it.rateChange(rate)
                    }
                    mCurrentRate = rate
                }
            }
        }
    }

    override fun onError(player: IMediaPlayer?, code: Int, msg: String?, extraInfo: Bundle?) {
        changePlayState(PlayState.PLAY_STATE_ERROR)
//        request()
    }


    /**
     * 切换组件显示状态
     * */
    override fun toggleShowState() {
        needShowChildComponent = !needShowChildComponent
        if (needShowChildComponent) {
            show()
        } else {
            hide()
        }
    }

    private val mDisposables = CompositeSubscription()



    /**
     * 加群组失败次数
     * */
    private var joinCount = 0






    override fun handlePlayState(
        lastPlayState: PlayState,
        currentPlayState: PlayState,
        playInfo: PlayComponentDataBean
    ) {
        super.handlePlayState(lastPlayState, currentPlayState, playInfo)
        when (currentPlayState) {
            PlayState.PLAY_STATE_PLAYING -> {
                if (getCurrentPlayType() != PlayType.PLAY) {
                    play(mPlayComponentDataBean.rtmp, true)
                } else if (getCurrentPlayType() != PlayType.LIVE) {
                    val playbackLink = mPlayComponentDataBean?.playbackLink ?: ""
                    if (canPlayBack(playbackLink)) {
                        play(playbackLink, false)
                    } else {
                        changePlayState(PlayState.PLAY_STATE_END)
                    }
                }
            }
            PlayState.PLAY_STATE_PLAYING_PAUSED -> {
                pausePlay(true)
            }
            PlayState.PLAY_STATE_PAUSED -> {
                pausePlay(false)
            }
            PlayState.PLAY_STATE_END -> {
                stopPlay()
                if (!mPlayComponentDataBean.playType.isPlay()) {
                    val playbackLink = mPlayComponentDataBean.playbackLink
                    if (canPlayBack(playbackLink)) {
                        changePlayState(PlayState.PLAY_STATE_PLAYBACK)
                    }
                }
            }
            PlayState.PLAY_STATE_INIT -> {
                stopPlay()
            }

            PlayState.PLAY_STATE_PLAYBACK -> {
                val playbackLink = mPlayComponentDataBean?.playbackLink ?: ""
                if (canPlayBack(playbackLink)) {
                    play(playbackLink, false)
                } else {
                    changePlayState(PlayState.PLAY_STATE_END)
                }
            }
            PlayState.PLAY_STATE_PLAYING_RESTORE -> {
                switchStream(mPlayComponentDataBean.rtmp)
                resumePlay(true)
            }
            else -> {}
        }
    }

    /**
     * 回放有效期是否有效：为设置或者 大于当前事件 true 表示有效
     * */
    private fun validTime() =
        mPlayComponentDataBean?.isInEffectiveTime == true

    /**
     * 是否能播放回放
     * @param playbackLink 播放地址
     * */
    private fun canPlayBack(playbackLink: String) =
        mPlayComponentDataBean?.supportPlayback == true && validTime() && !TextUtils.isEmpty(
            playbackLink
        )



    fun show() {
        mainHandler.apply {
            removeCallbacks(hideComponentRunnable)
            ergodicChildComponent { it, _ ->
                it.onVisibilityChanged(true, mShowAnim)
            }
            postDelayed(hideComponentRunnable, DEFAULT_HIDE_TIME)
        }
    }

    fun hide() {
        mainHandler.apply {
            removeCallbacks(hideComponentRunnable)
            ergodicChildComponent { it, _ ->
                it.onVisibilityChanged(false, mHideAnim)
            }
            needShowChildComponent = false
        }
    }

    override fun togglePlayState() {
        show()
        super.togglePlayState()
    }

    override fun getDefaultComponentDataBean() = PlayComponentDataBean()

    /**
     * @param isForce true 强制更新状态
     * */
    private fun changeGlobalPlayState(globalPlayState: GlobalPlayState, isForce: Boolean = false) {
        if (mCurrentGlobalPlayState != globalPlayState || isForce) {
            mCurrentGlobalPlayState = globalPlayState
            ergodicChildComponent { it, _ ->
                it.onGlobalPlayStateChanged(mCurrentGlobalPlayState)
            }
        }
    }


    /**
     * [MyApplication.mContext]可能会因为多进程影响到
     * */
    override fun getContext(): Application? =App.instance

    override fun getPlayerFactory() = TencentPlayerFactory

    override fun destroy(reallyDestroy: Boolean) {
        changeGlobalPlayState(
            if (reallyDestroy || mPlayComponentDataBean.playState == PlayState.PLAY_STATE_INIT) {
                GlobalPlayState.GLOBAL_PLAY_STATE_DISMISS
            } else {
                GlobalPlayState.GLOBAL_PLAY_STATE_SHOW
            }, true
        )
        mControlWrapper?.pageController = null
        super.destroy(reallyDestroy)
        removeControlComponent(!reallyDestroy)
        if (reallyDestroy) {
            removeGroupListener()
            mPlayComponentDataBean = getDefaultComponentDataBean()
            mDisposables.clear()
            mGestureVideoController = null
        }
    }

}