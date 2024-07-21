package com.wk.player

import android.content.Context
import com.wk.player.api.*

/**
 * 空布局，不能播放视频
 * */
object EmptyPlayerFactory : AbstractPlayerFactory() {
    override fun createLivePlayer(context: Context): ILivePlayer? {
        return null
    }

    override fun createVodPlayer(context: Context): IVodPlayer? {
        return null
    }

    override fun createPlayView(context: Context): IRenderView? {
        return null
    }
}