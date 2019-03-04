package com.zinc.class3_paint.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.class3_paint.R;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/16
 * @description
 */
public class PaintActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
    }
}
