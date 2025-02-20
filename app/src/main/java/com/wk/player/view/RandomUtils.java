package com.wk.player.view;

import java.util.Random;

public class RandomUtils {

    /**
     * 获取范围内的数据
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static Integer getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

}
