package com.zinc.code8_canvas_clip.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zinc.code8_canvas_clip.widget.ClipOutPathView;

/**
 * author       : zinc
 * time         : 2019/4/27 上午10:10
 * desc         :
 * version      :
 */
public class ClipOutActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ClipOutPathView(this));
    }
}
