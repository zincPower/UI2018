package com.zinc.class21_svg;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zinc.class21_svg.widget.JueJinLogoView;

/**
 * author       : zinc
 * time         : 2019/4/5 下午4:35
 * desc         :
 * version      :
 */
public class JueJinAnimActivity extends Activity {

    private JueJinLogoView jueJinLogoView;
    private TextView tvPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_anim);

        jueJinLogoView = findViewById(R.id.jue_jin_logo_view);
        tvPlay = findViewById(R.id.tv_play);

        tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jueJinLogoView.start();
            }
        });
    }

}
