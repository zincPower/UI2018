package com.zinc.bezier.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.bezier.R;
import com.zinc.bezier.utils.MathUtils;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/11
 * @description 粘性小红点
 */

public class StickDotView extends android.support.v7.widget.AppCompatTextView {

    public DragView mDragView;
    private float mWidth, mHeight;

    private onDragStatusListener onDragListener;

    public StickDotView(Context context) {
        this(context, null, 0);
    }

    public StickDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //获得根View
        View rootView = getRootView();

        //获得触摸位置在全屏所在位置
        float mRawX = event.getRawX();
        float mRawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //请求父View不拦截
                getParent().requestDisallowInterceptTouchEvent(true);

                //获得当前View在屏幕上的位置
                int[] cLocation = new int[2];
                getLocationOnScreen(cLocation);

                if (rootView instanceof ViewGroup) {
                    //初始化拖拽时显示的View
                    mDragView = new DragView(getContext());

                    //设置固定圆的圆心坐标
                    mDragView.setStickyPoint(cLocation[0] + mWidth / 2,
                            cLocation[1] + mHeight / 2,
                            mRawX,
                            mRawY);

                    //获得缓存的bitmap，滑动时直接通过drawBitmap绘制出来
                    setDrawingCacheEnabled(true);
                    Bitmap bitmap = getDrawingCache();

                    if (bitmap != null) {
                        mDragView.setCacheBitmap(bitmap);
                        //将DragView添加到RootView中，这样就可以全屏滑动了
                        ((ViewGroup) rootView).addView(mDragView);
                        setVisibility(INVISIBLE);
                    }

                }

                break;

            case MotionEvent.ACTION_MOVE:

