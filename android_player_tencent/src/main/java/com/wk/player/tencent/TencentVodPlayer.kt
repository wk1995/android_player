package com.wk.player.tencent

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import com.tencent.liteav.txcplayer.model.TXSubtitleRenderModel
import com.tencent.liteav.txcvodplayer.renderer.TextureRenderView
import com.tencent.rtmp.ITXVodPlayListener
import com.tencent.rtmp.TXVodPlayer
import com.tencent.rtmp.ui.TXCloudVideoView
import com.wk.player.api.BasePlayerObserver
import com.wk.player.api.IVodPlayer
import com.wk.player.bean.PlayBitrateItem
import com.wk.player.bean.PlayRotation
import com.wk.player.bean.PlayTrackInfo
import com.wk.player.bean.SubtitleRenderModel

class TencentVodPlayer(context: Application) : IVodPlayer {
    companion object {
        private const val TAG = "TencentVodPlayer"


        @SuppressLint("StaticFieldLeak")
        private var mTencentVodPlayer: TencentVodPlayer? = null

        /**
         * 后续可以用享元实现
         * */
        fun obtain(context: Context): TencentVodPlayer {
            if (mTencentVodPlayer == null) {
                mTencentVodPlayer = TencentVodPlayer(context.applicationContext as Application)
            }
            return mTencentVodPlayer!!
        }
    }

    /**
     * 腾讯云点播播放器接口类
     * 播放器包含如下能力：

     * 支持 RTMP、FLV、HLS、MP4、MP3、WebRTC 等丰富的音视频格式；
     * 支持网络视频的 URL 方式播放，URL 可以为点播播放地址也可以为直播拉流地址；
     * 支持使用云点播的 FileID 方式播放；
     * 支持精准快速的 seek 到指定位置播放；
     * 播放器 SDK 官网：https://cloud.tencent.com/document/product/881
     * */
    private val mTXVodPlayer: TXVodPlayer = TXVodPlayer(context)
    private var playerObserver: BasePlayerObserver? = null

    private val mITXVodPlayListener by lazy {
        val mITXVodPlayListener = object : ITXVodPlayListener {
            override fun onPlayEvent(p0: TXVodPlayer?, event: Int, param: Bundle?) {
                log("event: $event param :$param")
                playerObserver?.onPlayEvent(this@TencentVodPlayer, event, param)
            }

            override fun onNetStatus(p0: TXVodPlayer?, status: Bundle?) {
                log("param :$status")
                playerObserver?.onNetStatus(this@TencentVodPlayer, status)
            }
        }
        mITXVodPlayListener
    }

    /**
     *
     * @return 是否成功启动播放。 0: 成功
     * -1： 失败
     * -5：licence 不合法，播放失败
     * */
    override fun startPlay(playUrl: String)=mTXVodPlayer.startVodPlay(playUrl)

    override fun pausePlay(isManagerOperate: Boolean) {
        mTXVodPlayer.pause()
    }

    override fun resumePlay(isManagerOperate: Boolean) {
        mTXVodPlayer.resume()
    }

    override fun setPlayVolume(volume: Int) {
        mTXVodPlayer.setAudioPlayoutVolume(volume)
    }

    private var mPlayView: View? = null

    override fun setRenderView(playView: View?) {
        if (playView is TXCloudVideoView?) {
            mTXVodPlayer.setPlayerView(playView)
            mPlayView = playView
        } else if (playView is TextureRenderView) {
            mTXVodPlayer.setPlayerView(playView)
            mPlayView = playView
        } else {
            throw java.lang.Exception(" playView ${playView?.javaClass?.simpleName} not support")
        }

    }

    override fun setRenderRotation(playRotation: PlayRotation) {
        mTXVodPlayer.setRenderRotation(playRotation.rotation)
    }

    override fun isPlaying() = mTXVodPlayer.isPlaying

    override fun destroy(reallyDestroy: Boolean) {
        (mPlayView as? TXCloudVideoView)?.onDestroy()
        setRenderView(null)
        if (reallyDestroy) {
            mTXVodPlayer.stopPlay(true)
            removeObserver()
            mTencentVodPlayer = null
        }
    }

    override fun setConfig() {
//        mTXVodPlayer.setConfig
    }

    override fun setSubtitleView() {
//        mTXVodPlayer.setSubtitleView()
    }

    override fun addSubtitleSource(url: String, name: String, mimeType: String?) {
        mTXVodPlayer.addSubtitleSource(url, name, mimeType)
    }

    override fun selectTrack(trackIndex: Int) {
        mTXVodPlayer.selectTrack(trackIndex)
    }

    override fun deselectTrack(trackIndex: Int) {
        mTXVodPlayer.deselectTrack(trackIndex)
    }

