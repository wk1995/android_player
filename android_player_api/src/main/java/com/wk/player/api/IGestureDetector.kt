package com.wk.player.api

import android.view.GestureDetector
import android.view.MotionEvent

interface IGestureDetector  {

    fun getGestureDetector():GestureDetector?

    fun touchEvent(event: MotionEvent?): Boolean {
        return getGestureDetector()?.onTouchEvent(event) == true
    }

}