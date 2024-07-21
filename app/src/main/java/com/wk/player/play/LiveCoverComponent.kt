package com.wk.player.play

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wk.player.PlayComponentDataBean
import com.wk.player.PlayState
import com.wk.player.api.AbsControlComponent


/**
 * 封面
 *
 * */
class LiveCoverComponent(context: Context) :
    AbsControlComponent<View, PlayComponentDataBean>(context = context) {

    private val showCoverPlayState by lazy {
        arrayOf(
            PlayState.PLAY_STATE_END,
            PlayState.PLAY_STATE_PREVIEW,
            PlayState.PLAY_STATE_PLAYING_PAUSED,
            PlayState.PLAY_STATE_ERROR
        )
    }

    override fun createView(): ImageView {
        return ImageView(context)
    }


    override fun getViewParams(): ViewGroup.LayoutParams {
        return FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onPlayStateChanged(playState: PlayState, playInfo: PlayComponentDataBean) {
        val imageView = rootView as? ImageView ?: return
        imageView.visibility = View.GONE
        if (playState in showCoverPlayState && !playInfo.playType.isPlay()) {
            imageView.visibility = View.VISIBLE
            Glide.with(context).load(playInfo.imgDetail).into(imageView)
        }
    }
}