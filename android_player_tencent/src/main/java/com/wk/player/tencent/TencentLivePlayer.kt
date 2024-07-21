package com.wk.player.tencent

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tencent.live2.V2TXLiveDef
import com.tencent.live2.V2TXLiveDef.V2TXLiveRotation
import com.tencent.live2.V2TXLivePlayer
import com.tencent.live2.V2TXLivePlayerObserver
import com.tencent.live2.impl.V2TXLivePlayerImpl
import com.tencent.rtmp.ui.TXCloudVideoView
import com.wk.player.api.BasePlayerObserver
import com.wk.player.api.ILivePlayer
import com.wk.player.bean.*

/**
 * 腾讯播放器
 * https://cloud.tencent.com/document/product/454/56045#7cff91848dfdb52dee31980e6c555c6d
 *
 * https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__V2TXLivePlayer__android.html#a3202948a15617c9060336053bfed68d8
 * */
class TencentLivePlayer(context: Application) : ILivePlayer {

    companion object {
        private const val TAG = "TencentPlayer"

        private var mTencentLivePlayer: TencentLivePlayer? = null

        /**
         * 后续可以用享元实现
         * */
        fun obtain(context: Context): TencentLivePlayer {
            if (mTencentLivePlayer == null) {
                mTencentLivePlayer = TencentLivePlayer(context.applicationContext as Application)
            }
            return mTencentLivePlayer!!
        }
    }

    /**
     * 腾讯云直播播放器
     *主要负责从指定的直播流地址拉取音视频数据，并进行解码和本地渲染播放。
     * 播放器包含如下能力：
     * 支持 RTMP, HTTP-FLV, TRTC 以及 WebRTC；
     * 屏幕截图，可以截取当前直播流的视频画面；
     * 延时调节，可以设置播放器缓存自动调整的最小和最大时间；
     * 自定义的视频数据处理，您可以根据项目需要处理直播流中的视频数据后，再进行渲染以及播放。
     * */
    private val mV2TXLivePlayer: V2TXLivePlayer = V2TXLivePlayerImpl(context)

