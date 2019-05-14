package com.zinc.code8_canvas_draw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zinc.code8_canvas_draw.widget.CanvasView;

/**
 * author       : zinc
 * time         : 2019/5/9 下午10:10
 * desc         :
 * version      :
 */
public class CanvasActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CanvasView(this));
    }
}
