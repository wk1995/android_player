package com.wk.player.api

import android.content.Context


/**
 * 播放器相关的工厂
 * */
abstract class AbstractPlayerFactory {

    /**
     * 创建直播播放器
     * */
    abstract fun createLivePlayer(context: Context): ILivePlayer?

    /**
     * 创建点播播放器
     * */
    abstract fun createVodPlayer(context: Context): IVodPlayer?

    /**
     * @return 播放器的视频渲染 View
     * */
    abstract fun createPlayView(context: Context): IRenderView?

}