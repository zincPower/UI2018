package com.zinc.code8_canvas_text_paint.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.zinc.code8_canvas_text_paint.R;
import com.zinc.lib_base.BaseView;
import com.zinc.lib_base.UIUtils;

/**
 * author       : zinc
 * time         : 2019/5/15 下午11:07
 * desc         :
 * version      :
 */
public class WaveTextView extends BaseView {

    private static final int LENGTH_TIMES = 3;
    private static final int POINT_COUNT = 200;
    private static final float A = 200;

    private String mContent;

    private Paint mPaint;
    private Path mPath;

    private float mA = 0;

    private float mLength;
    private float mStepLength;
    private boolean mIsShowLine;

    private float m = 0;

    public WaveTextView(Context context) {
        super(context);
    }

    private float mLineWidth;
    private float mTextWidth;
    private int mLineColor;
    private int mTextColor;
    private int mBgColor;

    private float mTextSize;
    private ValueAnimator mAnimator;

    public WaveTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {

        mTextColor = ContextCompat.getColor(context, R.color.canvas_light_blue_color);
        mLineColor = ContextCompat.getColor(context, R.color.canvas_red_color);

        mBgColor = ContextCompat.getColor(context, R.color.canvas_pink_color);

        mTextWidth = UIUtils.dip2px(context, 1);
        mLineWidth = UIUtils.dip2px(context, 2);

        mTextSize = UIUtils.dip2px(context, 18);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setStrokeWidth(mTextWidth);

        mPath = new Path();

        mAnimator = ValueAnimator.ofFloat(0, (float) (2 * Math.PI));
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                m = progress;
                mA = (float) (1 - progress / (2 * Math.PI)) * A;
                invalidate();
            }
        });
        mAnimator.setDuration(1000);
    }

    public void setContent(String content) {
        this.mContent = content;

        this.mLength = mPaint.measureText(content) * LENGTH_TIMES;
        this.mStepLength = mLength / (POINT_COUNT);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(mBgColor);

        // 绘制网格线
        if (mIsShowLine) {
            drawCoordinate(canvas);
        }

        // 空内容就不绘制了
        if (TextUtils.isEmpty(mContent)) {
            return;
        }

        // 构建路径
        mPath.reset();
        for (int i = 0; i < POINT_COUNT; ++i) {

            float x = -mLength / LENGTH_TIMES + i * mStepLength;
            float y = calculateY(x);

            if (i == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }

        }

        // 移至中心
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 将字体放中间
        canvas.translate(mLength / LENGTH_TIMES / 2, 0);

        // 是否显示path
        if (mIsShowLine) {
            mPaint.setColor(mLineColor);
            mPaint.setStrokeWidth(mLineWidth);
            mPaint.setStyle(Paint.Style.STROKE);

            canvas.drawPath(mPath, mPaint);

            mPaint.setColor(mTextColor);
            mPaint.setStrokeWidth(mTextWidth);
            mPaint.setStyle(Paint.Style.FILL);
        }

        // 在路径上画文字
        canvas.drawTextOnPath(mContent, mPath, 0, 0, mPaint);

    }

    /**
     * 计算 y 轴值
     * <p>
     * 三角函数：A*sin(w*x+m)+k
     */
    private float calculateY(float x) {
        double a = Math.pow(4 / (4 + Math.pow(4 * x / mLength, 4)), 2.5f) * mA;
        return (float) (a * Math.sin(Math.PI * x / 200 - m));
    }

    /**
     * 是否显示提示线
     */
    public void setIsShowLine(boolean isShowLine) {
        this.mIsShowLine = isShowLine;
    }

    /**
     * 开始动画
     */
    public void start() {
        if (mAnimator == null) {
            return;
        }

        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mAnimator.start();
    }

}
