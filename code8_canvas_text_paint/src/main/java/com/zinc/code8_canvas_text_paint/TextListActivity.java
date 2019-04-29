package com.zinc.code8_canvas_text_paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * author       : zinc
 * time         : 2019/4/24 下午11:32
 * desc         :
 * version      :
 */
public class TextListActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_list);
    }

    public void onText(View view) {
        goToAct(CommonOperatorActivity.TEXT);
    }

    public void onTextPath(View view) {
        goToAct(CommonOperatorActivity.TEXT_PATH);
    }

    public void onTextRun(View view) {
        goToAct(CommonOperatorActivity.TEXT_RUN);
    }

    public void onTextPos(View view) {
        goToAct(CommonOperatorActivity.TEXT_POS);
    }

    private void goToAct(int type) {
        Intent intent = new Intent(this, CommonOperatorActivity.class);
        intent.putExtra(CommonOperatorActivity.TYPE, type);
        startActivity(intent);
    }

}
