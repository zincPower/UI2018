package com.zinc.canvas.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画线
 * version      :
 */
public class LineView extends BaseDrawView {

    private float[] pts = new float[]{
            0, -400, 200, -400,
            -300, 0, -300, 300,
            0, 400, 300, 400
    };

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        mPaint.setColor(mColor1);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeWidth(mLineWidth);
//        canvas.drawLine(-200, -200,
//                0, 0, mPaint);
//
        mPaint.setColor(mColor2);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawLines(pts, mPaint);

        mPaint.setColor(mColor1);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawLines(pts, 2, 8, mPaint);
    }
}
