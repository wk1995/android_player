package com.wk.player.api

/**
 * 页面的控制
 * */
interface IPageController {

    /**
     * 返回 不关闭直播间
     * */
    fun back()

    /**
     * 关闭直播间
     * */
    fun closeLive()

    /**
     * 分享直播间
     * */
    fun shareLive()

    /**
     * 显示速度页面
     * */
    fun showSpeechChoices()

    /**
     * 切换全屏状态
     * */
    fun toggleFullScreenState()


    /**
     * 预约
     * */
    fun reservation()

    fun showJoinGroupFail()
}