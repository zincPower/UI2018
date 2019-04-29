package com.zinc.code8_canvas_draw.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画矩阵
 * version      :
 */
public class RectView extends BaseDrawView {

    public RectView(Context context) {
        super(context);
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setPaint(mColor1, mLineWidth);
        canvas.drawRect(mRectF, mPaint);

        canvas.save();
        setPaint(mColor2, mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.translate(0, mRectF.height() + dpToPx(100));
        canvas.drawRoundRect(mRectF, 50, 150, mPaint);
        canvas.restore();

    }
}
