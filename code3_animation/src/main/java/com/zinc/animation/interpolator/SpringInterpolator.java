package com.zinc.animation.interpolator;

import android.animation.TimeInterpolator;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/27
 * @description 震旦效果
 */
public class SpringInterpolator implements TimeInterpolator {

    /**
     * 参数 x，即为 x轴的值
     * 返回值 便是 y 轴的值
     */
    @Override
    public float getInterpolation(float x) {
        float factor = 0.4f;
        return (float) (Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
