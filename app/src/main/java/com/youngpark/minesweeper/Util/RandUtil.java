package com.youngpark.minesweeper.Util;

import java.util.Random;

/**
 * youngpark 2020.02.04
 *
 * 扫雷 v0.1
 *
 * 随机数
 */

public class RandUtil {

    public static Random random = new Random(System.currentTimeMillis());

    public static int getBoom(int max){
        return random.nextInt(max);
    }

}
