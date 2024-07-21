package com.wk.player

import android.text.TextUtils
import com.wk.player.api.BasePlayComponentDataBean

/**
 * 播放器组件所需要的数据
 * @param liveTitle 直播标题字符串
 * @param imgDetail 图片详情地址
 * @param supportPlayback 是否支持回放 true 支持回放
 * @param startTimeCountdown 开始时间倒数值 单位s。支持可变，由于[PlayState]不断的在变，每次都会拿一次值，但原值只能从接口获取
 * @param hasReservation 是否预约了 true 预约了
 * @param disableScreenshot 禁止录屏 true 禁止录屏
 * @param userId 当前用户id
 * @param playbackLink 回放地址
 * @param rtmp 直播地址
 * @param isInEffectiveTime 是否在有效期内 true在有效期
 * */
data class PlayComponentDataBean(
    val liveId:String="",
    val liveTitle: String = "",
    val imgDetail: String = "",
    val supportPlayback: Boolean = false,
    var startTimeCountdown: Int = 0,
    var hasReservation: Boolean = false,
    val disableScreenshot: Boolean = false,
    val userId: String = "",
    val playbackLink: String = "",
    var rtmp: String = "",
    val isInEffectiveTime: Boolean = false,
    val chatRoomId:String=""
) : BasePlayComponentDataBean(){

    /**
     * 是否无效
     * */
    fun isInValid() = TextUtils.isEmpty(liveId)
}