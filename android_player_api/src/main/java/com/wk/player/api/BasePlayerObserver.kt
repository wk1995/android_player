package com.wk.player.api

import android.graphics.Bitmap
import android.os.Bundle

/**
 * 播放器回调通知。
 * 可以接收 播放器的一些回调通知，包括播放器状态、播放音量回调、音视频首帧回调、统计数据、警告和错误信息等
 * */
abstract class BasePlayerObserver {

    /**
     * 直播播放器错误通知，播放器出现错误时，会回调该通知
     * @param player    回调该通知的播放器对象
     * @param code    错误码 [com.wk.player.constants.LiveCode]
     * @param msg    错误信息
     * @param extraInfo    扩展信息
     * */
    open fun onError(player: IMediaPlayer?, code: Int, msg: String?, extraInfo: Bundle?) {
    }

    /**
     * 直播播放器警告通知
     * @param msg 警告信息
     * @param code    错误码 [com.wk.player.constants.LiveCode]
     * @param extraInfo 扩展信息
     * */
    open fun onWarning(player: IMediaPlayer?, code: Int, msg: String?, extraInfo: Bundle?) {
    }

    /**
     * 直播播放器分辨率变化通知
     * @param player    回调该通知的播放器对象
     * @param width    视频宽
     * @param height    视频高
     * */
    open fun onVideoResolutionChanged(player: IMediaPlayer?, width: Int, height: Int) {
    }

    /**
     * 已经成功连接到服务器
     * @param extraInfo    扩展信息
     * */
    open fun onConnected(player: IMediaPlayer?, extraInfo: Bundle?) {}

    /**
     * 视频播放事件
     * @param player    回调该通知的播放器对象
     * @param firstPlay    第一次播放标志
     * @param extraInfo    扩展信息
     * */
    open fun onVideoPlaying(player: IMediaPlayer?, firstPlay: Boolean, extraInfo: Bundle?) {
    }

    /**
     * 音频播放事件
     * @param firstPlay    第一次播放标志
     * @param extraInfo    扩展信息
     * */
    open fun onAudioPlaying(player: IMediaPlayer?, firstPlay: Boolean, extraInfo: Bundle?) {}

    /**
     * 视频加载事件
     * @param extraInfo    扩展信息
     * */
    open fun onVideoLoading(player: IMediaPlayer?, extraInfo: Bundle?) {}

    /**
     * 音频加载事件
     * @param extraInfo    扩展信息
     * */
    open fun onAudioLoading(player: IMediaPlayer?, extraInfo: Bundle?) {}

    /**
     * 播放器音量大小回调
     * @param volume    音量大小
     *
     * 调用 [com.wk.player.api.ILivePlayer.enableVolumeEvaluation] 开启播放音量大小提示之后，会收到这个回调通知
     * */
    open fun onPlayoutVolumeUpdate(player: IMediaPlayer?, volume: Int) {}

    /**
     * 截图回调
     * @param image    已截取的视频画面
     * */
    open fun onSnapshotComplete(player: IMediaPlayer?, image: Bitmap?) {}


    /**
     * 直播播放器统计数据回调
     * @param  player    回调该通知的播放器对象
     * */
    open fun onStatisticsUpdate(player: IMediaPlayer?) {

    }


    /**
     * 收到 SEI 消息的回调，发送端通过 V2TXLivePusher 中的 sendSeiMessage 来发送 SEI 消息。
     * 调用 [com.wk.player.api.ILivePlayer.enableReceiveSeiMessage]开启接收 SEI 消息之后，会收到这个回调通知
     * @param payloadType    回调数据的SEI payloadType
     * @param data    数据
     * */
    open fun onReceiveSeiMessage(player: IMediaPlayer?, payloadType: Int, data: ByteArray?) {}

    /**
     * 点播播放事件通知.
     * @param event    事件id。id类型请参考[com.wk.player.constants.PlayEventConstants]
     * @param param    事件相关的参数.(key,value)格式 其中key请参考[com.wk.player.constants.PlayEventParamsConstants]
     * */
    open fun onPlayEvent(player: IMediaPlayer?, event: Int, param: Bundle?) {

    }

    /**
     * 点播网络状态通知.
     * @param status    通知的内容.(key,value)格式 其中key请参考[com.wk.player.constants.PlayNetStateParamsConstants]
     * */
    open fun onNetStatus(player: IMediaPlayer?, status: Bundle?) {

    }

}