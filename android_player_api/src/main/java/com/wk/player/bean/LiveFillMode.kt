package com.wk.player.bean

/**
 * 画面填充模式
 * */
enum class LiveFillMode {
    /**
     * 图像铺满屏幕，超出显示视窗的视频部分将被裁剪，画面显示可能不完整
     * */
    LiveFillModeFill,
    /**
     * 图像长边填满屏幕，短边区域会被填充黑色，画面的内容完整
     * */
    LiveFillModeFit,
    LiveFillModeScaleFill;
}