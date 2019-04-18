package com.zinc.paint.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.paint.widget.RadarView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/30
 * @description
 */
public class RadarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new RadarView(this));
    }
}
