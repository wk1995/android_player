package com.wk.player.tencent

import android.content.Context
import com.wk.player.api.*
import com.wk.player.tencent.TencentLivePlayer

object TencentPlayerFactory : AbstractPlayerFactory() {

    override fun createLivePlayer(context: Context): ILivePlayer {
        return TencentLivePlayer.obtain(context)
    }

    override fun createVodPlayer(context: Context): IVodPlayer {
        return TencentVodPlayer.obtain(context)
    }

    override fun createPlayView(context: Context): IRenderView {
        return TencentRenderView()
    }
}
