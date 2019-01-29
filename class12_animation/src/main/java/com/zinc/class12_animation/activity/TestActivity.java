//package com.zinc.class12_animation.activity;
//
//import android.animation.FloatEvaluator;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.animation.LinearInterpolator;
//
//import com.zinc.class12_animation.interpolator.SpringInterpolator;
//
///**
// * @author Jiang zinc
// * @date 创建时间：2019/1/28
// * @description
// */
//public class TestActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        View view = new View(this);
//        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
//        rotationAnimator.setInterpolator(new SpringInterpolator());
//        rotationAnimator.setDuration(1_000);
//        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float) animation.getAnimatedValue();
//                Log.i("TestActivity", "onAnimationUpdate: " + value);
//            }
//        });
//        rotationAnimator.setEvaluator(new FloatEvaluator());
//        rotationAnimator.start();
//
//    }
//}
