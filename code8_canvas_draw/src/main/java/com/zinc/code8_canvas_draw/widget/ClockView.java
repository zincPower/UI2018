package com.zinc.code8_canvas_draw.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zinc.code8_canvas_draw.R;

/**
 * author       : zinc
 * time         : 2019/4/21 下午8:35
 * desc         : 时钟与指针
 * version      : 1.2.0
 */
public class ClockView extends View {

    private static final int DEGREE_COUNT = 12;

    private Paint mPaint;

    private int mPadding;
    private int mBorderWidth;
    private int mMainLineLength;
    private int mMainLineWidth;
    private int mSubLineLength;
    private int mSubLineWidth;
    private int mPointerRadius;
    private RectF mPointerRectF;

    private int mClockColor;
    private int mPointerColor;

    private Path mPointerPath;

    private int mCurAngle;
    private ObjectAnimator mAnim;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPadding = dpToPx(5);
        mBorderWidth = dpToPx(3);

        mMainLineLength = dpToPx(10) + mBorderWidth / 2;
        mMainLineWidth = dpToPx(5);

        mSubLineLength = dpToPx(5) + mBorderWidth / 2;
        mSubLineWidth = dpToPx(3);

        mPointerRadius = dpToPx(8);
        mPointerRectF = new RectF(-mPointerRadius,
                -mPointerRadius,
                mPointerRadius,
                mPointerRadius);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mClockColor = ContextCompat.getColor(context, R.color.canvas_red_color);
        mPointerColor = ContextCompat.getColor(context, R.color.canvas_orange_color);

        mPointerPath = new Path();

        mAnim = ObjectAnimator.ofInt(this, "angle", 0, 360);
        mAnim.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        int width = Math.min(w, h);

        width = width - mPadding - mBorderWidth / 2;

        canvas.translate(w / 2, h / 2);

        drawBorder(canvas, width);

        drawPointer(canvas, width);

    }

    private void drawPointer(Canvas canvas, int width) {
        canvas.save();
        canvas.rotate(mCurAngle);
        if (mPointerPath.isEmpty()) {
            mPointerPath.moveTo(mPointerRadius, 0);
            mPointerPath.addArc(mPointerRectF, 0, 180);
            mPointerPath.lineTo(0, -width / 4);
            mPointerPath.lineTo(mPointerRadius, 0);
            mPointerPath.close();
        }

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mPointerColor);
        canvas.drawPath(mPointerPath, mPaint);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPointerPath.reset();
    }

    private void drawBorder(Canvas canvas, int width) {
        canvas.save();

        mPaint.setColor(mClockColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        canvas.drawCircle(0, 0, width / 2, mPaint);

        mPaint.setColor(mClockColor);
        int angle = 360 / DEGREE_COUNT;
        for (int i = 0; i < DEGREE_COUNT; ++i) {
            if (i != 0) {
                canvas.rotate(angle);
            }

            if (i % 3 == 0) {
                mPaint.setStrokeWidth(mMainLineWidth);
                canvas.drawLine(0,
                        -width / 2,
                        0,
                        -(width / 2 - mMainLineLength),
                        mPaint);
            } else {
                mPaint.setStrokeWidth(mSubLineWidth);
                canvas.drawLine(0,
                        -width / 2,
                        0,
                        -(width / 2 - mSubLineLength),
                        mPaint);
            }
        }

        canvas.restore();
    }

    /**
     * 转换 dp 至 px
     *
     * @param dpValue dp值
     * @return px值
     */
    protected int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }

    public void start(long duration) {
        mAnim.setDuration(duration);
        mAnim.start();
    }

    public void setAngle(int angle) {
        mCurAngle = angle;
        postInvalidate();
    }
}
