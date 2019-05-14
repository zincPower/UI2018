package com.zinc.code8_canvas_draw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.code8_canvas_draw.R;

/**
 * author       : Jiang zinc
 * time         : 2018/10/29
 * email        : 56002982@qq.com
 * desc         :
 * version      : 1.0.0
 */

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_client);
    }

    public void onClock(View view) {
        startActivity(new Intent(this, ClockActivity.class));
    }

    public void onControl(View view) {
        startActivity(new Intent(this, ControlListActivity.class));
    }

    public void onCanvas(View view) {
        startActivity(new Intent(this, CanvasActivity.class));
    }

    public void onDraw(View view) {
        startActivity(new Intent(this, DrawListActivity.class));
    }
}
