package com.zinc.code8_canvas_draw.ControlListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zinc.code8_canvas_draw.R;

/**
 * author       : zinc
 * time         : 2019/4/22 下午9:59
 * desc         :
 * version      :
 */
public class DrawListActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_list);
    }

    public void onArc(View view) {
        goToAct(DrawOperatorActivity.ARC);
    }

    public void onCircle(View view) {
        goToAct(DrawOperatorActivity.CIRCLE);
    }

    public void onLine(View view) {
        goToAct(DrawOperatorActivity.LINE);
    }

    public void onOval(View view) {
        goToAct(DrawOperatorActivity.OVAL);
    }

    public void onRect(View view) {
        goToAct(DrawOperatorActivity.RECT);
    }

    public void onColorXfermode(View view) {
        goToAct(DrawOperatorActivity.COLOR_X_FERMODE);
    }

    public void onLines(View view) {
        goToAct(DrawOperatorActivity.LINES);
    }

    public void onLinesOffset(View view) {
        goToAct(DrawOperatorActivity.LINES_OFFSET);
    }

    public void onPoint(View view) {
        goToAct(DrawOperatorActivity.POINT);
    }

    public void onPoints(View view) {
        goToAct(DrawOperatorActivity.POINTS);
    }

    public void onPointsOffset(View view) {
        goToAct(DrawOperatorActivity.POINTS_OFFSET);
    }

    public void onPath(View view) {
        goToAct(DrawOperatorActivity.PATH);
    }

    private void goToAct(int type) {
        Intent intent = new Intent(this, DrawOperatorActivity.class);
        intent.putExtra(DrawOperatorActivity.TYPE, type);
        startActivity(intent);
    }

}
