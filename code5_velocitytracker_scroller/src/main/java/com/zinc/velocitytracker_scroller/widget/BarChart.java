package com.zinc.velocitytracker_scroller.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * author : zinc
 * email : 56002982@qq.com
 * time : 2019/3/1 下午5:04
 * desc : 带滑动效果的柱形图
 * version :
 */
public class BarChart extends View {

    private static final String TAG = "BarChart";
    /**
     * 内圆的颜色
     */
    private static final String INNER_DOT_COLOR = "#99E35B5B";
    /**
     * 外圆的颜色
     */
    private static final String OUTER_DOT_COLOR = "#28E35B5B";
    /**
     * 柱的颜色
     */
    private static final String BAR_COLOR = "#bb434343";
    /**
     * 文字颜色
     */
    private static final String TEXT_COLOR = "#64C5C5C5";
    /**
     * 动画时长
     */
    private static final int ANIM_DURATION = 2000;

    /**
     * 柱子的数据
     */
    private List<BarInfo> mBarInfoList = new ArrayList<>();
    /**
     * 描述字体的大小
     */
    private float mDescTextSize;
    /**
     * 点的内半径
     */
    private float mDotInnerRadius;
    /**
     * 点的外半径
     */
    private float mDotOuterRadius;
    /**
     * 底部边距
     */
    private float mBottomSpacing;
    /**
     * 柱与文字的距离
     */
    private float mBarTextSpacing;
    /**
     * 柱子与柱子的间隔
     */
    private float mBarInterval;
    /**
     * 柱子与上边距的距离
     */
    private float mTopSpacing;
    /**
     * 柱子的高度
     */
    private float mBarHeight;
    /**
     * 每根柱子的宽度
     */
    private float mBarWidth;
    /**
     * 有数据的画布宽
     */
    private float mCanvasWidth;
    /**
     * 用户可见的视图宽
     */
    private float mViewWidth;
    /**
     * 柱子路径
     */
    private Path mBarPath;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 当前动画的进度
     */
    private float mAnimRate = 0;
    /**
     * 柱子颜色
     */
    private int mBarColor;
    /**
     * 内圆颜色
     */
    private int mInnerDotColor;
    /**
     * 外圆颜色
     */
    private int mOuterDotColor;
    /**
     * 字体大小
     */
    private int mTextColor;
    /**
     * 最后触碰的x坐标
     */
    private float mLastTouchX;
    /**
     * 动画
     */
    private ValueAnimator mAnim;

    /**
     * 滑动速度追踪
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 滑动的最大速度
     */
    private int mMaximumVelocity;
    /**
     * 滑动的最小速度
     */
    private int mMinimumVelocity;

    /**
     * 滑动线程
     */
    private FlingRunnable mFling;

