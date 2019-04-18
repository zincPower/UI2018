package com.zinc.pathmeasure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.pathmeasure.BoatWaveView;
import com.zinc.pathmeasure.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/4
 * @description 乘风破浪的小船
 */
public class BoatLoadingActivity extends AppCompatActivity {

    BoatWaveView mBoatWaveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_wave_view);
        mBoatWaveView = findViewById(R.id.boat_wave_view);
    }

    public void start(View view) {
        mBoatWaveView.startAnim();
    }

    public void stop(View view) {
        mBoatWaveView.stopAnim();
    }

}
