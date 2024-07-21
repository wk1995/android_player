package com.wk.player.api

import android.view.View
import com.wk.player.bean.PlayRotation

/**
 * 抽象的播放器，继承此抽象类扩展自己的播放器
 * 对于系统原生播放器来说 对应就是封装系统的[android.media.MediaPlayer]
 * */
interface IMediaPlayer {

    /**
     * 播放音视频流
     * */
    fun startPlay(playUrl: String): Int

    /**
     * 暂停播放音视频流
     * */
    fun pausePlay(isManagerOperate: Boolean = false)

    /**
     * 回复播放音视频流
     * @param isManagerOperate 是否是管理员操作 true 管理员操作，普通用户不能干预
     * */
    fun resumePlay(isManagerOperate: Boolean)

    /**
     * 停止播放音视频流
     * */
    fun stopPlay()

    /**
     * 设置播放器音量
     * */
    fun setPlayVolume(volume: Int)

    /**
     * 设置播放器的视频渲染 View。 该控件负责显示视频内容
     * */
    fun setRenderView(playView: View?)

    /**
     * 设置播放器画面的旋转角度
     * */
    fun setRenderRotation(playRotation: PlayRotation)

    /**
     * 播放器是否正在播放中
     * */
    fun isPlaying(): Boolean

    /**
     * 截图
     * */
    fun screenshot()

    /**
     * 设置播放器回调
     * */
    fun setObserver(livePlayerObserver: BasePlayerObserver?)

    fun removeObserver()

    /**
     * @param reallyDestroy 是否真正的摧毁；false 只释放视图View相关对象
     * */
    fun destroy(reallyDestroy: Boolean)
}