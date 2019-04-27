package com.zinc.canvas.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画椭圆
 * version      :
 */
public class OvalView extends BaseDrawView {

    public OvalView(Context context) {
        super(context);
    }

    public OvalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OvalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor1);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawOval(mRectF, mPaint);

        mPaint.setColor(mColor2);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mRectF, mPaint);

    }
}
