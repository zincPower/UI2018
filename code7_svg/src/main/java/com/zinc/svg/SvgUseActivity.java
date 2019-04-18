package com.zinc.svg;

import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;

/**
 * author       : zinc
 * time         : 2019/4/5 下午4:35
 * desc         :
 * version      :
 */
public class SvgUseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ImageView img1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_use);

        img1 = findViewById(R.id.img1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((Animatable) img1.getDrawable()).start();
                } else {
                    img1.setImageDrawable(
                            ContextCompat.getDrawable(SvgUseActivity.this, R.drawable.ic_back));
                }
            }
        });

    }
}
