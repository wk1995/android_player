package com.wk.player.constants

/**
 * 腾讯云直播服务(LVB)错误码和警告码的定义。
 * */
object LiveCode {

    /**
     * 没有错误
     * */
    const val LIVE_OK = 0

    /**
     * 调用 API 时，传入的参数不合法
     * */
    const val LIVE_ERROR_INVALID_PARAMETER = -2

    /**
     * 播放器处于播放状态，不支持修改缓存策略
     * */
    const val LIVE_ERROR_REFUSED = -3

    /**
     * 像素格式或者数据格式不支持
     * */
    const val LIVE_ERROR_NOT_SUPPORTED = -4

    const val LIVE_ERROR_NULL = -10086
}