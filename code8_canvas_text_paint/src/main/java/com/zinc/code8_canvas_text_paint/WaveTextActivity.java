package com.zinc.code8_canvas_text_paint;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.zinc.code8_canvas_text_paint.widget.WaveTextView;

/**
 * author       : zinc
 * time         : 2019/5/21 上午9:18
 * desc         :
 * version      :
 */
public class WaveTextActivity extends Activity {

    private WaveTextView waveTextView;
    private EditText etContent;
    private Switch swHelperLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_text);

        waveTextView = findViewById(R.id.wave_text);
        etContent = findViewById(R.id.et_content);
        swHelperLine = findViewById(R.id.sw_helper_line);

        swHelperLine.setChecked(true);
        waveTextView.setIsShowLine(true);
        swHelperLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                waveTextView.setIsShowLine(isChecked);
            }
        });

    }

    public void onRun(View view) {

        waveTextView.setContent(etContent.getText().toString().trim());

        waveTextView.start();

    }
}
