package com.diggoods.api.only_one;

/**
 * Create by  FengJianyi on 2019/6/25
 */
import java.util.Random;

/**
 * 随机生成器
 * <p/>
 * Created by w_yong on 18/12/4.
 */
public class SnowRandomGenerator {
    private static final Random RANDOM = new Random();

    // 区间随机
    public float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    // 上界随机
    public float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;
    }

    // 上界随机
    public int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }
}