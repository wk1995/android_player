package com.wk.player.constants

/**
 * 播放网络状态通知参数
 * 用于[com.wk.player.api.BasePlayerObserver.onNetStatus] status参数中的key
 * */
object PlayNetStateParamsConstants {
    const val NET_STATUS_CPU_USAGE = "CPU_USAGE"
    const val NET_STATUS_VIDEO_WIDTH = "VIDEO_WIDTH"
    const val NET_STATUS_VIDEO_HEIGHT = "VIDEO_HEIGHT"
    const val NET_STATUS_VIDEO_FPS = "VIDEO_FPS"
    const val NET_STATUS_VIDEO_GOP = "VIDEO_GOP"
    const val NET_STATUS_VIDEO_BITRATE = "VIDEO_BITRATE"
    const val NET_STATUS_AUDIO_BITRATE = "AUDIO_BITRATE"
    const val NET_STATUS_NET_SPEED = "NET_SPEED"
    const val NET_STATUS_AUDIO_CACHE = "AUDIO_CACHE"
    const val NET_STATUS_VIDEO_CACHE = "VIDEO_CACHE"
    const val NET_STATUS_AUDIO_INFO = "AUDIO_PLAY_INFO"
    const val NET_STATUS_NET_JITTER = "NET_JITTER"
    const val NET_STATUS_SERVER_IP = "SERVER_IP"
    const val NET_STATUS_VIDEO_DPS = "VIDEO_DPS"
    const val NET_STATUS_QUALITY_LEVEL = "NET_QUALITY_LEVEL"
}