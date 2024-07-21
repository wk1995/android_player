package com.wk.player.api

import com.wk.player.PlayState
import com.wk.player.PlayType

abstract class BasePlayComponentDataBean {

    /**
     * 当前播放类型
     * */
    var playType: com.wk.player.PlayType = com.wk.player.PlayType.UN_KNOW

    /**
     * 当前播放状态
     * */
    var playState: com.wk.player.PlayState = com.wk.player.PlayState.PLAY_STATE_INIT
}