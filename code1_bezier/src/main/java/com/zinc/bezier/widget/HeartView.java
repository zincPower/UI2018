package com.zinc.bezier.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/10
 * @description 心
 */
public class HeartView extends BaseView {

    private Path mPath;
    private Paint mPaint;

    private List<PointF> mHeartPointList;

    private List<PointF> mCirclePointList;

    private List<PointF> mCurPointList;

    private ValueAnimator mAnimator;

    public HeartView(Context context) {
        super(context);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();

        mHeartPointList = new ArrayList<>();
        mHeartPointList.add(new PointF(0, dpToPx(-38)));
        mHeartPointList.add(new PointF(dpToPx(50), dpToPx(-103)));
        mHeartPointList.add(new PointF(dpToPx(112), dpToPx(-61)));
        mHeartPointList.add(new PointF(dpToPx(112), dpToPx(-12)));
        mHeartPointList.add(new PointF(dpToPx(112), dpToPx(37)));
        mHeartPointList.add(new PointF(dpToPx(51), dpToPx(90)));
        mHeartPointList.add(new PointF(0, dpToPx(129)));
        mHeartPointList.add(new PointF(dpToPx(-51), dpToPx(90)));
        mHeartPointList.add(new PointF(dpToPx(-112), dpToPx(37)));
        mHeartPointList.add(new PointF(dpToPx(-112), dpToPx(-12)));
        mHeartPointList.add(new PointF(dpToPx(-112), dpToPx(-61)));
        mHeartPointList.add(new PointF(dpToPx(-50), dpToPx(-103)));

        mCirclePointList = new ArrayList<>();
        mCirclePointList.add(new PointF(0, dpToPx(-89)));
        mCirclePointList.add(new PointF(dpToPx(50), dpToPx(-89)));
        mCirclePointList.add(new PointF(dpToPx(90), dpToPx(-49)));
        mCirclePointList.add(new PointF(dpToPx(90), 0));
        mCirclePointList.add(new PointF(dpToPx(90), dpToPx(50)));
        mCirclePointList.add(new PointF(dpToPx(50), dpToPx(90)));
        mCirclePointList.add(new PointF(0, dpToPx(90)));
        mCirclePointList.add(new PointF(dpToPx(-49), dpToPx(90)));
        mCirclePointList.add(new PointF(dpToPx(-89), dpToPx(50)));
        mCirclePointList.add(new PointF(dpToPx(-89), 0));
        mCirclePointList.add(new PointF(dpToPx(-89), dpToPx(-49)));
        mCirclePointList.add(new PointF(dpToPx(-49), dpToPx(-89)));

        mCurPointList = new ArrayList<>();
        mCurPointList.add(new PointF(0, dpToPx(-89)));
        mCurPointList.add(new PointF(dpToPx(50), dpToPx(-89)));
        mCurPointList.add(new PointF(dpToPx(90), dpToPx(-49)));
        mCurPointList.add(new PointF(dpToPx(90), 0));
        mCurPointList.add(new PointF(dpToPx(90), dpToPx(50)));
        mCurPointList.add(new PointF(dpToPx(50), dpToPx(90)));
        mCurPointList.add(new PointF(0, dpToPx(90)));
        mCurPointList.add(new PointF(dpToPx(-49), dpToPx(90)));
        mCurPointList.add(new PointF(dpToPx(-89), dpToPx(50)));
        mCurPointList.add(new PointF(dpToPx(-89), 0));
        mCurPointList.add(new PointF(dpToPx(-89), dpToPx(-49)));
        mCurPointList.add(new PointF(dpToPx(-49), dpToPx(-89)));

        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(1500);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float x = (float) animation.getAnimatedValue();
                float factor = 0.15f;
                double value = Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1;

                for (int i = 0; i < mCurPointList.size(); ++i) {

                    PointF startPoint = mCirclePointList.get(i);
                    PointF endPoint = mHeartPointList.get(i);

                    mCurPointList.get(i).x = startPoint.x + (float) ((endPoint.x - startPoint.x) * value);
                    mCurPointList.get(i).y = startPoint.y + (float) ((endPoint.y - startPoint.y) * value);

                }

                postInvalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        mPath.reset();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                mPath.moveTo(mCurPointList.get(i * 3).x, mCurPointList.get(i * 3).y);
            } else {
                mPath.lineTo(mCurPointList.get(i * 3).x, mCurPointList.get(i * 3).y);
            }

            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }

            mPath.cubicTo(mCurPointList.get(i * 3 + 1).x, mCurPointList.get(i * 3 + 1).y,
                    mCurPointList.get(i * 3 + 2).x, mCurPointList.get(i * 3 + 2).y,
                    mCurPointList.get(endPointIndex).x, mCurPointList.get(endPointIndex).y);
        }

        canvas.drawPath(mPath, mPaint);

    }

    public void start() {
        if (mAnimator.isRunning()) {
            return;
        }
        mAnimator.start();
    }

}
