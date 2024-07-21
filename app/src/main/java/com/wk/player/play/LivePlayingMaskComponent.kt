package com.wk.player.play

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.wk.android_player.R
import com.wk.player.PlayComponentDataBean
import com.wk.player.PlayState
import com.wk.player.PlayType
import com.wk.player.api.AbsControlComponent
import com.wk.player.log.LogUtil
import com.wk.player.DateUtils

/**
 * 控制组件
 * */
class LivePlayingMaskComponent(context: Context) : AbsControlComponent<View, PlayComponentDataBean>(context = context),
    OnClickListener {

    companion object {
        private const val TAG = "LivePlayingMaskControl"
    }

    private lateinit var ivLivePlay: ImageView
    private lateinit var ivPlayRewind: ImageView
    private lateinit var ivPlayForward: ImageView
    private lateinit var ivLivePlayFullScreen: ImageView
    private lateinit var sbLivePlayProgress: SeekBar
    private lateinit var tvLivePlayCurrentProcessTime: TextView
    private lateinit var tvLivePlayEndProcessTime: TextView
    private lateinit var tvLivePlaySpeed: TextView

    override fun createView(): View? {
        rootView =
            LayoutInflater.from(context).inflate(R.layout.live_playing_mask_progress, null, false)
        rootView?.apply {
            listOf(
                findViewById(R.id.ivLivePlayBack),
                findViewById(R.id.ivLivePlayClose),
                findViewById<ImageView>(R.id.ivLivePlayShare),
            ).forEach { childView ->
                childView.setOnClickListener(this@LivePlayingMaskComponent)
            }
            ivLivePlayFullScreen = findViewById<ImageView>(R.id.ivLivePlayFullScreen)
            ivPlayRewind = findViewById(R.id.ivPlayRewind)
            ivPlayForward = findViewById(R.id.ivPlayForward)
            tvLivePlaySpeed = findViewById(R.id.tvLivePlaySpeed)
            tvLivePlaySpeed.setOnClickListener(this@LivePlayingMaskComponent)
            ivPlayRewind.setOnClickListener(this@LivePlayingMaskComponent)
            ivPlayForward.setOnClickListener(this@LivePlayingMaskComponent)
            ivLivePlayFullScreen.setOnClickListener(this@LivePlayingMaskComponent)
            ivLivePlay = findViewById(R.id.ivLivePlay)
            ivLivePlay.setOnClickListener(this@LivePlayingMaskComponent)

            tvLivePlayCurrentProcessTime =
                findViewById(R.id.tvLivePlayCurrentProcessTime)
            tvLivePlayEndProcessTime = findViewById(R.id.tvLivePlayEndProcessTime)
            sbLivePlayProgress = findViewById(R.id.sbLivePlayProgress)
            sbLivePlayProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    LogUtil.d(TAG, "progress: $progress fromUser: $fromUser")
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    LogUtil.d(TAG, "onStartTrackingTouch")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    LogUtil.d(TAG, "onStopTrackingTouch")
                    val progress = seekBar?.progress ?: return
                    mControlWrapper?.seek(progress)
                    tvLivePlayCurrentProcessTime.text = DateUtils.formatTime(progress)
                }
            })
        }

        return rootView
    }


    override fun onPlayTypeChanged(playType: PlayType) {
        super.onPlayTypeChanged(playType)
        val isLive = !playType.isPlay()
        arrayOf(
            tvLivePlaySpeed,
            sbLivePlayProgress,
            ivLivePlayFullScreen,
            tvLivePlayEndProcessTime,
            ivPlayRewind,
            ivPlayForward,
            tvLivePlayCurrentProcessTime,
            ivLivePlay
        ).forEach {
            it.visibility = if (isLive) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    override fun onPlayStateChanged(playState: PlayState, playInfo: PlayComponentDataBean) {
        ivLivePlay.setImageResource(
            when {
                playState.isPlaying() -> {
                    R.drawable.live_paly_pause
                }
                else -> {
                    R.drawable.live_play_play
                }
            }
        )
    }

    override fun rateChange(rate: Float) {
        super.rateChange(rate)
        tvLivePlaySpeed.text = rate.toString()
    }

    override fun setProgress(duration: Int, position: Int) {
        super.setProgress(duration, position)
        sbLivePlayProgress.max = duration
        sbLivePlayProgress.progress = position
        tvLivePlayCurrentProcessTime.text = DateUtils.formatTime(position)
        tvLivePlayEndProcessTime.text = DateUtils.formatTime(duration)
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {
        super.onVisibilityChanged(isVisible, anim)
        rootView?.apply {
            if (visibility != View.VISIBLE && isVisible) {
                visibility = View.VISIBLE
                startAnimation(anim)
            } else if (visibility == View.VISIBLE && !isVisible) {
                visibility = View.GONE
                startAnimation(anim)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivLivePlayBack -> {
                mControlWrapper?.back()
            }
            R.id.ivLivePlayClose -> {
                mControlWrapper?.closeLive()
            }
            R.id.ivLivePlayShare -> {
                mControlWrapper?.shareLive()
            }
            R.id.tvLivePlaySpeed -> {
                mControlWrapper?.showSpeechChoices()
            }
            R.id.ivPlayRewind -> {
                mControlWrapper?.rewindPlay()
            }
            R.id.ivPlayForward -> {
                mControlWrapper?.forwardPlay()
            }
            R.id.ivLivePlayFullScreen -> {
                mControlWrapper?.toggleFullScreenState()
            }
            R.id.ivLivePlay -> {
                mControlWrapper?.togglePlayState()
            }
        }
    }
}