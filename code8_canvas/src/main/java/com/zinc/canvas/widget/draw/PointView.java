package com.zinc.canvas.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画圆弧
 * version      :
 */
public class PointView extends BaseDrawView {

    private float[] pts = new float[]{
            0, -400, 200, -400,
            -300, 0, -300, 300,
            0, 400, 300, 400
    };

    public PointView(Context context) {
        super(context);
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor1);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawPoint(0, 0, mPaint);

        mPaint.setColor(mColor2);
        mPaint.setStrokeWidth(mLineWidth);

        mPaint.setColor(mColor1);
    }
}
