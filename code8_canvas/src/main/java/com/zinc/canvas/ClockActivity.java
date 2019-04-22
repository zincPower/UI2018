package com.zinc.canvas;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zinc.canvas.widget.ClockView;

/**
 * author       : zinc
 * time         : 2019/4/21 上午9:29
 * desc         :
 * version      :
 */
public class ClockActivity extends Activity {

    private ClockView clockView;
    private TextView tvStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_clock);

        clockView = findViewById(R.id.clock_view);
        tvStart = findViewById(R.id.tv_start);

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockView.start(2000);
            }
        });
    }
}
