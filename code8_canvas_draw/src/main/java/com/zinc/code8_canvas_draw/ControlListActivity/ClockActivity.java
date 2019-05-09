package com.zinc.code8_canvas_draw.ControlListActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zinc.code8_canvas_draw.R;
import com.zinc.code8_canvas_draw.widget.ClockView;

/**
 * author       : zinc
 * time         : 2019/4/21 上午9:29
 * desc         :
 * version      :
 */
public class ClockActivity extends Activity {

    private final static int DEFAULT_DURATION = 2000;
    private final static int DURATION_INTERVAL = 10;
    private final static int DURATION_MIN = 1000;

    private ClockView clockView;
    private TextView tvStart;

    private TextView tvDuration;
    private SeekBar sbDuration;

    private int mDuration = DEFAULT_DURATION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_clock);

        clockView = findViewById(R.id.clock_view);
        tvStart = findViewById(R.id.tv_start);

        tvDuration = findViewById(R.id.tv_duration);
        sbDuration = findViewById(R.id.sb_duration);

        tvDuration.setText(String.format(getString(R.string.canvas_duration), mDuration));
        sbDuration.setProgress((mDuration - DURATION_MIN) / DURATION_INTERVAL);

        sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDuration = progress * DURATION_INTERVAL + DURATION_MIN;
                tvDuration.setText(String.format(getString(R.string.canvas_duration), mDuration));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockView.start(mDuration);
            }
        });
    }
}
