package com.wk.player.api

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wk.player.ControlWrapper
import com.wk.player.PlayState

/**
 *
 *  放 真正【播放器View】的【容器View】，用来承载播放器View
 *
 * 不能直接使用播放器View，由于播放器控件不确定是[View]还是[ViewGroup]
 * 并且很有可能会有一些额外的功能控件叠在【播放器View】上，所以【播放器View】一定要在一个[ViewGroup]之上
 * */
abstract class BaseVideoPlayViewContainer<R:BasePlayComponentDataBean> : FrameLayout {

    companion object {
        private const val TAG = "BaseVideoPlayView"
    }

    /**
     * 真正的【播放器View】
     * */
    private var mRealRenderView: View? = null

    /**
     * 播放控制
     * */
    var mController: BasePlayController<R>? = null
        set(value) {
            field = value
            field?.mRenderView = null
            if (mRealRenderView != null) {
                val realRenderViewParent = mRealRenderView?.parent
                if (realRenderViewParent is ViewGroup) {
                    realRenderViewParent.removeView(mRealRenderView)
                }
            }
            mRealRenderView = getPlayerFactory()?.createPlayView(context)?.getView(context)
            addView(
                mRealRenderView, 0, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            field?.mRenderView = mRealRenderView
        }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context, attrs, defStyleAttr)
    }

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }


    open fun initView(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
    ) {

    }

    private fun getPlayerFactory(): AbstractPlayerFactory? = mController?.getPlayerFactory()

    protected fun eventToString(e: MotionEvent?): String {
        if (e == null) {
            return "null"
        }
        return when (e.action) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
            else -> "unknow"
        }
    }

    fun pausePlay(isManagerOperate: Boolean) {
        mController?.changePlayState(
            if (isManagerOperate) {
                com.wk.player.PlayState.PLAY_STATE_PLAYING_PAUSED
            } else {
                com.wk.player.PlayState.PLAY_STATE_PAUSED
            }
        )
    }

    fun setRate(rate: Float) {
        mController?.setRate(rate)
    }

    fun getRate() = mController?.getRate() ?: 1.0f

    fun destroy(reallyDestroy: Boolean) {
        mController?.destroy(reallyDestroy)
    }

}