package com.wk.player

/**
 * 视频播放状态
 * */



const val PLAY_STATE_CONSTANT_INIT = "INIT"

/**
 * 拉流失败
 * */
const val PLAY_STATE_CONSTANT_ERROR = "ERROR"

/**
 * 未开始
 * */
const val PLAY_STATE_CONSTANT_CREATE = "CREATE"

/**
 * 直播中
 * */
const val PLAY_STATE_CONSTANT_START = "START"

/**
 * 管理暂停
 * */
const val PLAY_STATE_CONSTANT_SUSPEND = "SUSPEND"

/**
 * 直播结束
 * */
const val PLAY_STATE_CONSTANT_STOP = "STOP"

/**
 * 回放
 * */
const val PLAY_STATE_CONSTANT_PLAYBACK = "PLAYBACK"

/**
 * 手动暂停
 * */
const val PLAY_STATE_CONSTANT_PAUSED = "PAUSED"

const val PLAY_STATE_CONSTANT_RESTORE = "RESTORE"


enum class PlayState(val stateName: String) {

    PLAY_STATE_INIT(PLAY_STATE_CONSTANT_INIT),

    /**
     * 流播放出错
     * */
    PLAY_STATE_ERROR(PLAY_STATE_CONSTANT_ERROR),

    /**
     * 没到播放时间
     * */
    PLAY_STATE_PREVIEW(PLAY_STATE_CONSTANT_CREATE),

    /**
     * 正在播放
     * */
    PLAY_STATE_PLAYING(PLAY_STATE_CONSTANT_START),

    /**
     * 流是播放中，但内容暂停，管理员操作
     * */
    PLAY_STATE_PLAYING_PAUSED(PLAY_STATE_CONSTANT_SUSPEND),


    /**
     * 流结束
     * */
    PLAY_STATE_END(PLAY_STATE_CONSTANT_STOP),

    /**
     * 回放
     * */
    PLAY_STATE_PLAYBACK(PLAY_STATE_CONSTANT_PLAYBACK),


    /**
     * 恢复，管理员操作
     * */
    PLAY_STATE_PLAYING_RESTORE(PLAY_STATE_CONSTANT_RESTORE),

    /**
     * 流暂停播放,人工暂停
     * */
    PLAY_STATE_PAUSED(PLAY_STATE_CONSTANT_PAUSED);


    private val playStateMap by lazy {
        val map = HashMap<String, PlayState>()
        PlayState.values().forEach {
            map[it.stateName] = it
        }
        map
    }

    fun getPlayState(stateName: String) = playStateMap[stateName.uppercase()] ?: this


    fun isPlaying(): Boolean {
        return this == PlayState.PLAY_STATE_PLAYING || this == PlayState.PLAY_STATE_PLAYBACK
    }
}

/**
 * 全局播放器 状态
 * */
enum class GlobalPlayState {
    GLOBAL_PLAY_STATE_SHOW,
    GLOBAL_PLAY_STATE_DISMISS;

    fun canShow(): Boolean = this != GlobalPlayState.GLOBAL_PLAY_STATE_DISMISS
}

/**
 * 播放类型
 * */
enum class PlayType {
    /**
     * 直播
     * */
    LIVE,

    /**
     * 点播/播放视频
     * */
    PLAY,

    UN_KNOW;

    /**
     * 默认情况是不显示直播的UI，所以[UN_KNOW]的UI界面也同[LIVE]
     * */
    fun isPlay() = this == PlayType.PLAY
}