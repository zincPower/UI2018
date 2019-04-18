package com.zinc.bezier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.bezier.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/9
 * @description
 */
public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_client);
    }

    public void drawCircle(View view) {
        startActivity(new Intent(this, CircleActivity.class));
    }

    public void showBezierPlay(View view) {
        startActivity(new Intent(this, BezierActivity.class));
    }

    public void diy(View view) {
        startActivity(new Intent(this, DIYBezierActivity.class));
    }

    public void stickPoint(View view) {
        startActivity(new Intent(this, StickDotActivity.class));
    }

    public void changeToHeart(View view) {
        startActivity(new Intent(this, HeartActivity.class));
    }
}
