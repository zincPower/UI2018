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
        findViewById(R.id.lsn_2).setOnClickListener(this);
        findViewById(R.id.lsn_3).setOnClickListener(this);
        findViewById(R.id.lsn_4).setOnClickListener(this);
        findViewById(R.id.lsn_5).setOnClickListener(this);
        findViewById(R.id.lsn_6).setOnClickListener(this);
        findViewById(R.id.lsn_7).setOnClickListener(this);
        findViewById(R.id.lsn_8).setOnClickListener(this);
        findViewById(R.id.lsn_12).setOnClickListener(this);
        findViewById(R.id.lsn_15).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lsn_2:
                startActivity(new Intent(this, com.zinc.class2_flowlayout.ClientActivity.class));
                break;
            case R.id.lsn_3:
                startActivity(new Intent(this, com.zinc.class3_paint.ClientActivity.class));
                break;
            case R.id.lsn_4:
                startActivity(new Intent(this, com.zinc.class4_xfermode.ClientActivity.class));
                break;
            case R.id.lsn_5:
                startActivity(new Intent(this, com.zinc.class5_canvas.ClientActivity.class));
                break;
            case R.id.lsn_6:
                startActivity(new Intent(this, com.zinc.class6_drawable_gravity.ClientActivity.class));
                break;
            case R.id.lsn_7:
                startActivity(new Intent(this, com.zinc.class7_bezier.activity.ClientActivity.class));
                break;
            case R.id.lsn_8:
                startActivity(new Intent(this, com.zinc.class8_pathmeasure.activity.ClientActivity.class));
                break;
            case R.id.lsn_12:
                startActivity(new Intent(this, com.zinc.class12_animation.activity.ClientActivity.class));
                break;
            case R.id.lsn_15:
                startActivity(new Intent(this, com.zinc.class15_velocitytracker_scroller.BarActivity.class));
                break;
        }
    }

}
