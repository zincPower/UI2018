package com.zinc.code8_canvas_text_paint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * author       : zinc
 * time         : 2019/4/28 下午10:58
 * desc         :
 * version      :
 */
public class ClientActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_text_paint_client);
    }

    public void onWaveText(View view) {
        startActivity(new Intent(this, WaveTextActivity.class));
    }

    public void onAPI(View view) {
        startActivity(new Intent(this, TextListActivity.class));
    }
}