                //请求父View不拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                if (mDragView != null) {
                    //更新DragView的位置
                    mDragView.setDragViewLocation(mRawX, mRawY);
                }

                break;

            case MotionEvent.ACTION_UP:

                getParent().requestDisallowInterceptTouchEvent(false);
                if (mDragView != null) {
                    //手抬起时来判断各种情况
                    mDragView.setDragUp();
                }

                break;

        }
        return true;
    }

    public interface onDragStatusListener {
        void onDrag();

        void onMove();

        void onRestore();

        void onDismiss();
    }

    public void setOnDragListener(onDragStatusListener onDragListener) {
        this.onDragListener = onDragListener;
    }

    /**
     * 拖拽时的椭圆
     */
    public class DragView extends View {
        private static final int STATE_INIT = 0;//默认静止状态
        private static final int STATE_DRAG = 1;//拖拽状态
        private static final int STATE_MOVE = 2;//移动状态
        private static final int STATE_DISMISS = 3;//消失状态

        private Path mPath;
        private Paint mPaint;
        private Bitmap mCacheBitmap;
        // 拖拽圆的圆点
        private PointF mDragPointF;
        // 固定圆的圆点
        private PointF mStickyPointF;
        // 二阶贝塞尔曲线控制点
        private PointF mControlPoint;
        // 拖拽的距离
        private float mDragDistance;
        // 最大拖拽距离
        private float mMaxDistance = dpToPx(100);
        // View的宽和高
        private float mWidth, mHeight;

        // 当前红点的状态
        private int mState;

        // 固定圆的半径
        private int mStickRadius;
        // 固定小圆的默认半径
        private int mDefaultRadius = dpToPx(10);
        // 拖拽圆的半径
        private int mDragRadius = dpToPx(15);

        private int[] mExplodeRes = {R.drawable.explode1,
                R.drawable.explode2,
                R.drawable.explode3,
                R.drawable.explode4,
                R.drawable.explode5};

        private Bitmap[] mBitmaps;

        private int mExplodeIndex;

        public DragView(Context context) {
            this(context, null, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            // 初始化Path
            mPath = new Path();

            // 初始化Paint
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(Color.RED);

            // 初始化拖拽点
            mDragPointF = new PointF();
            // 初始化固定点
            mStickyPointF = new PointF();

            // 初始化状态
            mState = STATE_INIT;

            // 初始化消失动画资源
            mBitmaps = new Bitmap[mExplodeRes.length];
            for (int i = 0; i < mExplodeRes.length; i++) {
                mBitmaps[i] = BitmapFactory.decodeResource(getResources(), mExplodeRes[i]);
            }

        }

        @Override
        protected void onDraw(Canvas canvas) {

            if (isInsideRange() && mState == STATE_DRAG) {

                // 绘制固定的小圆
                canvas.drawCircle(mStickyPointF.x, mStickyPointF.y, mStickRadius, mPaint);

                // 首先获得两圆心之间的斜率
                Float linK = MathUtils.getLineSlope(mDragPointF, mStickyPointF);

                // 然后通过两个圆心和半径、斜率来获得外切线的切点
                PointF[] stickyPoints = MathUtils.getIntersectionPoints(mStickyPointF, mStickRadius, linK);
                mDragRadius = (int) Math.min(mWidth, mHeight) / 2;
                PointF[] dragPoints = MathUtils.getIntersectionPoints(mDragPointF, mDragRadius, linK);

                // 二阶贝塞尔曲线的控制点取得两圆心的中点
                mControlPoint = MathUtils.getMiddlePoint(mDragPointF, mStickyPointF);

                // 绘制贝塞尔曲线
                mPath.reset();
                mPath.moveTo(stickyPoints[0].x, stickyPoints[0].y);
                mPath.quadTo(mControlPoint.x, mControlPoint.y, dragPoints[0].x, dragPoints[0].y);
                mPath.lineTo(dragPoints[1].x, dragPoints[1].y);
                mPath.quadTo(mControlPoint.x, mControlPoint.y, stickyPoints[1].x, stickyPoints[1].y);
                mPath.lineTo(stickyPoints[0].x, stickyPoints[0].y);

                canvas.drawPath(mPath, mPaint);
            }

            // 绘制 StickDotView 内容
            if (mCacheBitmap != null && mState != STATE_DISMISS) {
                canvas.drawBitmap(mCacheBitmap,
                        mDragPointF.x - mWidth / 2,
                        mDragPointF.y - mHeight / 2,
                        mPaint);
            }

            if (mState == STATE_DISMISS && mExplodeIndex < mExplodeRes.length) {
                // 绘制小红点消失时的爆炸动画
                canvas.drawBitmap(mBitmaps[mExplodeIndex],
                        mDragPointF.x - mWidth / 2,
                        mDragPointF.y - mHeight / 2,
                        mPaint);
            }
        }

        /**
         * 设置缓存Bitmap，即 StickDotView 的视图
         *
         * @param mCacheBitmap 缓存Bitmap
         */
        public void setCacheBitmap(Bitmap mCacheBitmap) {
            this.mCacheBitmap = mCacheBitmap;
            mWidth = mCacheBitmap.getWidth();
            mHeight = mCacheBitmap.getHeight();
            mDefaultRadius = (int) Math.min(mWidth, mHeight) / 2;
        }


        /**
         * 设置固定圆的圆心和半径
         *
         * @param stickyX 固定圆的X坐标
         * @param stickyY 固定圆的Y坐标
         */
        public void setStickyPoint(float stickyX,
                                   float stickyY,
                                   float touchX,
                                   float touchY) {
            //分别设置固定圆和拖拽圆的坐标
            mStickyPointF.set(stickyX, stickyY);
            mDragPointF.set(touchX, touchY);

            //通过两个圆点算出圆心距，也是拖拽的距离
            mDragDistance = MathUtils.getTwoPointDistance(mDragPointF, mStickyPointF);

            if (mDragDistance <= mMaxDistance) {

                //如果拖拽距离小于规定最大距离，则固定的圆应该越来越小，这样看着才符合实际
                mStickRadius = (int) (mDefaultRadius - mDragDistance / 10) < 10 ? 10 : (int) (mDefaultRadius - mDragDistance / 10);
                mState = STATE_DRAG;

            } else {

                mState = STATE_INIT;

            }
        }

        /**
         * 设置拖拽的坐标位置
         *
         * @param touchX 拖拽时的X坐标
         * @param touchY 拖拽时的Y坐标
         */
        public void setDragViewLocation(float touchX, float touchY) {
            mDragPointF.set(touchX, touchY);
            //随时更改圆心距
            mDragDistance = MathUtils.getTwoPointDistance(mDragPointF, mStickyPointF);
            if (mState == STATE_DRAG) {
                if (isInsideRange()) {
                    mStickRadius = (int) (mDefaultRadius - mDragDistance / 10) < 10 ? 10 : (int) (mDefaultRadius - mDragDistance / 10);
                } else {
                    mState = STATE_MOVE;
                    if (onDragListener != null) {
                        onDragListener.onMove();
                    }
                }
            }
            invalidate();
        }

        public void setDragUp() {

            if (mState == STATE_DRAG && isInsideRange()) {

                //拖拽状态且在范围之内
                startResetAnimator();

            } else if (mState == STATE_MOVE) {

                if (isInsideRange()) {
                    //在范围之内 需要RESET
                    startResetAnimator();
                } else {
                    //在范围之外 消失动画
                    mState = STATE_DISMISS;
                    startExplodeAnim();
                }

            }
        }


        /**
         * 是否在最大拖拽范围之内
         *
         * @return true 范围之内 false 范围之外
         */
        private boolean isInsideRange() {
            return mDragDistance <= mMaxDistance;
        }

        /**
         * 爆炸动画
         */
        private void startExplodeAnim() {
            ValueAnimator animator = ValueAnimator.ofInt(0, mExplodeRes.length);
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mExplodeIndex = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (onDragListener != null) {
                        onDragListener.onDismiss();
                    }
                }
            });
            animator.start();
        }

        /**
         * 拖拽红点reset动画
         */
        private void startResetAnimator() {

            if (mState == STATE_DRAG) {

                ValueAnimator animator = ValueAnimator.ofObject(
                        new PointEvaluator(),
                        new PointF(mDragPointF.x, mDragPointF.y),
                        new PointF(mStickyPointF.x, mStickyPointF.y));
                animator.setDuration(500);
                animator.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        float factor = 0.4f;
                        return (float) (Math.pow(2, -10 * factor) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
                    }
                });
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF curPoint = (PointF) animation.getAnimatedValue();
                        mDragPointF.set(curPoint.x, curPoint.y);
                        invalidate();
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        clearDragView();
                        if (onDragListener != null) {
                            onDragListener.onDrag();
                        }
                    }
                });
                animator.start();

            } else if (mState == STATE_MOVE) {

                //先拖拽到范围之外 在拖拽回范围之内
                mDragPointF.set(mStickyPointF.x, mStickyPointF.y);
                invalidate();
                clearDragView();
                if (onDragListener != null) {
                    onDragListener.onRestore();
                }

            }

        }

        private void clearDragView() {
            ViewGroup viewGroup = (ViewGroup) getParent();
            viewGroup.removeView(DragView.this);
            StickDotView.this.setVisibility(VISIBLE);
        }
    }

    private static class PointEvaluator implements TypeEvaluator<PointF> {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float x = startValue.x + fraction * (endValue.x - startValue.x);
            float y = startValue.y + fraction * (endValue.y - startValue.y);
            return new PointF(x, y);
        }
    }

    /**
     * 转换 dp 至 px
     *
     * @param dp dp像素
     * @return
     */
    protected int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }


}
