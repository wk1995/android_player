package com.wk.player.bean

/**
 * 映射 com.tencent.rtmp.TXBitrateItem
 * https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TXBitrateItem__android.html#classcom_1_1tencent_1_1rtmp_1_1TXBitrateItem
 *
 * @param bitrate 此流的视频码率
 * @param height 此流的视频高度
 * @param index m3u8 文件中的序号
 * @param width 此流的视频宽度
 * */
data class PlayBitrateItem(
    val index: Int, val width: Int, val height: Int, val bitrate: Int
)
