package com.zinc.code8_canvas_draw.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.lib_base.BaseView;

/**
 * author       : zinc
 * time         : 2019/5/4 上午11:41
 * desc         :
 * version      : 1.2.0
 */
public class LinesView extends BaseDrawView {

    private float[] pts = new float[]{
            0, -400, 200, -400,
            -300, 0, -300, 300,
            0, 400, 300, 400
    };


    public LinesView(Context context) {
        super(context);
    }

    public LinesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawLines(pts, mPaint);
    }

}
