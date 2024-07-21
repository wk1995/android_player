package com.wk.player.api

import android.view.Surface
import com.wk.player.bean.PlayBitrateItem
import com.wk.player.bean.PlayTrackInfo
import com.wk.player.bean.SubtitleRenderModel

interface IVodPlayer : IMediaPlayer {
    /**
     * 置播放器配置信息，推荐在启动播放前设置配置信息。
     *
     * @param config 播放器配置信息.配置信息请参考 com.tencent.rtmp.TXVodPlayConfig
     */
    fun setConfig()

    /**
     * 设置字幕软解目标对象
     */
    fun setSubtitleView()

    /**
     * @param url      字幕地址
     * @param name     字幕的名字。如果添加多个字幕，字幕名称请设置为不同的名字，用于区分与其他添加的字幕，否则可能会导致字幕选择错误。
     * @param mimeType 字幕类型，仅支持VVT和SRT格式 TXVodConstants#VOD_PLAY_MIMETYPE_TEXT_SRT, TXVodConstants#VOD_PLAY_MIMETYPE_TEXT_VTT 后面可以通过getSubtitleTrackInfo()中的TXTrackInfo#getName()获取对应的名字
     */
    fun addSubtitleSource(url: String, name: String, mimeType: String?)

    /**
     * 选择轨道
     */
    fun selectTrack(trackIndex: Int)

    /**
     * 取消选择轨道
     */
    fun deselectTrack(trackIndex: Int)

    /**
     * 当播放地址为HSL时，返回支持的码率（清晰度）列表。
     * 在收到PLAY_EVT_PLAY_BEGIN事件后才能正确返回结果。
     */
    fun getSubtitleTrackInfo(): List<PlayTrackInfo>

    /**
     * 返回当前播放的码率索引
     */
    fun getAudioTrackInfo(): List<PlayTrackInfo>

    /**
     * 设置字幕软解目标对象
     */
    fun setSubtitleStyle(renderModel: SubtitleRenderModel)

    /**
     * 设置Surface，目前只支持硬解 播放器只能在Surface有效的情况下才能正常工作，使用此接口播放时，需要由用户手动调用 resume() 或 pause()
     */
    fun setSurface(surface: Surface?)

    /**
     * 停止播放。
     *
     * @param isNeedClearLastImg 是否需要清除最后一帧画面。
     * true：清除最后一帧画面，正常停止播放时推荐清除。
     * false：保留最后一帧画面，异常停止播放(如网络异常,导致播放被迫停止)，而SDK使用者希望重连服务器，继续播放时，推荐保留。
     * @return 是否成功启动播放。0：成功 ;  -1：失败
     */
    fun stopPlay(isNeedClearLastImg: Boolean): Int

    override fun stopPlay() {
        stopPlay(true)
    }

    /**
     * 跳转到视频流指定时间点。
     * 可实现视频快进、快退、进度条跳转等功能。
     *
     * @param time 视频流时间点，单位为秒
     */
    fun seek(time: Int)

    /**
     * 跳转到视频流指定时间点。
     * 可实现视频快进、快退、进度条跳转等功能。
     *
     * @param time 视频流时间点,单位秒(s),小数点后精确到3位
     */
    fun seek(time: Float)

    /**
     * 获取当前播放位置
     *
     * @return 单位秒
     */
    fun getCurrentPlaybackTime(): Float

    /**
     * 返回已经缓存的视频时长
     *
     * @return 单位秒
     */
    fun getBufferDuration(): Float

    /**
     * 获取总时长
     *
     * @return 单位秒
     */
    fun getDuration(): Float

    /**
     * 获取可播放时长
     *
     * @return 单位秒
     */
    fun getPlayableDuration(): Float

    /**
     * 获取视频宽度
     *
     * @return 视频宽度，单位：像素
     */
    fun getWidth(): Int

    /**
     * 获取视频高度
     *
     * @return 视频高度，单位：像素
     */
    fun getHeight(): Int


    /**
     * 设置图像平铺模式。
     */
    fun setRenderMode(mode: Int)


    /**
     * 启用或禁用视频硬解码。
     *
     * @param enable 启用或禁用视频硬解码
     * true: 启用视频硬解码
     * false: 禁用视频硬解码，启用默认的视频软解码
     * @return 是否成功启用或禁用视频硬解码。
     * true:启用或禁用视频硬解码成功。
     * false:启用或禁用视频硬解码失败
     */
    fun enableHardwareDecode(enable: Boolean): Boolean

    /**
     * 设置是否静音播放。
     *
     * @param mute 是否静音播放。
     * true：静音播放。
     * false：不静音播放。
     */
    fun setMute(mute: Boolean)

    /**
     * 设置是否自动获取音频焦点，默认自动获取，在开始播放之前设置有效
     *
     * @param requestFocus 是否获取焦点。
     * @return true：设置成功。false：设置失败。
     */
    fun setRequestAudioFocus(requestFocus: Boolean): Boolean

    /**
     * 设置点播是否startPlay后自动开始播放。默认自动播放
     *
     * @param autoPlay 是否自动播放
     */
    fun setAutoPlay(autoPlay: Boolean)

    /**
     * 设置点播的播放速率。默认1.0
     *
     * @param rate 播放速率
     */
    fun setRate(rate: Float)

    /**
     * 返回当前播放的码率索引
     */
    fun getBitrateIndex(): Int

    /**
     * 设置当前正在播放的码率索引，无缝切换清晰度。 清晰度切换可能需要等待一小段时间。腾讯云支持多码率HLS分片对齐，保证最佳体验。
     *
     * @param index 码率索引，index == -1，表示开启HLS码流自适应； index > 0 TXVodPlayer#getSupportedBitrates()，表示手动切换到对应清晰度码率
     */
    fun setBitrateIndex(index: Int)

    /**
     * 当播放地址为HSL时，返回支持的码率（清晰度）列表。
     *
     *
     * 在收到PLAY_EVT_PLAY_BEGIN事件后才能正确返回结果。
     */
    fun getSupportedBitrates(): List<PlayBitrateItem>

    /**
     * 设置镜像
     *
     * @param mirror true：镜像播放;false：非镜像播放
     */
    fun setMirror(mirror: Boolean)

    /**
     * 设置播放开始时间.
     * 在startPlay前设置，修改开始播放的起始位置
     *
     * @param pos 视频流时间点,单位秒(s),小数点后精确到3位
     */
    fun setStartTime(pos: Float)

    /**
     * 加密HLS的token。设置此值后，播放器自动在URL中的文件名之前增加voddrm.token.<Token>
     *
     * @param token 加密HLS的token
    </Token> */
    fun setToken(token: String?)
    /**
     * 是否在循环播放
     */
    /**
     * 设置是否循环播放
     *
     * @param loop true：循环播放 false：非循环播放
     */
    fun setLoop(loop: Boolean)

    /**
     * 是否在循环播放
     */
    fun isLoop(): Boolean

    /**
     * 播放器拓展参数配置
     *
     * @param key   配置参数key
     * @param value 配置参数value
     */
    fun setStringOption(key: String?, value: Any?)
}