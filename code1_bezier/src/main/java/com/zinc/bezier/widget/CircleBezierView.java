package com.zinc.bezier.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/9
 * @description 用贝塞尔曲线绘制圆的过程
 */
public class CircleBezierView extends BaseView {

    // 圆的中心点
    private PointF mCenterPoint;
    // 圆半径
    private float mRadius;

    // 控制点列表，顺序为：右上、右下、左下、左上
    private List<PointF> mControlPointList;

    // 控制点占半径的比例
    private float mRatio;

    // 圆的路径
    private Path mPath;

    // 绘制贝塞尔曲线的画笔
    private Paint mPaint;
    // 绘制圆的画笔
    private Paint mCirclePaint;
    // 绘制控制线的画笔
    private Paint mLinePaint;

    // 控制线的颜色
    private int[] mLineColor;

    // 线的宽度
    private int LINE_WIDTH;

    public CircleBezierView(Context context) {
        super(context);
    }

    public CircleBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置比例
     *
     * @param ratio 比例，0-1
     */
    public void setRatio(float ratio) {
        this.mRatio = ratio;
        calculateControlPoint();
        invalidate();
    }

    @Override
    protected void init(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        mRadius = width / 3;

        LINE_WIDTH = dpToPx(2);

        mCenterPoint = new PointF(0, 0);

        mControlPointList = new ArrayList<>();

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#1296db"));

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(LINE_WIDTH);
        mCirclePaint.setColor(Color.RED);

        mRatio = 0.55f;

        mLineColor = new int[4];
        mLineColor[0] = Color.parseColor("#f4ea2a");    //黄色
        mLineColor[1] = Color.parseColor("#1afa29");    //绿色
        mLineColor[2] = Color.parseColor("#efb336");    //橙色
        mLineColor[3] = Color.parseColor("#e89abe");    //粉色

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(dpToPx(2));

        calculateControlPoint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        mPath.reset();

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                mPath.moveTo(mControlPointList.get(i * 3).x, mControlPointList.get(i * 3).y);
            } else {
                mPath.lineTo(mControlPointList.get(i * 3).x, mControlPointList.get(i * 3).y);
            }

            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }

            mPath.cubicTo(mControlPointList.get(i * 3 + 1).x, mControlPointList.get(i * 3 + 1).y,
                    mControlPointList.get(i * 3 + 2).x, mControlPointList.get(i * 3 + 2).y,
                    mControlPointList.get(endPointIndex).x, mControlPointList.get(endPointIndex).y);
        }

        // 绘制贝塞尔曲线
        canvas.drawPath(mPath, mPaint);

        // 绘制圆
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCirclePaint);

        // 绘制控制线
        for (int i = 0; i < mControlPointList.size(); ++i) {
            // 设置颜色
            mLinePaint.setColor(mLineColor[i / 3]);

            int endPointIndex = (i == mControlPointList.size() - 1) ? 0 : i + 1;

            canvas.drawLine(mControlPointList.get(i).x,
                    mControlPointList.get(i).y,
                    mControlPointList.get(endPointIndex).x,
                    mControlPointList.get(endPointIndex).y,
                    mLinePaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 计算圆的控制点
     */
    private void calculateControlPoint() {
        // 计算 中间控制点到端点的距离
        float controlWidth = mRatio * mRadius;

        mControlPointList.clear();

        // 右上
        mControlPointList.add(new PointF(0, -mRadius));
        mControlPointList.add(new PointF(controlWidth, -mRadius));
        mControlPointList.add(new PointF(mRadius, -controlWidth));

        // 右下
        mControlPointList.add(new PointF(mRadius, 0));
        mControlPointList.add(new PointF(mRadius, controlWidth));
        mControlPointList.add(new PointF(controlWidth, mRadius));

        // 左下
        mControlPointList.add(new PointF(0, mRadius));
        mControlPointList.add(new PointF(-controlWidth, mRadius));
        mControlPointList.add(new PointF(-mRadius, controlWidth));
        // 左上
        mControlPointList.add(new PointF(-mRadius, 0));
        mControlPointList.add(new PointF(-mRadius, -controlWidth));
        mControlPointList.add(new PointF(-controlWidth, -mRadius));

    }

}
