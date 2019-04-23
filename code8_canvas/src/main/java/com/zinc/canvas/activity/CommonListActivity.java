package com.zinc.canvas.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zinc.canvas.R;

/**
 * author       : zinc
 * time         : 2019/4/22 下午9:59
 * desc         :
 * version      :
 */
public class CommonListActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_list);
    }

    public void onClip(View view) {
        goToAct(CommonOperatorActivity.CLIP);
    }

    public void onArc(View view) {
        goToAct(CommonOperatorActivity.ARC);
    }

    public void onCircle(View view) {
        goToAct(CommonOperatorActivity.CIRCLE);
    }

    public void onLine(View view) {
        goToAct(CommonOperatorActivity.LINE);
    }

    public void onOval(View view) {
        goToAct(CommonOperatorActivity.OVAL);
    }

    public void onPoint(View view) {
        goToAct(CommonOperatorActivity.OVAL);
    }

    public void onText(View view) {
        goToAct(CommonOperatorActivity.OVAL);
    }

    public void onRect(View view) {
        goToAct(CommonOperatorActivity.OVAL);
    }

    public void onColor(View view) {
        goToAct(CommonOperatorActivity.OVAL);
    }

    public void onPathText(View view) {
        goToAct(CommonOperatorActivity.OVAL);
    }

    private void goToAct(int type) {
        Intent intent = new Intent(this, CommonOperatorActivity.class);
        intent.putExtra(CommonOperatorActivity.TYPE, type);
        startActivity(intent);
    }
}
