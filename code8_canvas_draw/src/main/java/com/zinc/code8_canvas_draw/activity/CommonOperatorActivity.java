package com.zinc.code8_canvas_draw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.zinc.code8_canvas_draw.R;
import com.zinc.code8_canvas_draw.widget.draw.ArcView;
import com.zinc.code8_canvas_draw.widget.draw.BackgroundView;
import com.zinc.code8_canvas_draw.widget.draw.CircleView;
import com.zinc.code8_canvas_draw.widget.draw.LineView;
import com.zinc.code8_canvas_draw.widget.draw.OvalView;
import com.zinc.code8_canvas_draw.widget.draw.PointView;
import com.zinc.code8_canvas_draw.widget.draw.RectView;

/**
 * author       : zinc
 * time         : 2019/4/22 下午9:58
 * desc         : 普通操作
 * version      :
 */
public class CommonOperatorActivity extends Activity {

    public static final String TYPE = "TYPE";

    public static final int ARC = 2;
    public static final int CIRCLE = 3;
    public static final int LINE = 4;
    public static final int OVAL = 5;
    public static final int POINT = 6;
    public static final int TEXT = 7;
    public static final int RECT = 8;
    public static final int COLOR = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_operator);

        FrameLayout frameLayout = findViewById(R.id.frame_layout);

        View view;
        int type = getIntent().getIntExtra(TYPE, ARC);
        switch (type) {
            case ARC:
                view = new ArcView(this);
                break;
            case CIRCLE:
                view = new CircleView(this);
                break;
            case LINE:
                view = new LineView(this);
                break;
            case OVAL:
                view = new OvalView(this);
                break;
            case POINT:
                view = new PointView(this);
                break;
            case RECT:
                view = new RectView(this);
                break;
            case COLOR:
                view = new BackgroundView(this);
                break;
            default:
                view = new ArcView(this);
                break;
        }

        frameLayout.addView(view);

    }
}
