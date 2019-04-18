package com.zinc.animation.activity;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/14
 * @description 属性动画源码分析
 */
public class SimpleAnimationActivity extends Activity {

    private static final String TAG = "SimpleAnimationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ZincView view = new ZincView(this);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "zinc", 0f, 2f, 5f);
        // 时长
        objectAnimator.setDuration(5000);
        // 插值器
        objectAnimator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        // 估值器
        objectAnimator.setEvaluator(new FloatEvaluator());
        // 更新回调
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedValue().getClass());
                Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedValue());
                Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedFraction());
            }
        });
        // 生命周期回调
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "onAnimationEnd: ");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i(TAG, "onAnimationCancel: ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i(TAG, "onAnimationRepeat: ");
            }
        });
        // 开启
        objectAnimator.start();

    }

    public static class ZincView extends View {

        public ZincView(Context context) {
            super(context);
        }

        public void setZinc(float value) {
            Log.i(TAG, "setZinc: " + value);
        }

    }

}
