package com.wk.player.bean

/**
 * 视频数据包装格式
 * */
enum class LiveBufferType {
    /**
     * 	未知
     * */
    LiveBufferTypeUnknown,

    /**
     * DirectBuffer，装载 I420 等 buffer，在 native 层使用
     * */
    LiveBufferTypeByteBuffer,

    /**
     * byte[]，装载 I420 等 buffer，在 Java 层使用
     * */
    LiveBufferTypeByteArray,

    /**
     * 直接操作纹理 ID，性能最好，画质损失最少
     * */
    LiveBufferTypeTexture;
}