    private var livePlayerObserver: BasePlayerObserver? = null
    private val mV2TXLivePlayerObserver: V2TXLivePlayerObserver by lazy {
        val mV2TXLivePlayerObserver = object : V2TXLivePlayerObserver() {
            override fun onError(
                player: V2TXLivePlayer?,
                code: Int,
                msg: String?,
                extraInfo: Bundle?
            ) {
                super.onError(player, code, msg, extraInfo)
                log("onErrorcode: $code  msg: $msg extraInfo  $extraInfo")
                livePlayerObserver?.onError(this@TencentLivePlayer, code, msg, extraInfo)
            }

            override fun onWarning(
                player: V2TXLivePlayer?,
                code: Int,
                msg: String?,
                extraInfo: Bundle?
            ) {
                super.onWarning(player, code, msg, extraInfo)
                log("onWarning code: $code  msg: $msg extraInfo  $extraInfo")
                livePlayerObserver?.onWarning(this@TencentLivePlayer, code, msg, extraInfo)
            }

            override fun onVideoResolutionChanged(
                player: V2TXLivePlayer?,
                width: Int,
                height: Int
            ) {
                super.onVideoResolutionChanged(player, width, height)
                log("onVideoResolutionChanged width : $width  height: $height ")
                livePlayerObserver?.onVideoResolutionChanged(this@TencentLivePlayer, width, height)
            }

            override fun onConnected(player: V2TXLivePlayer?, extraInfo: Bundle?) {
                super.onConnected(player, extraInfo)
                log("onConnected extraInfo  $extraInfo")
                livePlayerObserver?.onConnected(this@TencentLivePlayer, extraInfo)
            }

            override fun onVideoPlaying(
                player: V2TXLivePlayer?,
                firstPlay: Boolean,
                extraInfo: Bundle?
            ) {
                super.onVideoPlaying(player, firstPlay, extraInfo)
                log("onVideoPlaying firstPlay  $firstPlay extraInfo  $extraInfo")
                livePlayerObserver?.onVideoPlaying(this@TencentLivePlayer, firstPlay, extraInfo)
            }

            override fun onAudioPlaying(
                player: V2TXLivePlayer?,
                firstPlay: Boolean,
                extraInfo: Bundle?
            ) {
                super.onAudioPlaying(player, firstPlay, extraInfo)
                log("onAudioPlaying firstPlay  $firstPlay extraInfo  $extraInfo")
                livePlayerObserver?.onAudioPlaying(this@TencentLivePlayer, firstPlay, extraInfo)
            }

            override fun onVideoLoading(player: V2TXLivePlayer?, extraInfo: Bundle?) {
                super.onVideoLoading(player, extraInfo)
                log("onVideoLoading extraInfo  $extraInfo")
                livePlayerObserver?.onVideoLoading(this@TencentLivePlayer, extraInfo)
            }

            override fun onAudioLoading(player: V2TXLivePlayer?, extraInfo: Bundle?) {
                super.onAudioLoading(player, extraInfo)
                log("onAudioLoading extraInfo  $extraInfo")
                livePlayerObserver?.onAudioLoading(this@TencentLivePlayer, extraInfo)
            }

            override fun onPlayoutVolumeUpdate(player: V2TXLivePlayer?, volume: Int) {
                super.onPlayoutVolumeUpdate(player, volume)
                log("onPlayoutVolumeUpdate volume  $volume")
                livePlayerObserver?.onPlayoutVolumeUpdate(this@TencentLivePlayer, volume)
            }

            /**
             * 直播播放器统计数据回调
             * @param  player    回调该通知的播放器对象
             * @param  statistics    播放器统计数据 [V2TXLiveDef.V2TXLivePlayerStatistics]
             * */
            override fun onStatisticsUpdate(
                player: V2TXLivePlayer?,
                statistics: V2TXLiveDef.V2TXLivePlayerStatistics?
            ) {
                super.onStatisticsUpdate(player, statistics)
                log("onStatisticsUpdate statistics  $statistics")
                livePlayerObserver?.onStatisticsUpdate(this@TencentLivePlayer)
            }

            override fun onSnapshotComplete(player: V2TXLivePlayer?, image: Bitmap?) {
                super.onSnapshotComplete(player, image)
                log(" onSnapshotComplete")
                livePlayerObserver?.onSnapshotComplete(this@TencentLivePlayer, image)
            }

            override fun onRenderVideoFrame(
                player: V2TXLivePlayer?,
                videoFrame: V2TXLiveDef.V2TXLiveVideoFrame?
            ) {
                super.onRenderVideoFrame(player, videoFrame)
                log(" onRenderVideoFrame")
            }

            override fun onPlayoutAudioFrame(
                player: V2TXLivePlayer?,
                audioFrame: V2TXLiveDef.V2TXLiveAudioFrame?
            ) {
                super.onPlayoutAudioFrame(player, audioFrame)
                log(" onPlayoutAudioFrame")
            }

            override fun onReceiveSeiMessage(
                player: V2TXLivePlayer?,
                payloadType: Int,
                data: ByteArray?
            ) {
                super.onReceiveSeiMessage(player, payloadType, data)
                log(" onReceiveSeiMessage payloadType $payloadType")
                livePlayerObserver?.onReceiveSeiMessage(this@TencentLivePlayer, payloadType, data)
            }
        }
        mV2TXLivePlayerObserver
    }

    /**
     * @param playUrl url	音视频流的播放地址，支持 RTMP, HTTP-FLV, TRTC。
     * @return 返回值 V2TXLiveCode
     * */
    override fun startPlay(playUrl: String): Int {
       return mV2TXLivePlayer.startLivePlay(playUrl)
    }

