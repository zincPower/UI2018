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
public class PointsView extends BaseDrawView {

    private float[] pts = new float[]{
            0, -400,
            200, -400,
            -300, 0
    };

    public PointsView(Context context) {
        super(context);
    }

    public PointsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PointsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor2);
        mPaint.setStrokeWidth(dpToPx(5));
        canvas.drawPoints(pts, mPaint);

    }
}
