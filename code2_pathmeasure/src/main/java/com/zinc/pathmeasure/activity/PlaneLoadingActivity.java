package com.zinc.pathmeasure.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.pathmeasure.PlaneLoadingView;
import com.zinc.pathmeasure.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/5
 * @description
 */
public class PlaneLoadingActivity extends AppCompatActivity {

    PlaneLoadingView mPlaneLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_loading);

        mPlaneLoadingView = findViewById(R.id.plane_loading_view);
    }

    public void start(View view) {
        mPlaneLoadingView.startLoading();
    }

    public void stop(View view) {
        mPlaneLoadingView.stopLoading();
    }

}
