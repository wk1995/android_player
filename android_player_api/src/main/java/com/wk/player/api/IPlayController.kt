package com.wk.player.api

/**
 * 播放器控制接口
 * */
interface IPlayController {

    /**
     * 播放后退
     * */
    fun rewindPlay()

    /**
     * 播放前进
     * */
    fun forwardPlay()

    fun pausePlay()

    fun resumePlay()

    /**
     * 切换播放状态
     * */
    fun togglePlayState()

    fun seek(process:Int)

    fun destroy(reallyDestroy:Boolean)

}