package com.wk.player.play

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import com.wk.android_player.R
import com.wk.player.PlayComponentDataBean
import com.wk.player.PlayState
import com.wk.player.api.AbsControlComponent
import com.wk.player.view.DynamicWaterConfig
import com.wk.player.view.DynamicWatermarkView

class UserIdComponent(context: Context) :
    AbsControlComponent<View, PlayComponentDataBean>(context = context) {
    private var mWaterMark: DynamicWatermarkView? = null

    private var dynamicWaterConfig: DynamicWaterConfig? = null

    override fun createView(): View? {
        rootView =
            LayoutInflater.from(context).inflate(R.layout.live_component_user_id, null, false)
        mWaterMark = rootView?.findViewById(R.id.water_mark)
        return rootView
    }

    override fun onPlayStateChanged(playState: PlayState, playInfo: PlayComponentDataBean) {
        if (playInfo.disableScreenshot && (playState.isPlaying() || playState == PlayState.PLAY_STATE_PLAYING_RESTORE)) {
            mWaterMark?.visibility = View.VISIBLE
            rootView?.visibility = View.VISIBLE
            if (dynamicWaterConfig == null) {
                dynamicWaterConfig = DynamicWaterConfig(
                    playInfo.userId,
                    14.sp.toInt(),
                    Color.parseColor("#B2FFFFFF")
                )
                mWaterMark?.setData(
                    dynamicWaterConfig
                )
            }
            mWaterMark?.show()
        } else {
            mWaterMark?.hide()
        }
    }

    override fun onDestroy() {
        mWaterMark?.release()
        dynamicWaterConfig = null
    }
    val Int.sp: Float
        get() = this * Resources.getSystem().displayMetrics.scaledDensity

}