package com.zinc.class12_animation.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/24
 * @description 插值器的坐标显示
 */
public class TimeInterpolatorView extends View {

    // 外边距
    private static final int PADDING = dpToPx(2.5f);
    // X、Y 轴色
    private static final int COORDINATION_LINE_COLOR = Color.BLACK;
    // 网格线色
    private static final int GRID_LINE_COLOR = Color.LTGRAY;
    // 数据线色
    private static final int DATA_LINE_COLOR = Color.parseColor("#DB001B");
    // 默认的最低点
    private static final PointF DEFAULT_MIN_POINT = new PointF(0, 0);
    // 默认的最高点
    private static final PointF DEFAULT_MAX_POINT = new PointF(0, 1);
    // 10个间隔
    private static final int GRID_INTERVAL_COUNT = 10;
    // 每个间隔的跨幅
    private static final float GRID_INTERVAL_LENGTH = 0.1f;

    // 速率的数据
    private final List<PointF> mLineDataList = new ArrayList<>();

    // 坐标的画笔
    private Paint mCoordinationPaint;
    // 速率的轨迹
    private Path mDataPath = new Path();

    // 数据的最低点
    private PointF mMinPoint = DEFAULT_MIN_POINT;
    // 数据的最高点
    private PointF mMaxPoint = DEFAULT_MAX_POINT;

    // 视图的宽
    private float mViewWidth;
    // 视图的高
    private float mViewHeight;
    // 坐标的宽
    private float mWidth;

    // 坐标中每个下标 的宽度
    private float mEachItemWidth;

    private int mPositiveCount;
    private int mNegativeCount;

    public TimeInterpolatorView(Context context) {
        this(context, null, 0);
    }

    public TimeInterpolatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeInterpolatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mCoordinationPaint = new Paint();
        mCoordinationPaint.setAntiAlias(true);
        mCoordinationPaint.setStyle(Paint.Style.STROKE);

    }

    public void setLineData(List<PointF> lineDataList) {
        mLineDataList.clear();
        mLineDataList.addAll(lineDataList);

        mMinPoint = DEFAULT_MIN_POINT;
        mMaxPoint = DEFAULT_MAX_POINT;
        // 构建 路径，并选出最高和最低的point
        for (int i = 0; i < mLineDataList.size(); i++) {
            PointF curPoint = mLineDataList.get(i);

            // 选最低点
            if (curPoint.y < mMinPoint.y) {
                mMinPoint = curPoint;
            }

            // 选最高点
            if (curPoint.y > mMaxPoint.y) {
                mMaxPoint = curPoint;
            }

        }

        calculateEachItemWidth();

        invalidate();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        mWidth = Math.min(w, h);

        calculateEachItemWidth();
    }

    private void calculateEachItemWidth() {

        mPositiveCount = (int) Math.abs(Math.ceil(mMaxPoint.y / GRID_INTERVAL_LENGTH));
        mNegativeCount = (int) Math.abs(Math.floor(mMinPoint.y / GRID_INTERVAL_LENGTH));

        // 计算需要分割的数量，最少十个
        int intervalCount = mPositiveCount + mNegativeCount;
        intervalCount = Math.max(intervalCount, GRID_INTERVAL_COUNT);

        mEachItemWidth = mWidth / intervalCount;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        buildDataPath();

        canvas.save();
        moveToTheOrigin(canvas);

        drawCoordination(canvas);
        drawGrid(canvas);

        drawDataLine(canvas);

        canvas.restore();

    }

    private void drawGrid(Canvas canvas) {

    }

    /**
     * 画数据线
     *
     * @param canvas
     */
    private void drawDataLine(Canvas canvas) {

        mCoordinationPaint.setStrokeWidth(dpToPx(2f));
        mCoordinationPaint.setColor(DATA_LINE_COLOR);

        canvas.drawPath(mDataPath, mCoordinationPaint);

    }

    /**
     * 画 x、y 轴
     *
     * @param canvas
     */
    private void drawCoordination(Canvas canvas) {

        mCoordinationPaint.setStrokeWidth(dpToPx(1.5f));
        mCoordinationPaint.setColor(COORDINATION_LINE_COLOR);

        canvas.drawCircle(0, -mPositiveCount * mEachItemWidth, 15, mCoordinationPaint);

        canvas.drawCircle(GRID_INTERVAL_COUNT * mEachItemWidth,
                0, 15, mCoordinationPaint);

        canvas.drawLine(0,
                -mPositiveCount * mEachItemWidth,
                0,
                mNegativeCount * mEachItemWidth,
                mCoordinationPaint);

        canvas.drawLine(0,
                0,
                GRID_INTERVAL_COUNT * mEachItemWidth,
                0,
                mCoordinationPaint);

    }

    /**
     * 构建数据路径
     */
    private void buildDataPath() {
        mDataPath.reset();
        // 构建 路径，并选出最高和最低的point
        for (int i = 0; i < mLineDataList.size(); i++) {
            PointF curPoint = mLineDataList.get(i);
            if (i == 0) {
                mDataPath.moveTo(curPoint.x * mWidth, -curPoint.y * mWidth);
            } else {
                mDataPath.lineTo(curPoint.x * mWidth, -curPoint.y * mWidth);
            }
        }
    }

    /**
     * 将画布移至 原点
     */
    private void moveToTheOrigin(Canvas canvas) {

        float verHeight = mEachItemWidth * mPositiveCount;

        // 计算 横向移动距离
        float horPadding = mViewWidth - mEachItemWidth * GRID_INTERVAL_COUNT + PADDING;
        float verPadding = mViewHeight - mEachItemWidth * (mPositiveCount + mNegativeCount) - PADDING;

        canvas.translate(horPadding / 2, verPadding / 2 + verHeight);

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
