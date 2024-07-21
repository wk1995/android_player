package com.wk.player.api

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.wk.player.ControlWrapper
import com.wk.player.GlobalPlayState
import com.wk.player.PlayState
import com.wk.player.PlayType

/**
 * 组件接口
 * */
abstract class AbsControlComponent<T : View, R>(
    protected var rootView: T? = null,
    val context: Context
) {

    companion object {
        private const val TAG = "AbsControlComponent"
    }

    protected var mControlWrapper: ControlWrapper? = null


    fun setControlWrapper(controlWrapper: ControlWrapper?) {
        mControlWrapper = controlWrapper
    }


    open fun createView(): T? {
        return null
    }

    fun getView(): View? {
        if (rootView == null) {
            rootView = createView()
        }
        return rootView
    }

    /**
     * 布局参数
     * */
    open fun getViewParams(): ViewGroup.LayoutParams? = null

    /**
     * 添加view的序号，小于0的会放在父布局最后一层
     * */
    open fun getViewIndex(): Int = -1


    /**
     * 回调控制器显示和隐藏状态，
     * 此方法可用于控制 [AbsControlComponent] 中的控件的跟随手指点击显示和隐藏
     * @param isVisible true 代表要显示， false 代表要隐藏
     * @param anim 显示和隐藏的动画，是一个补间Alpha动画
     */
    open fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {
        log("isVisible: $isVisible")
    }

    /**
     * 回调播放器的状态
     * @param playState 播放器状态
     */
    open fun onPlayStateChanged(playState: PlayState, playInfo: R) {
        log("onPlayStateChanged: ${playState.stateName}")
    }

    /**
     * 播放类型变更
     * */
    open fun onPlayTypeChanged(playType: PlayType) {
        log("onPlayTypeChanged: $playType")
    }

    /**
     * 全局播放器状态变更 see [GlobalPlayState]
     * */
    open fun onGlobalPlayStateChanged(globalPlayState: GlobalPlayState) {
        log("onGlobalPlayStateChanged: $globalPlayState")
    }

    /**
     * 速率变化回调
     * */
    open fun rateChange(rate: Float) {
        log("rateChange: $rate")
    }
    /**
     * 回调播放进度
     * @param duration 视频总时长
     * @param position 播放进度
     */
    open fun setProgress(duration: Int, position: Int) {
        log("setProgress: duration $duration  position  $position")
    }

    /**
     * 回调控制器是否被锁定，锁定后会产生如下影响：
     * 无法响应滑动手势，双击事件，点击显示和隐藏控制UI，跟随重力感应切换横竖屏
     * @param isLocked 是否锁定
     */
    open fun onLockStateChanged(isLocked: Boolean) {
        log("onLockStateChanged: isLocked $isLocked ")
    }

    open fun onDestroy() {
        log("onDestroy: ")
    }

    private fun log(msg: String) {
        Log.d(TAG, "${this.javaClass.simpleName}    $msg")
    }
}