    override fun stopPlay() {
        mV2TXLivePlayer.stopPlay()
    }

    override fun pausePlay(isManagerOperate: Boolean) {
        pauseVideo()
        pauseAudio()
    }

    override fun resumePlay(isManagerOperate: Boolean) {
        resumeAudio()
        resumeVideo()
    }

    override fun pauseAudio() {
        mV2TXLivePlayer.pauseAudio()
    }

    override fun resumeAudio() {
        mV2TXLivePlayer.resumeAudio()
    }

    override fun pauseVideo() {
        mV2TXLivePlayer.pauseVideo()
    }

    override fun resumeVideo() {
        mV2TXLivePlayer.resumeVideo()
    }

    override fun screenshot() {
        mV2TXLivePlayer.snapshot()
    }

    override fun setPlayVolume(volume: Int) {
        mV2TXLivePlayer.setPlayoutVolume(volume)
    }

    /**
     * 设置播放器缓存自动调整的最小和最大时间 ( 单位：秒 )
     * @param maxTime default 5.0
     * @param minTime default 1.0
     * @return 返回值 [com.wk.player.constants.LiveCode]
     * [com.wk.player.constants.LiveCode.LIVE_OK]: 成功
     * [com.wk.player.constants.LiveCode.LIVE_ERROR_INVALID_PARAMETER]: 操作失败，minTime 和 maxTime 需要大于0
     * [com.wk.player.constants.LiveCode.LIVE_ERROR_REFUSED]: 播放器处于播放状态，不支持修改缓存策略
     * */
    override fun setCacheParams(minTime: Float, maxTime: Float): Int {
        return mV2TXLivePlayer.setCacheParams(minTime, maxTime)
    }

    override fun changeDelayMode() {
    }

    override fun setRenderView(playView: View?) {
        if (playView is TXCloudVideoView?) {
            mV2TXLivePlayer.setRenderView(playView)
        } else {
            throw java.lang.Exception(" playView ${playView?.javaClass?.simpleName} not support")
        }
    }

    override fun setRenderRotation(playRotation: PlayRotation) {
        mV2TXLivePlayer.setRenderRotation(
            when (playRotation) {
                PlayRotation.Rotation0 -> V2TXLiveRotation.V2TXLiveRotation0
                PlayRotation.Rotation90 -> V2TXLiveRotation.V2TXLiveRotation90
                PlayRotation.Rotation180 -> V2TXLiveRotation.V2TXLiveRotation180
                PlayRotation.Rotation270 -> V2TXLiveRotation.V2TXLiveRotation270
            }
        )

    }

    override fun setRenderFillMode(mode: LiveFillMode) {
        mV2TXLivePlayer.setRenderFillMode(
            when (mode) {
                LiveFillMode.LiveFillModeFill -> V2TXLiveDef.V2TXLiveFillMode.V2TXLiveFillModeFill
                LiveFillMode.LiveFillModeFit -> V2TXLiveDef.V2TXLiveFillMode.V2TXLiveFillModeFit
                LiveFillMode.LiveFillModeScaleFill -> V2TXLiveDef.V2TXLiveFillMode.V2TXLiveFillModeScaleFill
            }
        )
    }

    override fun isPlaying() = mV2TXLivePlayer.isPlaying == 1

    /**
     * 直播流无缝切换，支持 FLV 和 LEB
     * @param newUrl 新的拉流地址
     * */
    override fun switchStream(newUrl: String) {
        mV2TXLivePlayer.switchStream(newUrl)
    }

    override fun getStreamList(): List<LiveStreamInfo> {
        return mV2TXLivePlayer.streamList.map {
            LiveStreamInfo(it.width, it.height, it.url)
        }
    }

