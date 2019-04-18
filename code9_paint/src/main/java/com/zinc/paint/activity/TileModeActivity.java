package com.zinc.paint.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.paint.R;
import com.zinc.paint.widget.TileModeView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/18
 * @description
 */
public class TileModeActivity extends AppCompatActivity implements View.OnClickListener {

    private TileModeView tileMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_mode);

        findViewById(R.id.btn_clamp).setOnClickListener(this);
        findViewById(R.id.btn_mirror).setOnClickListener(this);
        findViewById(R.id.btn_repeat).setOnClickListener(this);
        tileMode = findViewById(R.id.tile_mode);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_clamp) {
            tileMode.setMode(0);
        } else if (i == R.id.btn_mirror) {
            tileMode.setMode(1);
        } else if (i == R.id.btn_repeat) {
            tileMode.setMode(2);
        }
    }
}
