package com.zinc.code8_canvas_draw.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画点
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

    protected int mPointWidth1;
    protected int mPointWidth2;

    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mPointWidth1 = dpToPx(5f);
        mPointWidth2 = dpToPx(4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setPaint(mColor1, mPointWidth1);
        canvas.drawPoint(0, 0, mPaint);

        setPaint(mColor2, mPointWidth1);
        canvas.drawPoints(pts, mPaint);

//        setPaint(mColor1, mPointWidth2);
//        canvas.drawPoints(pts, 2, pts.length - 3, mPaint);
    }
}
