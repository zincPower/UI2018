package com.zinc.class7_bezier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/30
 * @description
 */
public class BezierActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivSetting;
    private ImageView ivRestart;
    private ImageView ivPause;
    private ImageView ivPlay;
    private BezierView bezierView;

    BezierDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);

        ivSetting = findViewById(R.id.iv_setting);
        ivRestart = findViewById(R.id.iv_restart);
        ivPause = findViewById(R.id.iv_pause);
        ivPlay = findViewById(R.id.iv_play);
        bezierView = findViewById(R.id.bezier_view);

        dialog = BezierDialog.getInstance();

        ivSetting.setOnClickListener(this);
        ivRestart.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        ivPlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_setting) {
            dialog.show(getSupportFragmentManager(), "BezierActivity");
        } else if (i == R.id.iv_play) {
            bezierView.start();
        } else if (i == R.id.iv_pause) {

        } else if (i == R.id.iv_restart) {

        }
    }
}