    /**
     *
     * 启用播放音量大小提示
     * 开启后可以在 [com.wk.player.api.BasePlayerObserver.onPlayoutVolumeUpdate] 回调中获取到 SDK 对音量大小值的评估。
     *
     * @param intervalMs 决定了 [com.wk.player.api.BasePlayerObserver.onPlayoutVolumeUpdate] 回调的触发间隔，
     * 单位为ms，最小间隔为100ms，如果小于等于0则会关闭回调，建议设置为300ms；【默认值】：0，不开启。
     * @return 返回值 [com.wk.player.constants.LiveCode] [com.wk.player.constants.LiveCode.LIVE_OK] 成功
     *
     * */
    override fun enableVolumeEvaluation(intervalMs: Int): Int {
        return mV2TXLivePlayer.enableVolumeEvaluation(intervalMs)
    }

    /**
     * 开启/关闭对视频帧的监听回调。
     * SDK 在您开启次此开关后将不再渲染视频画面，您可以通过 [com.wk.player.api.BasePlayerObserver] 获得视频帧，并执行自定义的渲染逻辑。
     * @param enable    是否开启自定义渲染。【默认值】：false
     * @param pixelFormat    自定义渲染回调的视频像素格式 V2TXLivePixelFormat。
     * @param bufferType    自定义渲染回调的视频数据格式 V2TXLiveBufferType。
     * @return  返回值 [com.wk.player.constants.LiveCode] [com.wk.player.constants.LiveCode.LIVE_OK] 成功
     *  */
    override fun enableObserveVideoFrame(
        enable: Boolean,
        pixelFormat: LivePixelFormat,
        bufferType: LiveBufferType
    ): Int {
        return mV2TXLivePlayer.enableObserveVideoFrame(
            enable, when (pixelFormat) {
                LivePixelFormat.LivePixelFormatUnknown -> V2TXLiveDef.V2TXLivePixelFormat.V2TXLivePixelFormatUnknown
                LivePixelFormat.LivePixelFormatI420 -> V2TXLiveDef.V2TXLivePixelFormat.V2TXLivePixelFormatI420
                LivePixelFormat.LivePixelFormatTexture2D -> V2TXLiveDef.V2TXLivePixelFormat.V2TXLivePixelFormatTexture2D
            }, when (bufferType) {
                LiveBufferType.LiveBufferTypeUnknown -> V2TXLiveDef.V2TXLiveBufferType.V2TXLiveBufferTypeUnknown
                LiveBufferType.LiveBufferTypeByteBuffer -> V2TXLiveDef.V2TXLiveBufferType.V2TXLiveBufferTypeByteBuffer
                LiveBufferType.LiveBufferTypeTexture -> V2TXLiveDef.V2TXLiveBufferType.V2TXLiveBufferTypeTexture
                LiveBufferType.LiveBufferTypeByteArray -> V2TXLiveDef.V2TXLiveBufferType.V2TXLiveBufferTypeByteArray
            }
        )
    }

    override fun enableObserveAudioFrame(enable: Boolean) {
        mV2TXLivePlayer.enableObserveAudioFrame(enable)
    }

    override fun enableReceiveSeiMessage(enable: Boolean, payloadType: Int): Int {
        return mV2TXLivePlayer.enableReceiveSeiMessage(enable, payloadType)
    }

    override fun showDebugView(isShow: Boolean) {
        mV2TXLivePlayer.showDebugView(isShow)
    }

    override fun setProperty(key: String, value: Any): Int {
        return mV2TXLivePlayer.setProperty(key, value)
    }

    override fun setObserver(livePlayerObserver: BasePlayerObserver?) {
        if (this.livePlayerObserver != livePlayerObserver) {
            this.livePlayerObserver = livePlayerObserver
            mV2TXLivePlayer.setObserver(mV2TXLivePlayerObserver)
        }
    }

    override fun removeObserver() {
        livePlayerObserver = null
    }

    override fun destroy(reallyDestroy: Boolean) {
        setRenderView(null)
        if (reallyDestroy) {
            stopPlay()
            removeObserver()
            mTencentLivePlayer = null
        }
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }
}