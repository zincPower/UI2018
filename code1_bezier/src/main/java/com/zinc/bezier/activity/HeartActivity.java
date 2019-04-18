package com.zinc.bezier.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.bezier.R;
import com.zinc.bezier.widget.HeartView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/10
 * @description
 */
public class HeartActivity extends AppCompatActivity {

    private HeartView heartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_heart);

        heartView = findViewById(R.id.heart_view);

    }

    public void onStart(View view) {
        heartView.start();
    }
}
