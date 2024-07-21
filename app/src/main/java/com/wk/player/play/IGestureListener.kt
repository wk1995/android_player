package com.wk.player.play

import android.view.GestureDetector
import android.view.MotionEvent
import com.wk.player.log.LogUtil

/**
 * 触摸手势事件整合
 * */
interface IGestureListener : GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    companion object {
        private const val TAG = "IGestureListener"
    }

    override fun onDown(e: MotionEvent): Boolean {
        LogUtil.d(TAG, "onDown : ${eventToString(e)}")
        return true
    }

    override fun onShowPress(e: MotionEvent) {
        LogUtil.d(TAG, "onShowPress : ${eventToString(e)}")
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        LogUtil.d(TAG, "onSingleTapUp : ${eventToString(e)}")
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        LogUtil.d(TAG, "onScroll : e1 ${eventToString(e1)}  e2  ${eventToString(e2)}")
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        LogUtil.d(TAG, "onLongPress : ${eventToString(e)}")
    }


    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return true
    }


    override fun onDoubleTap(e: MotionEvent): Boolean {
        LogUtil.d(TAG, "onDoubleTap : ${eventToString(e)}")
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        LogUtil.d(TAG, "onDoubleTapEvent : ${eventToString(e)}")
        return true
    }

    private fun eventToString(e: MotionEvent?): String {
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

}