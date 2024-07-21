package com.wk.player.play

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wk.android_player.R
import com.wk.player.PlayComponentDataBean
import com.wk.player.PlayState
import com.wk.player.PlayType
import com.wk.player.api.AbsControlComponent
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

/**
 * 播放状态的组件
 * */
class PlayStateMaskTipsComponent(context: Context) :
    AbsControlComponent<View, PlayComponentDataBean>(context = context),
    View.OnClickListener {

    private lateinit var tvLivePlayMaskStart: TextView
    private lateinit var tvLivePlayMaskContent: TextView
    private lateinit var tvLivePlayMaskTips: TextView
    private lateinit var tvLivePlayMaskOperate: TextView
    private val mDisposables by lazy {
        CompositeSubscription()
    }

    /**
     * 是否预约
     * */
    private var hasReservation = false

    override fun createView(): View? {
        val rootView =
            LayoutInflater.from(context)
                .inflate(R.layout.live_play_mask_live_playback, null, false)
        tvLivePlayMaskStart = rootView.findViewById(R.id.tvLivePlayMaskStart)
        tvLivePlayMaskContent = rootView.findViewById(R.id.tvLivePlayMaskContent)
        tvLivePlayMaskTips = rootView.findViewById(R.id.tvLivePlayMaskTips)
        tvLivePlayMaskOperate = rootView.findViewById(R.id.tvLivePlayMaskOperate)
        rootView.findViewById<View>(R.id.tvLivePlaySpeed).visibility = View.GONE
        tvLivePlayMaskOperate.setOnClickListener(this)

        rootView?.apply {
            listOf(
                findViewById<ImageView>(R.id.ivLivePlayBack),
                findViewById<ImageView>(R.id.ivLivePlayClose),
                findViewById<ImageView>(R.id.ivLivePlayShare),
            ).forEach { childView ->
                childView.setOnClickListener(this@PlayStateMaskTipsComponent)
            }
        }
        return rootView
    }

    override fun onPlayStateChanged(playState: PlayState, playInfo: PlayComponentDataBean) {
        super.onPlayStateChanged(playState, playInfo)
        rootView?.visibility = View.VISIBLE
        mDisposables.clear()
        when (playState) {
            //直播结束提示：
            PlayState.PLAY_STATE_END -> {
                if(playInfo.playType.isPlay()){
                    rootView?.visibility = View.GONE
                }
                tvLivePlayMaskStart.visibility = View.GONE
                tvLivePlayMaskContent.visibility = View.VISIBLE
                tvLivePlayMaskContent.text = "直播结束"
                if (playInfo.supportPlayback && playInfo.playType != PlayType.PLAY && playInfo.isInEffectiveTime) {
                    tvLivePlayMaskTips.visibility = View.VISIBLE
                    tvLivePlayMaskTips.text = "回放视频制作中，请稍后观看"
                } else {
                    tvLivePlayMaskTips.visibility = View.GONE
                }
                tvLivePlayMaskOperate.visibility = View.GONE
            }
            //预约
            PlayState.PLAY_STATE_PREVIEW -> {
                if (playInfo.playType == PlayType.PLAY) {
                    rootView?.visibility = View.GONE
                    return
                }

                tvLivePlayMaskStart.visibility = View.VISIBLE
                tvLivePlayMaskTips.visibility = View.GONE
                var time = playInfo.startTimeCountdown.toLong()
                val startTime = time + System.currentTimeMillis() / 1000
                if (time > 0) {
                    tvLivePlayMaskContent.text = getDetailTime(time)
                    tvLivePlayMaskStart.text = "距开播还有"
                    tvLivePlayMaskContent.visibility = View.VISIBLE
                    mDisposables.add(Observable.interval(500, TimeUnit.MILLISECONDS)
                        .takeUntil {
                            time < 0
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            time = startTime - System.currentTimeMillis() / 1000
                            playInfo.startTimeCountdown = time.toInt()
                            tvLivePlayMaskContent.text = getDetailTime(time)
                            if (time < 0) {
                                rootView?.visibility = View.GONE
                            }
                        })
                    tvLivePlayMaskOperate.visibility = View.VISIBLE
                    hasReservation = playInfo.hasReservation
                    if (hasReservation) {
                        tvLivePlayMaskOperate.text = "已预约"
                        tvLivePlayMaskOperate.setBackgroundResource(R.drawable.live_paly_mask_has_deal_bg)
                    } else {
                        tvLivePlayMaskOperate.text = "预约"
                        tvLivePlayMaskOperate.setBackgroundResource(R.drawable.live_paly_mask_order_bg)
                    }
                } else {
                    tvLivePlayMaskOperate.visibility = View.GONE
                    tvLivePlayMaskStart.visibility = View.GONE
                    tvLivePlayMaskContent.visibility = View.GONE
                    rootView?.visibility = View.GONE
                }

            }
            //中途暂停
            PlayState.PLAY_STATE_PLAYING_PAUSED, PlayState.PLAY_STATE_ERROR -> {
                if (playInfo.playType.isPlay()) {
                    rootView?.visibility = View.GONE
                    return
                }
                tvLivePlayMaskStart.visibility = View.GONE
                tvLivePlayMaskContent.visibility = View.VISIBLE
                tvLivePlayMaskContent.text = "主播暂时离开，请稍后"
                tvLivePlayMaskTips.visibility = View.GONE
                tvLivePlayMaskOperate.visibility = View.GONE
            }
            else -> {
                rootView?.visibility = View.GONE
            }
        }
    }

    /**
     * 时间戳 单位s
     * */
    private fun getDetailTime(time: Long): String {
        val second = time % 60
        val minu = (time - second) / 60 % 60
        val hour = (time - second - minu * 60) / (60 * 60) % 24
        val day = time / (24 * 60 * 60)
        return "$day 天 $hour 小时 $minu 分 $second 秒"
    }


    /**
     * 是否登录
     * */
    private fun hasLogin(): Boolean = true

    /**
     * 是否预约过
     * */
    private fun hasOrder(): Boolean = hasReservation

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
            R.id.tvLivePlayMaskOperate -> {
                if (!hasOrder()) {
                    mControlWrapper?.apply {
                        hasReservation = true
                        tvLivePlayMaskOperate.text = "已预约"
                        tvLivePlayMaskOperate.setBackgroundResource(R.drawable.live_paly_mask_has_deal_bg)
                        reservation()
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
    }
}