package com.zinc.class12_animation.widget;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/19
 * @description 表盘视图
 */
public class DialView extends View {

    // 开始的偏移角度
    private static final int OFFSET_ANGEL = 90;

    // 表盘的颜色
    private static final String DEFAULT_DIAL_LINE_COLOR = "#3A5DFE";
    // 表盘的指针颜色
    private static final String DEFAULT_POINTER_COLOR = "#FE3171";
    // 默认画笔宽度
    private static final float LINE_WIDTH = dpToPx(10f);
    // 线的间隔，每根线相隔 10 度
    private static final float LINE_INTERVAL = 10;
    // 线的条数
    private static final int DEFAULT_LINE_COUNT = 5;
    // 动画时长
    private static final int DURATION = 2500;

    // 表盘颜色
    private int mDialLineColor;
    // 指针颜色
    private int mPointerColor;

    // 宽
    private float mWidth;

    // 线的个数
    private int mLineCount;

    // 每个项的角度
    private float mEachAngle;

    // 宽度的比例
    private float mPointerCircleWidthRatio = 6;
    // 指针长度比例
    private float mPointerLengthRatio = 4;

    private float mPointerCircleRadius;
    private float mPointerLength;

    private Path mLinePath;
    private Path mPointerPath;

    private Paint mLinePaint;
    private Paint mPointerPaint;

    private RectF mRectF;

    private ObjectAnimator mAnimator;

    // 动画的值
    private float mAnimValue;

    // 当前的项
    private int mValue = 4;

    // 当前旋转角度
    private float mRotateAngle;

    public DialView(Context context) {
        this(context, null, 0);
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {

        setDialLineColor(Color.parseColor(DEFAULT_DIAL_LINE_COLOR));
        setPointerColor(Color.parseColor(DEFAULT_POINTER_COLOR));

        mLinePath = new Path();
        mPointerPath = new Path();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(LINE_WIDTH);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mPointerPaint = new Paint();
        mPointerPaint.setAntiAlias(true);
        mPointerPaint.setStyle(Paint.Style.FILL);

        mRectF = new RectF();
        setLineCount(DEFAULT_LINE_COUNT);

        mRotateAngle = 180 + mEachAngle / 2 + LINE_INTERVAL;

    }

    /**
     * 设置 线的颜色
     *
     * @param dialLineColor 线的颜色
     */
    public void setDialLineColor(int dialLineColor) {
        this.mDialLineColor = dialLineColor;
    }

    /**
     * 设置 指针的颜色
     *
     * @param pointerColor 指针的颜色
     */
    public void setPointerColor(int pointerColor) {
        this.mPointerColor = pointerColor;
    }

    /**
     * 设置线条的数量
     *
     * @param lineCount 线数量
     */
    public void setLineCount(int lineCount) {
        this.mLineCount = lineCount;
        mEachAngle = (360 - LINE_INTERVAL * mLineCount) / mLineCount;
        createLinePath();
    }

    public void start() {
        mAnimator = ObjectAnimator.ofFloat(this,
                "curPointAngle",
                180 + mEachAngle / 2 + LINE_INTERVAL,
                mValue * (mEachAngle + LINE_INTERVAL) + 180);
        mAnimator.setDuration(DURATION);
        mAnimator.setEvaluator(new TypeEvaluator() {
            @Override
            public Float evaluate(float fraction, Object startValue, Object endValue) {

                float x = fraction;
                float factor = 0.45f;
                mAnimValue = (float) (Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1);

                float value = (float) endValue - (float) startValue;


                Log.i("mAnimator", "evaluate: " + mAnimValue);

                return (float) startValue + mAnimValue * value;
            }
        });
        mAnimator.start();
    }


    public void setCurPointAngle(float angle) {
        this.mRotateAngle = angle;
        Log.i("dial", "setCurPointAngle: " + mRotateAngle);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = Math.min(w, h);

        mPointerCircleRadius = mWidth / mPointerCircleWidthRatio / 2;
        mPointerLength = mWidth / mPointerLengthRatio;

        mRectF.top = LINE_WIDTH / 2;
        mRectF.right = mWidth - LINE_WIDTH / 2;
        mRectF.bottom = mWidth - LINE_WIDTH / 2;
        mRectF.left = LINE_WIDTH / 2;

        createLinePath();
        createPointerPath();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawDialLine(canvas);

        drawPointer(canvas);

    }

    /**
     * 画指针
     *
     * @param canvas 画布
     */
    private void drawPointer(Canvas canvas) {
        canvas.save();

        canvas.translate((mWidth + LINE_WIDTH) / 2, (mWidth + LINE_WIDTH) / 2);

        canvas.rotate(mRotateAngle);

        mPointerPaint.setColor(mPointerColor);
        canvas.drawPath(mPointerPath, mPointerPaint);

        canvas.restore();
    }

    /**
     * 画表盘线
     *
     * @param canvas 画布
     */
    private void drawDialLine(Canvas canvas) {
        mLinePaint.setColor(mDialLineColor);
        canvas.drawPath(mLinePath, mLinePaint);
    }

    private void createLinePath() {

        for (int i = 0; i < mLineCount - 1; ++i) {
            mLinePath.addArc(mRectF,
                    getRealOffsetAngle() + (i + 1) * LINE_INTERVAL + i * mEachAngle,
                    mEachAngle);
        }

    }

    private void createPointerPath() {
        RectF rectF = new RectF(
                -mPointerCircleRadius,
                -mPointerCircleRadius,
                mPointerCircleRadius,
                mPointerCircleRadius
        );

        mPointerPath.moveTo(mPointerCircleRadius, 0);

        mPointerPath.addArc(rectF, 0, 180);

        mPointerPath.lineTo(0, -mPointerLength);
        mPointerPath.lineTo(mPointerCircleRadius, 0
        );
        mPointerPath.close();
    }

    private float getRealOffsetAngle() {
        return OFFSET_ANGEL + mEachAngle / 2;
    }

    /**
     * 转换 sp 至 px
     *
     * @param spValue sp值
     * @return px值
     */
    protected static int spToPx(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 转换 dp 至 px
     *
     * @param dpValue dp值
     * @return px值
     */
    protected static int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }

}
