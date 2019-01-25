package com.zinc.class12_animation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.class12_animation.R;
import com.zinc.class12_animation.widget.DialView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/19
 * @description
 */
public class DialActivity extends AppCompatActivity {

    private DialView dialView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial);

//        dialView = findViewById(R.id.dial_view);
        dialView.start();
    }
}
