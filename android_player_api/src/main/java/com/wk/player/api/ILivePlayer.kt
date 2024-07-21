package com.wk.player.api

import com.wk.player.bean.LiveStreamInfo
import com.wk.player.bean.LiveBufferType
import com.wk.player.bean.LiveFillMode
import com.wk.player.bean.LivePixelFormat

/**
 * 直播播放器
 * */
interface ILivePlayer : IMediaPlayer {


    /**
     * 暂停播放器的音频流
     * */
    fun pauseAudio()

    /**
     * 恢复播放器的音频流
     * */
    fun resumeAudio()


    /**
     * 暂停播放器的视频流
     * */
    fun pauseVideo()

    /**
     * 恢复播放器的视频流
     * */
    fun resumeVideo()

    /**
     * 设置播放器缓存自动调整的最小和最大时间 ( 单位：秒 )
     * */
    fun setCacheParams(minTime: Float, maxTime: Float): Int

    /**
     * 切换延时调节
     * */
    fun changeDelayMode()

    /**
     * 设置画面的填充模式
     * @param mode    画面填充模式 [LiveFillMode]。
     * */
    fun setRenderFillMode(mode: LiveFillMode)


    /**
     * 直播流无缝切换
     * @param newUrl 新的拉流地址
     * */
    fun switchStream(newUrl: String)

    /**
     * 获取码流信息
     * */
    fun getStreamList(): List<LiveStreamInfo>?

    /**
     * 启用播放音量大小提示
     * */
    fun enableVolumeEvaluation(intervalMs: Int): Int

    /**
     * 开启/关闭对视频帧的监听回调
     * */
    fun enableObserveVideoFrame(
        enable: Boolean,
        pixelFormat: LivePixelFormat,
        bufferType: LiveBufferType
    ): Int

    /**
     * 开启/关闭对音频数据的监听回调
     * */
    fun enableObserveAudioFrame(enable: Boolean)

    /**
     * 开启接收 SEI 消息
     * @param enable    true: 开启接收 SEI 消息; false: 关闭接收 SEI 消息。【默认值】: false
     * @param payloadType    指定接收 SEI 消息的 payloadType，支持 5、242，请与发送端的 payloadType 保持一致
     * @return [LIVE_OK]成功
     * */
    fun enableReceiveSeiMessage(enable: Boolean, payloadType: Int): Int

    /**
     * 是否显示播放器状态信息的调试浮层
     * */
    fun showDebugView(isShow: Boolean)

    /**
     * 调用 V2TXLivePlayer 的高级 API 接口
     * 该接口用于调用一些高级功能
     * @param key    高级 API 对应的 key。
     * @param value    调用 key 所对应的高级 API 时，需要的参数
     * @param [LIVE_OK]: 成功 LIVE_ERROR_INVALID_PARAMETER: 操作失败，key 不允许为 null
     * */
    fun setProperty(key: String, value: Any): Int


}