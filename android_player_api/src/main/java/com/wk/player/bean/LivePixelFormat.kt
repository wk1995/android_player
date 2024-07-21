package com.wk.player.bean

/**
 * 视频帧像素格式
 * */
enum class LivePixelFormat {
    /**
     * 未知
     * */
    LivePixelFormatUnknown,
    /**
     * YUV420P I420
     * */
    LivePixelFormatI420,

    /**
     * OpenGL 2D 纹理
     * */
    LivePixelFormatTexture2D
}