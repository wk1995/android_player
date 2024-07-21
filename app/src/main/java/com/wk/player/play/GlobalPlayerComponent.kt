package com.wk.player.play

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.wk.android_player.R
import com.wk.player.GlobalPlayState
import com.wk.player.PlayComponentDataBean
import com.wk.player.PlayState
import com.wk.player.PlayType
import com.wk.player.api.AbsControlComponent
import io.feeeei.circleseekbar.CircleSeekBar
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.interfaces.OnFloatCallbacks
import com.wk.player.log.LogUtil
import com.wk.player.ScreenUtil
import com.wk.player.LiveActivity


/**
 * 全局播放组件
 * */
class GlobalPlayerComponent(context: Context) : AbsControlComponent<View, PlayComponentDataBean>(context = context),
    View.OnClickListener {

    companion object {
        private const val TAG = "GlobalPlayerComponent"
        private const val LIVE_PLAY = "live_play"
    }

    private lateinit var sbGlobalPlayProcess: CircleSeekBar
    private lateinit var ivGlobalPlayState: ImageView
    private lateinit var ivGlobalPlayImgDetail: ImageView
    private lateinit var tvGlobalPlayState: TextView
    private lateinit var ivGlobalPlayTitle: TextView
    /**
     * 是否是点播
     * 默认false 即不显示进度，不显示速率等UI
     * */
    private var isPlay: Boolean = false


    override fun createView(): View? {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.live_play_global_play, null, false)
        rootView.findViewById<View>(R.id.ivGlobalPlaySwitch).setOnClickListener(this)
        rootView.setOnClickListener(this)
        sbGlobalPlayProcess = rootView.findViewById(R.id.sbGlobalPlayProcess)
        ivGlobalPlayImgDetail = rootView.findViewById(R.id.ivGlobalPlayImgDetail)
        ivGlobalPlayState = rootView.findViewById(R.id.ivGlobalPlayState)
        tvGlobalPlayState = rootView.findViewById(R.id.tvGlobalPlayState)
        ivGlobalPlayTitle = rootView.findViewById(R.id.ivGlobalPlayTitle)
        sbGlobalPlayProcess.setOnClickListener(this)
        ivGlobalPlayState.setOnClickListener(this)
        return rootView
    }


    override fun onPlayStateChanged(playState: PlayState, playInfo: PlayComponentDataBean) {
        Glide.with(context.applicationContext).load(playInfo.imgDetail).into(ivGlobalPlayImgDetail)
        ivGlobalPlayTitle.text = playInfo.liveTitle
        ivGlobalPlayState.setImageResource(
            when {
                playState.isPlaying() -> {
                    R.drawable.live_paly_pause
                }
                else -> {
                    R.drawable.live_play_play
                }
            }
        )
        tvGlobalPlayState.text = when (playState) {
            PlayState.PLAY_STATE_PREVIEW -> {
                "直播预告"
            }
            PlayState.PLAY_STATE_END -> {
                if (isPlay) {
                    "直播回放"
                }else{
                    "直播结束"
                }
            }
            PlayState.PLAY_STATE_PLAYING_PAUSED -> {
                "直播暂停"
            }
            PlayState.PLAY_STATE_PLAYBACK->{
                "直播回放"
            }
            else -> {
                if (isPlay) {
                    "直播回放"
                } else {
                    "正在直播"
                }
            }
        }

    }

    override fun onPlayTypeChanged(playType: PlayType) {
        super.onPlayTypeChanged(playType)
        isPlay = playType.isPlay()
        sbGlobalPlayProcess.visibility = if (isPlay) View.VISIBLE else View.GONE
        ivGlobalPlayState.visibility = if (isPlay) View.VISIBLE else View.GONE
    }

    override fun onGlobalPlayStateChanged(globalPlayState: GlobalPlayState) {
        if (globalPlayState.canShow()) {
            play()
        } else {
            onDestroy()
        }
    }

    override fun setProgress(duration: Int, position: Int) {
        super.setProgress(duration, position)
        sbGlobalPlayProcess.maxProcess = duration
        sbGlobalPlayProcess.curProcess = position
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sbGlobalPlayProcess, R.id.ivGlobalPlayState -> {
                mControlWrapper?.togglePlayState()
            }
            R.id.ivGlobalPlaySwitch -> {
                mControlWrapper?.closeLive()
            }
            rootView?.id -> {

            }
        }
    }

    fun dismiss() {
        if (EasyFloat.getFloatView(LIVE_PLAY) != null) {
            EasyFloat.dismiss(LIVE_PLAY, true)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        val parent=rootView?.parent as? ViewGroup
        parent?.removeView(rootView)
        dismiss()
    }

    fun play() {
        if (EasyFloat.getFloatView(LIVE_PLAY) == null) {
            EasyFloat.with(context)
                .setTag(LIVE_PLAY)
                .setShowPattern(ShowPattern.FOREGROUND)
                .setLayout(rootView ?: return)
                .setMatchParent(widthMatch = true)
                .setAnimator(null)
                .setFilter(LiveActivity::class.java)
                .setLocation(x = 0, y = ScreenUtil.screenHeight * 5 / 6)
                .registerCallbacks(object : OnFloatCallbacks {
                    override fun createdResult(isCreated: Boolean, msg: String?, view: View?) {
                        LogUtil.d(TAG, "isCreated: $isCreated msg: $msg  ")
                    }

                    override fun show(view: View) {
                        view.visibility=View.VISIBLE
                    }

                    override fun hide(view: View) {
                        LogUtil.d(TAG, "hide  ")
                    }

                    override fun dismiss() {
                        LogUtil.d(TAG, "dismiss  ")
                    }

                    override fun touchEvent(view: View, event: MotionEvent) {
                        LogUtil.i(TAG, "touchEvent ${event.action.toString()}")

                    }

                    override fun drag(view: View, event: MotionEvent) {
                        LogUtil.d(TAG, "drag  ")
                    }

                    override fun dragEnd(view: View) {
                        LogUtil.d(TAG, "dragEnd  ")
                    }
                })
                .show()
        } else {
            EasyFloat.show(LIVE_PLAY)
        }
    }

}