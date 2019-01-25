package com.zinc.class12_animation.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/14
 * @description 属性动画基本使用
 */
public class SimpleAnimationActivity extends Activity {

    private static final String TAG = "SimpleAnimationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ZincView view = new ZincView(this);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "zinc", 0f, 1f, 5f);
        objectAnimator.setDuration(2000);
        // 插值器
        objectAnimator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
//        // 估值器
//        objectAnimator.setEvaluator(new TypeEvaluator<PointF>() {
//
//            PointF pointF = new PointF();
//
//            @Override
//            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
//                Log.i(TAG, "evaluate: " + fraction);
//                pointF.x = fraction;
//                pointF.y = fraction;
//                return pointF;
//            }
//        });
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedValue().getClass());
            }
        });
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

        objectAnimator.start();

    }

    public static class ZincView extends View {

        public ZincView(Context context) {
            super(context);
        }

        public void setZinc(float value) {
            Log.i(TAG, "setZinc: " + value);
        }

//        public void setZinc(PointF value) {
//            Log.i(TAG, "setZinc: [" + value.x + ", " + value.y + "]");
//        }
    }

}
