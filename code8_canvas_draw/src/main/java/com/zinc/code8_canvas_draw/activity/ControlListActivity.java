package com.zinc.code8_canvas_draw.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zinc.code8_canvas_draw.R;

/**
 * author       : zinc
 * time         : 2019/5/7 下午11:27
 * desc         :
 * version      :
 */
public class ControlListActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_list);
    }

    public void onRotate(View view) {
        goToAct(ControlOperatorActivity.ROTATE);
    }

    public void onRotatePoint(View view) {
        goToAct(ControlOperatorActivity.ROTATE_POINT);
    }

    public void onScale(View view) {
        goToAct(ControlOperatorActivity.SCALE);
    }

    public void onScalePoint(View view) {
        goToAct(ControlOperatorActivity.SCALE_POINT);
    }

    public void onSkew(View view) {
        goToAct(ControlOperatorActivity.SKEW);
    }

    public void onTranslate(View view) {
        goToAct(ControlOperatorActivity.TRANSFORM);
    }

    public void onMatrix(View view) {
        goToAct(ControlOperatorActivity.MATRIX);
    }

    private void goToAct(int type) {
        Intent intent = new Intent(this, ControlOperatorActivity.class);
        intent.putExtra(ControlOperatorActivity.TYPE, type);
        startActivity(intent);
    }

}
