package com.zinc.code8_canvas_clip.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zinc.code8_canvas_clip.widget.ClipOpView;

/**
 * author       : zinc
 * time         : 2019/4/26 下午12:35
 * desc         :
 * version      :
 */
public class ClipOpContentActivity extends Activity {

    public static final String CLIP = "CLIP";

    private ClipOpView mClipOpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClipOpView = new ClipOpView(this);
        setContentView(mClipOpView);

        String clip = getIntent().getStringExtra(CLIP);
        mClipOpView.setType(clip);

    }
}