    override fun getSubtitleTrackInfo(): List<PlayTrackInfo> {
        return mTXVodPlayer.subtitleTrackInfo.map {
            PlayTrackInfo(it.trackIndex, it.name, it.trackType)
        }
    }

    override fun getAudioTrackInfo(): List<PlayTrackInfo> {
        return mTXVodPlayer.audioTrackInfo.map {
            PlayTrackInfo(it.trackIndex, it.name, it.trackType)
        }
    }

    override fun setSubtitleStyle(renderModel: SubtitleRenderModel) {
        val mTXSubtitleRenderModel = TXSubtitleRenderModel()
        mTXSubtitleRenderModel.canvasHeight = renderModel.canvasHeight
        mTXSubtitleRenderModel.canvasWidth = renderModel.canvasWidth
        mTXSubtitleRenderModel.familyName = renderModel.familyName
        mTXSubtitleRenderModel.fontSize = renderModel.fontSize
        mTXSubtitleRenderModel.fontScale = renderModel.fontScale
        mTXSubtitleRenderModel.fontColor = renderModel.fontColor
        mTXSubtitleRenderModel.isBondFontStyle = renderModel.isBondFontStyle
        mTXSubtitleRenderModel.outlineWidth = renderModel.outlineWidth
        mTXSubtitleRenderModel.outlineColor = renderModel.outlineColor
        mTXSubtitleRenderModel.lineSpace = renderModel.lineSpace
        mTXSubtitleRenderModel.startMargin = renderModel.startMargin
        mTXSubtitleRenderModel.endMargin = renderModel.endMargin
        mTXSubtitleRenderModel.verticalMargin = renderModel.verticalMargin
        mTXVodPlayer.setSubtitleStyle(mTXSubtitleRenderModel)
    }

    override fun setSurface(surface: Surface?) {
        mTXVodPlayer.setSurface(surface)
    }

    override fun stopPlay(isNeedClearLastImg: Boolean): Int {
        return mTXVodPlayer.stopPlay(isNeedClearLastImg)
    }

    override fun seek(time: Int) {
        mTXVodPlayer.seek(time)
    }

    override fun seek(time: Float) {
        mTXVodPlayer.seek(time)
    }

    override fun getCurrentPlaybackTime() = mTXVodPlayer.currentPlaybackTime

    override fun getBufferDuration() = mTXVodPlayer.bufferDuration

    override fun getDuration() = mTXVodPlayer.duration

    override fun getPlayableDuration() = mTXVodPlayer.playableDuration

    override fun getWidth() = mTXVodPlayer.width

    override fun getHeight() = mTXVodPlayer.height

    override fun setRenderMode(mode: Int) {
        mTXVodPlayer.setRenderMode(mode)
    }

    override fun enableHardwareDecode(enable: Boolean): Boolean {
        return mTXVodPlayer.enableHardwareDecode(enable)
    }

    override fun setMute(mute: Boolean) {
        mTXVodPlayer.setMute(mute)
    }

    override fun setRequestAudioFocus(requestFocus: Boolean): Boolean {
        return mTXVodPlayer.setRequestAudioFocus(requestFocus)
    }

    override fun setAutoPlay(autoPlay: Boolean) {
        mTXVodPlayer.setAutoPlay(autoPlay)
    }

    override fun setRate(rate: Float) {
        mTXVodPlayer.setRate(rate)
    }

    override fun getBitrateIndex(): Int {
        return mTXVodPlayer.bitrateIndex
    }

    override fun setBitrateIndex(index: Int) {
        mTXVodPlayer.bitrateIndex = index
    }

    override fun getSupportedBitrates(): List<PlayBitrateItem> {
        return mTXVodPlayer.supportedBitrates.map {
            PlayBitrateItem(it.index, it.width, it.height, it.bitrate)
        }
    }

    override fun screenshot() {
        mTXVodPlayer.snapshot { bmp ->
            playerObserver?.onSnapshotComplete(this, bmp)
        }
    }

    override fun setMirror(mirror: Boolean) {
        mTXVodPlayer.setMirror(mirror)
    }

    override fun setStartTime(pos: Float) {
        mTXVodPlayer.setStartTime(pos)
    }

    override fun setToken(token: String?) {
        mTXVodPlayer.setToken(token)
    }

    override fun setLoop(loop: Boolean) {
        mTXVodPlayer.isLoop = loop
    }

    override fun isLoop(): Boolean {
        return mTXVodPlayer.isLoop
    }

    override fun setStringOption(key: String?, value: Any?) {
        mTXVodPlayer.setStringOption(key, value)
    }

    override fun setObserver(livePlayerObserver: BasePlayerObserver?) {
        if (playerObserver != livePlayerObserver) {
            playerObserver = livePlayerObserver
            mTXVodPlayer.setVodListener(mITXVodPlayListener)
        }
    }

    override fun removeObserver() {
        playerObserver = null
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }
}