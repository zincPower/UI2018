package com.zinc.bezier.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
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
public class DIYBezierView extends BaseView {

    private static final String BEZIER_CIRCLE_COLOR = "#20A298";    //绿色
    private static final String NATIVE_CIRCLE_COLOR = "#F6A010";    //橙色
    private static final String CONTROL_LINE_COLOR = "#FA3096";     //艳红色
    private static final String SEL_POINT_COLOR = "#1296db";        //蓝色

    // 圆的中心点
    private PointF mCenterPoint;
    // 圆半径
    private float mRadius;

    // 控制点列表，顺序为：右上、右下、左下、左上
    private List<PointF> mControlPointList;

    // 选中的点集合，受 status 影响
    private final List<PointF> mCurSelectPointList = new ArrayList<>();
    // 选中的点
    private PointF mSelPoint;

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
    // 选中控制点的半径
    private int SEL_POINT_RADIO_WIDTH;

    // 拽动状态
    private Status mStatus;

    // 是否显示辅助线
    private boolean mIsShowHelpLine;

    // 触碰的x轴
    private float mLastX = -1;
    // 触碰的y轴
    private float mLastY = -1;

    public DIYBezierView(Context context) {
        super(context);
    }

    public DIYBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DIYBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    /**
     * 获取控制点
     *
     * @return
     */
    public List<PointF> getControlPointList() {
        return mControlPointList;
    }

    public void setStatus(Status status) {
        this.mStatus = status;
    }

    public void setIsShowHelpLine(boolean isShowHelpLine) {
        this.mIsShowHelpLine = isShowHelpLine;
        invalidate();
    }

    @Override
    protected void init(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        mRadius = width / 4;

        LINE_WIDTH = dpToPx(2);
        POINT_RADIO_WIDTH = dpToPx(4);
        SEL_POINT_RADIO_WIDTH = dpToPx(6);
        mTouchRegionWidth = dpToPx(20);

        mCenterPoint = new PointF(0, 0);

        mControlPointList = new ArrayList<>();

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(BEZIER_CIRCLE_COLOR));

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(LINE_WIDTH);
        mCirclePaint.setColor(Color.parseColor(NATIVE_CIRCLE_COLOR));

        mControlPaint = new Paint();
        mControlPaint.setAntiAlias(true);
        mControlPaint.setStyle(Paint.Style.STROKE);
        mControlPaint.setStrokeWidth(LINE_WIDTH);
        mControlPaint.setColor(Color.parseColor(CONTROL_LINE_COLOR));

        mControlPath = new Path();

        mStatus = Status.FREE;

        mIsShowHelpLine = true;

        mRatio = 0.55f;

