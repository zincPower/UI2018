package com.zinc.class8_pathmeasure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/5
 * @description
 */
public class ClientActivity extends AppCompatActivity {

    PlaneLoadingView mPlaneLoadingView;

    BoatWaveView mBoatWaveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_wave_view);
//        setContentView(new ABCView(this));
        setContentView(new GetSegmentView(this));

//        mPlaneLoadingView = findViewById(R.id.plane_loading_view);
//        mBoatWaveView = findViewById(R.id.boat_wave_view);
    }

    public static void main(String[] args) {

        System.out.println(Math.atan2(45, 30));

    }

    public void start(View view) {
        mBoatWaveView.startAnim();
    }

    public void stop(View view) {
        mBoatWaveView.stopAnim();
    }

}
