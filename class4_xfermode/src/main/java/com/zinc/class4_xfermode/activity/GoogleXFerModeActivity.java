package com.zinc.class4_xfermode.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.class4_xfermode.widget.GoogleXFerModeView;
import com.zinc.class4_xfermode.widget.ZincXFerModeView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/22
 * @description
 */
public class GoogleXFerModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GoogleXFerModeView(this));
    }
}
