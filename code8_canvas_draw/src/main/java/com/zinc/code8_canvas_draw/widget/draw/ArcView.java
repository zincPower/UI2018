package com.zinc.code8_canvas_draw.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画圆弧
 * version      : 1.2.0
 */
public class ArcView extends BaseDrawView {

    public ArcView(Context context) {
        super(context);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.save();
            canvas.translate(0, -mRectF.height() * 3 / 2);

            mPaint.setStrokeWidth(mLineWidth);
            // STROKE 只画线不填充，FILL 会将区域填充
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mColor1);
            // 第四个参数，设置为false，不连接中心
            canvas.drawArc(-150, -150, 400, 150,
                    0, 120, false, mPaint);

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mColor2);
            mPaint.setStrokeWidth(mBorderWidth);
            canvas.drawRect(-150, -150, 400, 150, mPaint);

            canvas.restore();
        }

        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor1);
        // 第四个参数，设置为true，连接中心
        canvas.drawArc(mRectF, 0, 120,
                true, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor2);
        mPaint.setStrokeWidth(mBorderWidth);
        canvas.drawRect(mRectF, mPaint);
    }
}
