package com.wk.player

import com.wk.player.api.IPageController
import com.wk.player.api.IPlayController


/**
 * 中转
 *
 * 组件与播放控制器桥梁
 *
 * */
class ControlWrapper(
    var pageController: IPageController?,
    private val playController: IPlayController
) : IPageController, IPlayController {

    override fun back() {
        pageController?.back()?:playController.destroy(false)

    }

    override fun closeLive() {
        pageController?.closeLive()?: playController.destroy(true)

    }

    override fun shareLive() {
        pageController?.shareLive()
    }

    override fun pausePlay() {
        playController.pausePlay()
    }

    override fun resumePlay() {
        playController.resumePlay()
    }

    override fun showSpeechChoices() {
        pageController?.showSpeechChoices()
    }

    override fun rewindPlay() {
        playController.rewindPlay()
    }

    override fun forwardPlay() {
        playController.forwardPlay()
    }

    override fun toggleFullScreenState() {
        pageController?.toggleFullScreenState()
    }

    override fun togglePlayState() {
        playController.togglePlayState()
    }

    override fun seek(process: Int) {
        playController.seek(process)
    }

    override fun destroy(reallyDestroy: Boolean) {

    }

    override fun reservation() {
        pageController?.reservation()
    }

    override fun showJoinGroupFail() {
        pageController?.showJoinGroupFail()
    }
}