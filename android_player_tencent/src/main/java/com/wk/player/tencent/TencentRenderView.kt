package com.wk.player.tencent

import android.content.Context
import com.tencent.rtmp.ui.TXCloudVideoView
import com.wk.player.api.IRenderView

class TencentRenderView : IRenderView {

    override fun getView(context: Context) = TXCloudVideoView(context)

}