package com.zinc.code8_canvas_text_paint;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.zinc.code8_canvas_text_paint.text.TextOnPathView;
import com.zinc.code8_canvas_text_paint.text.TextPosText;
import com.zinc.code8_canvas_text_paint.text.TextRunView;
import com.zinc.code8_canvas_text_paint.text.TextView;

/**
 * author       : zinc
 * time         : 2019/4/22 下午9:58
 * desc         : 普通操作
 * version      :
 */
public class CommonOperatorActivity extends Activity {

    public static final String TYPE = "TYPE";

    public static final int TEXT = 1;
    public static final int TEXT_PATH = 2;
    public static final int TEXT_RUN = 3;
    public static final int TEXT_POS = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_operator);

        FrameLayout frameLayout = findViewById(R.id.frame_layout);

        View view;
        int type = getIntent().getIntExtra(TYPE, TEXT);
        switch (type) {
            case TEXT:
                view = new TextView(this);
                break;
            case TEXT_PATH:
                view = new TextOnPathView(this);
                break;
            case TEXT_RUN:
                view = new TextRunView(this);
                break;
            case TEXT_POS:
                view = new TextPosText(this);
                break;
            default:
                view = new TextView(this);
                break;
        }

        frameLayout.addView(view);

    }
}
