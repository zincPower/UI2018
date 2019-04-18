package com.zinc.paint.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.paint.R;
import com.zinc.paint.widget.BitmapShaderView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/19
 * @description
 */
public class BitmapShaderActivity extends AppCompatActivity {

    BitmapShaderView shapeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_drawable);

        shapeView = findViewById(R.id.shape_view);

        findViewById(R.id.btn_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeView.setType(0);
            }
        });

        findViewById(R.id.btn_rect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeView.setType(1);
            }
        });

        findViewById(R.id.btn_radius).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shapeView.setType(2);
            }
        });

    }
}
