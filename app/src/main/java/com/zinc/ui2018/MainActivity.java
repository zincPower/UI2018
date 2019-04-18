package com.zinc.ui2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_code1_bezier).setOnClickListener(this);
        findViewById(R.id.tv_code2_pathmeasure).setOnClickListener(this);
        findViewById(R.id.tv_code3_anim).setOnClickListener(this);
        findViewById(R.id.tv_code4_xfermode).setOnClickListener(this);
        findViewById(R.id.tv_code5_scroller_velocityTracker).setOnClickListener(this);
        findViewById(R.id.tv_code6_draw_flow).setOnClickListener(this);
        findViewById(R.id.tv_code7_svg).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code1_bezier:
                startActivity(new Intent(this, com.zinc.bezier.activity.ClientActivity.class));
                break;
            case R.id.tv_code2_pathmeasure:
                startActivity(new Intent(this, com.zinc.pathmeasure.activity.ClientActivity.class));
                break;
            case R.id.tv_code3_anim:
                startActivity(new Intent(this, com.zinc.animation.activity.ClientActivity.class));
                break;
            case R.id.tv_code4_xfermode:
                startActivity(new Intent(this, com.zinc.xfermode.ClientActivity.class));
                break;
            case R.id.tv_code5_scroller_velocityTracker:
                startActivity(new Intent(this, com.zinc.velocitytracker_scroller.BarActivity.class));
                break;
            case R.id.tv_code6_draw_flow:
                startActivity(new Intent(this, com.zinc.flowlayout.ClientActivity.class));
                break;
            case R.id.tv_code7_svg:
                startActivity(new Intent(this, com.zinc.svg.ClientActivity.class));
                break;
        }
    }

}
