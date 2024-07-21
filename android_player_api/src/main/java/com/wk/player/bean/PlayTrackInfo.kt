package com.wk.player.bean

/**
 * 用来对应腾讯的 com.tencent.rtmp.TXTrackInfo
 * https://liteav.sdk.qcloud.com/doc/api/zh-cn/group__TXTrackInfo__android.html#af4c0336629c7a8a3d911c76b93938c58
 *
 * @param trackIndex 获取轨道index
 * @param name 获取轨道名称
 * @param trackType 获取轨道轨获取轨道类型:2->音频轨 3->字幕轨,0->未知,1->视频轨
 * */
data class PlayTrackInfo(val trackIndex:Int, val name:String,val trackType:Int)
