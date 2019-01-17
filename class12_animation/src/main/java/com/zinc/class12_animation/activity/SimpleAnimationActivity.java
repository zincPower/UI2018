package com.zinc.class12_animation.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/14
 * @description 属性动画基本使用
 */
public class SimpleAnimationActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Object object = new Object();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(object, "zinc", 0f, 1f);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();

    }
}
