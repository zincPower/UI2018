package com.zinc.code8_canvas_draw.ControlListActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.zinc.code8_canvas_draw.R;
import com.zinc.code8_canvas_draw.widget.control.MatrixView;
import com.zinc.code8_canvas_draw.widget.control.RotatePointView;
import com.zinc.code8_canvas_draw.widget.control.RotateView;
import com.zinc.code8_canvas_draw.widget.control.ScalePointView;
import com.zinc.code8_canvas_draw.widget.control.ScaleView;
import com.zinc.code8_canvas_draw.widget.control.SkewView;
import com.zinc.code8_canvas_draw.widget.control.TranslateView;

/**
 * author       : zinc
 * time         : 2019/5/7 下午11:27
 * desc         :
 * version      :
 */
public class ControlOperatorActivity extends Activity {

    public static final String TYPE = "TYPE";

    public static final int ROTATE = 1;
    public static final int ROTATE_POINT = 2;

    public static final int SCALE = 3;
    public static final int SCALE_POINT = 4;

    public static final int SKEW = 5;

    public static final int TRANSFORM = 6;

    public static final int MATRIX = 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_operator);

        FrameLayout frameLayout = findViewById(R.id.frame_layout);

        View view;
        int type = getIntent().getIntExtra(TYPE, ROTATE);
        switch (type) {

            case ROTATE:
                view = new RotateView(this);
                break;

            case ROTATE_POINT:
                view = new RotatePointView(this);
                break;

            case SCALE:
                view = new ScaleView(this);
                break;

            case SCALE_POINT:
                view = new ScalePointView(this);
                break;

            case SKEW:
                view = new SkewView(this);
                break;

            case TRANSFORM:
                view = new TranslateView(this);
                break;

            case MATRIX:
                view = new MatrixView(this);
                break;

            default:
                view = new RotateView(this);
                break;
        }

        frameLayout.addView(view);

    }

}
