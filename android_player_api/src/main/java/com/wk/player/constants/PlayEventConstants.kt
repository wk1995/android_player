package com.wk.player.constants

/**
 * 播放事件列表
 * https://cloud.tencent.com/document/product/881/20216
 *
 * 用于[com.wk.player.api.BasePlayerObserver.onPlayEvent]event参数
 * */
object PlayEventConstants {

    /**
     * 网络接收到首个可渲染的视频数据包（IDR）。
     * */
    const val PLAY_EVT_RCV_FIRST_I_FRAME = 2003

    /**
     * 视频播放开始
     * */
    const val PLAY_EVT_PLAY_BEGIN = 2004

    /**
     * 视频播放进度，会通知当前播放进度、加载进度 和总体时长。
     * */
    const val PLAY_EVT_PLAY_PROGRESS = 2005

    /**
     * 视频播放结束。
     * */
    const val PLAY_EVT_PLAY_END = 2006

    /**
     * 视频播放 loading，如果能够恢复，之后会有 [PLAY_EVT_VOD_LOADING_END] 事件。
     * */
    const val PLAY_EVT_PLAY_LOADING = 2007
    const val PLAY_EVT_START_VIDEO_DECODER = 2008

    /**
     * 视频分辨率改变。
     * */
    const val PLAY_EVT_CHANGE_RESOLUTION = 2009

    /**
     * 成功获取播放文件信息。
     * */
    const val PLAY_EVT_GET_PLAYINFO_SUCC = 2010

    /**
     * MP4 视频旋转角度。
     * */
    const val PLAY_EVT_CHANGE_ROTATION = 2011

    /**
     * 播放器已准备完成，可以播放。设置了 autoPlay 为 false 之后，需要在收到此事件后，调用 resume 才会开始播放。
     * */
    const val PLAY_EVT_VOD_PLAY_PREPARED = 2013

    /**
     * 视频播放 loading 结束，视频继续播放
     * */
    const val PLAY_EVT_VOD_LOADING_END = 2014
    const val PLAY_EVT_RCV_FIRST_AUDIO_FRAME = 2026
    const val PLAY_WARNING_RECONNECT = 2103

    /**
     * 网络断连,且经多次重连亦不能恢复,更多重试请自行重启播放。
     * */
    const val PLAY_ERR_NET_DISCONNECT = -2301
    const val PLAY_ERR_FILE_NOT_FOUND = -2303
    const val PLAY_ERR_HEVC_DECODE_FAIL = -2304

    /**
     * HLS 解密 key 获取失败。
     * */
    const val PLAY_ERR_HLS_KEY = -2305
    const val PLAY_ERR_GET_PLAYINFO_FAIL = -2306
    const val PLAY_WARNING_HW_ACCELERATION_FAIL = 2106
    const val PLAY_ERR_INVALID_LICENSE = -5
    const val PLAY_EVT_TCP_CONNECT_SUCC = 2016
    const val PLAY_EVT_FIRST_VIDEO_PACKET = 2017
    const val PLAY_EVT_DNS_RESOLVED = 2018

    /**
     * Seek 完成，10.3版本开始支持
     * */
    const val PLAY_EVT_SEEK_COMPLETE = 2019

    /**
     * 循环播放，一轮播放结束（10.8 版本开始支持）。
     * */
    const val PLAY_EVT_LOOP_ONCE_COMPLETE = 6001
}