package com.zinc.canvas.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zinc.canvas.R;
import com.zinc.canvas.widget.HeartView;

/**
 * author       : zinc
 * time         : 2019/4/21 上午9:29
 * desc         :
 * version      :
 */
public class HeartActivity extends Activity {

    private HeartView heartView;
    private TextView tvStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_heart);

        heartView = findViewById(R.id.heart_view);
        tvStart = findViewById(R.id.tv_start);

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartView.start();
            }
        });
    }
}
