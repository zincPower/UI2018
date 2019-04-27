package com.zinc.code8_canvas_clip.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zinc.code8_canvas_clip.R;

/**
 * author       : zinc
 * time         : 2019/4/26 下午12:53
 * desc         :
 * version      :
 */
public class ClientActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_clip_client);
    }

    public void onHeart(View view) {
        startActivity(new Intent(this, HeartActivity.class));
    }

    public void onClip(View view) {
        startActivity(new Intent(this, ClipActivity.class));
    }

    public void onClipOut(View view) {
        startActivity(new Intent(this, ClipOutActivity.class));
    }

    public void onClipPathWithOp(View view) {
        startActivity(new Intent(this, ClipOpListActivity.class));
    }
}