    public BarChart(Context context) {
        this(context, null, 0);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        setClickable(true);

        mDescTextSize = dip2px(context, 12f);
        mDotInnerRadius = dip2px(context, 3.5f);
        mDotOuterRadius = dip2px(context, 5f);
        mBarInterval = dip2px(context, 40f);
        mBottomSpacing = dip2px(context, 10f);
        mBarTextSpacing = dip2px(context, 12f);
        mTopSpacing = dip2px(context, 10f);
        mBarWidth = dip2px(context, 1.25f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mBarPath = new Path();

        mBarColor = Color.parseColor(BAR_COLOR);
        mInnerDotColor = Color.parseColor(INNER_DOT_COLOR);
        mOuterDotColor = Color.parseColor(OUTER_DOT_COLOR);
        mTextColor = Color.parseColor(TEXT_COLOR);

        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        mFling = new FlingRunnable(context);

        mAnim = ValueAnimator.ofFloat(0, 1);
        mAnim.setDuration(ANIM_DURATION);
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimRate = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    /**
     * 设置动画数据
     *
     * @param barInfoList
     */
    public void setBarInfoList(List<BarInfo> barInfoList) {
        this.mBarInfoList.clear();
        this.mBarInfoList.addAll(barInfoList);
        this.mCanvasWidth = (this.mBarInfoList.size() + 1) * this.mBarInterval;

        // 停止正在执行的动画
        if (mAnim != null && mAnim.isRunning()) {
            mAnim.cancel();
        }

        // 停止滚动
        if (mFling != null) {
            mFling.stop();
        }

        // 重置动画进度
        mAnimRate = 0;

        // 滚回最开始的坐标
        scrollTo(0, 0);

        // 提交刷新
        postInvalidate();
    }

    /**
     * 启动动画
     */
    public void start() {
        if (mBarInfoList == null || mBarInfoList.size() == 0) {
            Log.e(TAG, "启动动画前，请先设置数据");
            return;
        }

        mAnimRate = 0;

        if (mAnim.isRunning()) {
            mAnim.cancel();
        }

        mAnim.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 柱子的高度 = 控件高度 - 上内边距 - 下内边距 - 字体大小 - 字体与柱子的间距
        this.mBarHeight = h - mTopSpacing - mBottomSpacing - mDescTextSize - mBarTextSpacing;
        this.mViewWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBar(canvas);
        drawDot(canvas);
        drawText(canvas);
    }

    /**
     * 控制屏幕不越界
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 当数据的长度不足以滑动时，不做滑动处理
        if (mCanvasWidth < mViewWidth) {
            return true;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mLastTouchX = event.getX();

            mFling.stop();
        } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
            // 滑动的距离
            float scrollLengthX = event.getX() - mLastTouchX;
            // getScrollX() 小于0，说明画布右移了
            // getScrollX() 大于0，说明画布左移了
            float endX = getScrollX() - scrollLengthX;

            if (scrollLengthX > 0) {    // 画布往右移动 -->

                // 注意：这里的等号不能去除，否则会有闪动
                if (endX <= 0) {
                    scrollTo(0, 0);
                } else {
                    scrollBy((int) -scrollLengthX, 0);
                }

            } else if (scrollLengthX < 0) {                    // 画布往左移动  <--

                if (endX >= mCanvasWidth - mViewWidth) {     // 需要考虑是否右越界
                    scrollTo((int) (mCanvasWidth - mViewWidth), 0);
                } else {
                    scrollBy((int) -scrollLengthX, 0);
                }

            }
            mLastTouchX = event.getX();
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            // 计算当前速度， 1000表示每秒像素数等
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);

            // 获取横向速度
            int velocityX = (int) mVelocityTracker.getXVelocity();

            // 速度要大于最小的速度值，才开始滑动
            if (Math.abs(velocityX) > mMinimumVelocity) {

                int initX = getScrollX();

                int maxX = (int) (mCanvasWidth - mViewWidth);
                if (maxX > 0) {
                    mFling.start(initX, velocityX, initX, maxX);
                }
            }

            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }

        }

        return super.onTouchEvent(event);

    }

    /**
     * 画柱
     *
     * @param canvas
     */
    private void drawBar(Canvas canvas) {
        mBarPath.reset();
        for (int i = 0; i < mBarInfoList.size(); ++i) {

            float x = (i + 1) * mBarInterval;

            if (isInVisibleArea(x)) {
                mBarPath.moveTo(x, mTopSpacing);
                mBarPath.lineTo(x, mBarHeight + mTopSpacing);
            }

        }

        mPaint.setColor(mBarColor);
        mPaint.setStrokeWidth(mBarWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mBarPath, mPaint);
    }

    /**
     * 画数据点
     *
     * @param canvas
     */
    private void drawDot(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mBarInfoList.size(); ++i) {
            float x = (i + 1) * mBarInterval;

            if (isInVisibleArea(x)) {

                BarInfo barInfo = mBarInfoList.get(i);

                float curBarDotY = (float) (mBarHeight * (1 - barInfo.percent * mAnimRate) + mTopSpacing);

                // 画外圆
                mPaint.setColor(mOuterDotColor);
                canvas.drawCircle(x, curBarDotY, mDotOuterRadius, mPaint);

                // 画内圆
                mPaint.setColor(mInnerDotColor);
                canvas.drawCircle(x, curBarDotY, mDotInnerRadius, mPaint);
            }

        }
    }

    /**
     * 画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        float textY = mTopSpacing + mBarHeight + mBarTextSpacing + mDescTextSize / 2;

        for (int i = 0; i < mBarInfoList.size(); ++i) {
            float x = (i + 1) * mBarInterval;

            if (isInVisibleArea(x)) {
                BarInfo barInfo = mBarInfoList.get(i);

                mPaint.setColor(mTextColor);
                mPaint.setTextSize(mDescTextSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(barInfo.desc, x, textY, mPaint);
            }
        }
    }

    /**
     * 是否在可视的范围内
     *
     * @param x
     * @return true：在可视的范围内；false：不在可视的范围内
     */
    private boolean isInVisibleArea(float x) {
        float dx = x - getScrollX();

        return -mBarInterval <= dx && dx <= mViewWidth + mBarInterval;
    }

    private int dip2px(Context context, float dipValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }

    /**
     * author : Jiang zinc
     * email : 56002982@qq.com
     * time : 2019/3/1 下午5:08
     * desc : 柱形图的数据
     * version :
     */
    public static final class BarInfo {
        /**
         * 该柱的描述
         */
        private String desc;
        /**
         * 该柱的占比
         */
        private double percent;

        public BarInfo(String desc, double percent) {
            this.desc = desc;
            this.percent = percent;
        }
    }

    /**
     * 滚动线程
     */
    private class FlingRunnable implements Runnable {

        private Scroller mScroller;

        private int mInitX;
        private int mMinX;
        private int mMaxX;
        private int mVelocityX;

        FlingRunnable(Context context) {
            this.mScroller = new Scroller(context, null, false);
        }

        void start(int initX,
                   int velocityX,
                   int minX,
                   int maxX) {
            this.mInitX = initX;
            this.mVelocityX = velocityX;
            this.mMinX = minX;
            this.mMaxX = maxX;

            // 先停止上一次的滚动
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }

            // 开始 fling
            mScroller.fling(initX, 0, velocityX,
                    0, 0, maxX, 0, 0);
            post(this);
        }

        @Override
        public void run() {

            // 如果已经结束，就不再进行
            if (!mScroller.computeScrollOffset()) {
                return;
            }

            // 计算偏移量
            int currX = mScroller.getCurrX();
            int diffX = mInitX - currX;

            Log.i(TAG, "run: [currX: " + currX + "]\n"
                    + "[diffX: " + diffX + "]\n"
                    + "[initX: " + mInitX + "]\n"
                    + "[minX: " + mMinX + "]\n"
                    + "[maxX: " + mMaxX + "]\n"
                    + "[velocityX: " + mVelocityX + "]\n"
            );

            // 用于记录是否超出边界，如果已经超出边界，则不再进行回调，即使滚动还没有完成
            boolean isEnd = false;

            if (diffX != 0) {

                // 超出右边界，进行修正
                if (getScrollX() + diffX >= mCanvasWidth - mViewWidth) {
                    diffX = (int) (mCanvasWidth - mViewWidth - getScrollX());
                    isEnd = true;
                }

                // 超出左边界，进行修正
                if (getScrollX() <= 0) {
                    diffX = -getScrollX();
                    isEnd = true;
                }

                if (!mScroller.isFinished()) {
                    scrollBy(diffX, 0);
                }
                mInitX = currX;
            }

            if (!isEnd) {
                post(this);
            }
        }

        /**
         * 进行停止
         */
        void stop() {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
        }
    }
}
