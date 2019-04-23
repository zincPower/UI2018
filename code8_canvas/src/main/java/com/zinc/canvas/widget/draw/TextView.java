package com.zinc.canvas.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画矩阵
 * version      :
 */
public class TextView extends BaseDrawView {

    private static final String CONTENT = "zinc 猛猛的小盆友";

    private int mTextSize;

    private float[] mPos;

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mTextSize = dpToPx(14);
        mPos = new float[]{
                0, dpToPx(200)
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setPaint(mColor1, mLineWidth);
        mPaint.setTextSize(mTextSize);

        canvas.drawText(CONTENT, 0, 0, mPaint);

        canvas.drawText(CONTENT, 3, CONTENT.length(),
                0, dpToPx(100), mPaint);

    }
}
