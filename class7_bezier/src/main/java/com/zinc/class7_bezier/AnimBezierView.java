package com.zinc.class7_bezier;

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
 * @description
 */
public class AnimBezierView extends BaseView {

    // 圆的中心点
    private PointF mCenterPoint;
    // 圆半径
    private float mRadius;

    // 控制点列表，顺序为：右上、右下、左下、左上
    private List<PointF> mControlPointList;

    // 控制点占半径的比例
    private float mRatio;

    private Path mPath;

    private Path mControlPath;

    private Paint mPaint;
    private Paint mCirclePaint;
    private Paint mControlPaint;

    // 有效触碰的范围
    private int mTouchRegionWidth;

    // 线的宽度
    private int LINE_WIDTH;
    // 控制点的半径
    private int POINT_RADIO_WIDTH;

    public AnimBezierView(Context context) {
        super(context);
    }

    public AnimBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        POINT_RADIO_WIDTH = dpToPx(4);
        mTouchRegionWidth = dpToPx(20);

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

        mControlPaint = new Paint();
        mControlPaint.setAntiAlias(true);
        mControlPaint.setStyle(Paint.Style.STROKE);
        mControlPaint.setStrokeWidth(LINE_WIDTH);
        mControlPaint.setColor(Color.parseColor("#e89abe"));

        mControlPath = new Path();

        mRatio = 0.55f;

        calculateControlPoint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        mPath.reset();

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                mPath.moveTo(mControlPointList.get(i * 4).x, mControlPointList.get(i * 4).y);
            } else {
                mPath.lineTo(mControlPointList.get(i * 4).x, mControlPointList.get(i * 4).y);
            }
            mPath.cubicTo(mControlPointList.get(i * 4 + 1).x, mControlPointList.get(i * 4 + 1).y,
                    mControlPointList.get(i * 4 + 2).x, mControlPointList.get(i * 4 + 2).y,
                    mControlPointList.get(i * 4 + 3).x, mControlPointList.get(i * 4 + 3).y);
        }

        mControlPath.reset();

        for (int i = 0; i < mControlPointList.size(); ++i) {
            PointF point = mControlPointList.get(i);
            if (i == 0) {
                mControlPath.moveTo(point.x, point.y);
            } else {
                mControlPath.lineTo(point.x, point.y);
            }
        }
        mControlPath.close();

        // 绘制贝塞尔曲线
        canvas.drawPath(mPath, mPaint);

        // 绘制圆
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCirclePaint);

        mControlPaint.setStyle(Paint.Style.STROKE);
        // 控制线
        canvas.drawPath(mControlPath, mControlPaint);

        mControlPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mControlPointList.size(); ++i) {
            PointF point = mControlPointList.get(i);
            canvas.drawCircle(point.x, point.y, POINT_RADIO_WIDTH, mControlPaint);
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
        mControlPointList.add(new PointF(mRadius, 0));

        // 右下
        mControlPointList.add(new PointF(mRadius, 0));
        mControlPointList.add(new PointF(mRadius, controlWidth));
        mControlPointList.add(new PointF(controlWidth, mRadius));
        mControlPointList.add(new PointF(0, mRadius));

        // 左下
        mControlPointList.add(new PointF(0, mRadius));
        mControlPointList.add(new PointF(-controlWidth, mRadius));
        mControlPointList.add(new PointF(-mRadius, controlWidth));
        mControlPointList.add(new PointF(-mRadius, 0));

        // 左上
        mControlPointList.add(new PointF(-mRadius, 0));
        mControlPointList.add(new PointF(-mRadius, -controlWidth));
        mControlPointList.add(new PointF(-controlWidth, -mRadius));
        mControlPointList.add(new PointF(0, -mRadius));

    }

}
