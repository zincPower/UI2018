package com.zinc.paint;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.paint.activity.GradientHeartActivity;
import com.zinc.paint.activity.PaintActivity;
import com.zinc.paint.activity.RunningLinearGradientActivity;
import com.zinc.paint.activity.BitmapShaderActivity;
import com.zinc.paint.activity.TileModeActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/22
 * @description
 */
public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        findViewById(R.id.tile_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientActivity.this, TileModeActivity.class));
            }
        });

        findViewById(R.id.zoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientActivity.this, PaintActivity.class));
            }
        });

        findViewById(R.id.bitmap_control).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientActivity.this, BitmapShaderActivity.class));
            }
        });

        findViewById(R.id.gradient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientActivity.this, GradientHeartActivity.class));
            }
        });

        findViewById(R.id.running_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientActivity.this, RunningLinearGradientActivity.class));
            }
        });

    }
}
