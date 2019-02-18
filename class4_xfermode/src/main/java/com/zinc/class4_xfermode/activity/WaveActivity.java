package com.zinc.class4_xfermode.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.class4_xfermode.R;
import com.zinc.class4_xfermode.widget.WaveView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/18
 * @description
 */
public class WaveActivity extends AppCompatActivity {

    private WaveView waveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new IrregularWaveView_DSTIN(this));
        setContentView(R.layout.lsn4_activity_wave);

        waveView = findViewById(R.id.wave_view);
        waveView.start();
    }
}
