package com.wk.player.play

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.wk.player.PlayComponentDataBean
import com.wk.player.api.AbsControlComponent
import com.wk.player.api.BaseVideoPlayViewContainer
import com.wk.player.api.IGestureDetector
import com.wk.player.log.LogUtil

class VideoViewContainer : BaseVideoPlayViewContainer<PlayComponentDataBean>, IGestureDetector, View.OnTouchListener,
    IGestureListener {

    companion object {
        private const val TAG = "VideoView"
    }

    private var mGestureDetector: GestureDetector? = null


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun initView(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        super.initView(context, attrs, defStyleAttr, defStyleRes)
        setOnTouchListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogUtil.d(TAG, "onTouchEvent: ${eventToString(event)}")
        return touchEvent(event) || super.onTouchEvent(event)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        LogUtil.d(TAG, "onTouch: ${eventToString(event)}")
        return touchEvent(event)
    }

    override fun getGestureDetector(): GestureDetector? {
        if (mGestureDetector == null) {
            mGestureDetector = GestureDetector(context, this)
        }
        return mGestureDetector
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        mController?.toggleShowState() ?: return false
        return true
    }


    fun addComponentToThis(controlComponent: AbsControlComponent<in View, PlayComponentDataBean>, isDissociate: Boolean = false){
        mController?.addComponentCache(controlComponent,isDissociate)
        val view = controlComponent.getView()
        if (!isDissociate && view != null) {
            view.visibility = View.GONE
            val params = controlComponent.getViewParams()
            val viewIndex = controlComponent.getViewIndex()
            if (params != null) {
                addView(view, viewIndex, params)
            } else {
                addView(view, viewIndex)
            }
        }
    }

}