        calculateControlPoint();
    }

    public void reset() {
        calculateControlPoint();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        mPath.reset();

        // 画圆
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

        // 不需要辅助线，则画完贝塞尔曲线就终止
        if(!mIsShowHelpLine){
            return;
        }

        // 绘制圆
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCirclePaint);

        // 控制基线
        mControlPath.reset();
        for (int i = 0; i < 4; i++) {
            int startIndex = i * 3;
            if (i == 0) {
                mControlPath.moveTo(mControlPointList.get(mControlPointList.size() - 1).x,
                        mControlPointList.get(mControlPointList.size() - 1).y);
            } else {
                mControlPath.moveTo(mControlPointList.get(startIndex - 1).x, mControlPointList.get(startIndex - 1).y);
            }

            mControlPath.lineTo(mControlPointList.get(startIndex).x, mControlPointList.get(startIndex).y);
            mControlPath.lineTo(mControlPointList.get(startIndex + 1).x, mControlPointList.get(startIndex + 1).y);
        }
        mControlPaint.setColor(Color.parseColor(CONTROL_LINE_COLOR));
        mControlPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mControlPath, mControlPaint);

        // 控制点
        mControlPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mControlPointList.size(); ++i) {
            PointF point = mControlPointList.get(i);
            float radio;
            if (mCurSelectPointList.contains(point)) {      // 绘制选中的点
                mControlPaint.setColor(Color.parseColor(SEL_POINT_COLOR));
                radio = SEL_POINT_RADIO_WIDTH;
            } else {        // 绘制为选中的点
                mControlPaint.setColor(Color.parseColor(CONTROL_LINE_COLOR));
                radio = POINT_RADIO_WIDTH;
            }
            canvas.drawCircle(point.x, point.y, radio, mControlPaint);
        }

        // 如果为三点拽动，将三点连接
        if (mStatus == Status.THREE) {
            if (mCurSelectPointList.size() == 1) {
                return;
            }
            for (int i = 0; i < mCurSelectPointList.size() - 1; ++i) {
                PointF p1 = mCurSelectPointList.get(i);
                PointF p2 = mCurSelectPointList.get(i + 1);
                mControlPaint.setColor(Color.parseColor(SEL_POINT_COLOR));
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mControlPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 触碰的坐标
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (selectControlPoint(x, y)) {
                    mLastX = x;
                    mLastY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLastX == -1 || mLastY == -1) {
                    return true;
                }

                // 计算偏移值
                float offsetX = x - mLastX;
                float offsetY = y - mLastY;

                if ((mStatus == Status.MIRROR_DIFF || mStatus == Status.MIRROR_SAME) && mSelPoint != null) {

                    mSelPoint.x = mSelPoint.x + offsetX;
                    mSelPoint.y = mSelPoint.y + offsetY;

                    PointF otherPoint = null;
                    for (PointF point : mCurSelectPointList) {
                        if (point != mSelPoint) {
                            otherPoint = point;
                            break;
                        }
                    }

                    if (mStatus == Status.MIRROR_DIFF) {
                        offsetX = -offsetX;
                        offsetY = -offsetY;
                    }

                    if (otherPoint != null) {
                        otherPoint.x = otherPoint.x + offsetX;
                        otherPoint.y = otherPoint.y + offsetY;
                    }

                } else {
                    // 更新选中
                    for (PointF point : mCurSelectPointList) {
                        point.x = point.x + offsetX;
                        point.y = point.y + offsetY;
                    }
                }


                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:
                mCurSelectPointList.clear();
                mSelPoint = null;
                mLastX = -1;
                mLastY = -1;
                break;
        }

        invalidate();

        return true;
    }

    /**
     * 是否在有效的触碰范围
     *
     * @param x
     * @param y
     * @return true 有选中；false 无选中
     */
    private boolean selectControlPoint(float x, float y) {

        // 选中的下标
        int selIndex = -1;

        for (int i = 0; i < mControlPointList.size(); ++i) {

            PointF controlPoint = mControlPointList.get(i);

            float resultX = controlPoint.x + mWidth / 2;
            float resultY = controlPoint.y + mHeight / 2;

            RectF pointRange = new RectF(resultX - mTouchRegionWidth,
                    resultY - mTouchRegionWidth,
                    resultX + mTouchRegionWidth,
                    resultY + mTouchRegionWidth);

            if (pointRange.contains(x, y)) {
                selIndex = i;
                break;
            }
        }

        // 如果没有选中的就返回
        if (selIndex == -1) {
            return false;
        }

        // 清空之前的选中点
        mCurSelectPointList.clear();

        mSelPoint = mControlPointList.get(selIndex);

        switch (mStatus) {
            case FREE:  // 任意点拽动
                mCurSelectPointList.add(mControlPointList.get(selIndex));
                break;
            case THREE: // 三点拽动，需要同时选中三个

                // 进行整体的偏移下标，便于计算
                int offsetSelIndex = (selIndex + 1) % 12;
                int offsetRangeIndex = offsetSelIndex / 3;

                if (offsetRangeIndex == 0) {
                    mCurSelectPointList.add(mControlPointList.get(11));
                } else {
                    mCurSelectPointList.add(mControlPointList.get(offsetRangeIndex * 3 - 1));
                }

                mCurSelectPointList.add(mControlPointList.get(offsetRangeIndex * 3));
                mCurSelectPointList.add(mControlPointList.get(offsetRangeIndex * 3 + 1));

                break;
            // 镜像，需要同时选中两个
            case MIRROR_DIFF:
            case MIRROR_SAME:
                if (selIndex == 0 || selIndex == 6) {
                    mCurSelectPointList.add(mControlPointList.get(0));
                    mCurSelectPointList.add(mControlPointList.get(6));
                } else {
                    mCurSelectPointList.add(mControlPointList.get(selIndex));
                    mCurSelectPointList.add(mControlPointList.get(12 - selIndex));
                }

                break;
        }

        return true;

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

    public enum Status {
        FREE,          // 自由拽动
        THREE,         // 三点拽动
        MIRROR_DIFF,   // 镜像异向
        MIRROR_SAME,   // 镜像同向
    }

}
