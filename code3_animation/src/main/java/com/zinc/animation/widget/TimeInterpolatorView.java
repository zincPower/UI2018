package com.zinc.animation.widget;

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
    private static final int PADDING = dpToPx(5f);
    // 字体大小
    private static final int TEXT_SIZE = dpToPx(8f);
    // 点的半径
    private static final int CUR_POINT_RADIUS = dpToPx(4.5f);

    // X、Y 轴色
    private static final int COORDINATION_LINE_COLOR = Color.BLACK;
    // 网格线色
    private static final int GRID_LINE_COLOR = Color.LTGRAY;
    // 数据线色
    private static final int DATA_LINE_COLOR = Color.parseColor("#DB001B");
    // 当前点的色
    private static final int CUR_POINT_COLOR = Color.parseColor("#DC143C");
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
    private Paint mLinePaint;
    // 速率的轨迹
    private Path mDataPath = new Path();
    // 字体画笔
    private Paint mTextPaint;
    // 点的笔
    private Paint mPointPaint;

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

    // 当前的点
    private PointF mCurPoint;

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

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(TEXT_SIZE);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);

    }

    /**
     * 设置当前移动的点
     *
     * @param curPoint
     */
    public void setCurPoint(PointF curPoint) {
        this.mCurPoint = curPoint;
        invalidate();
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

        // 需要减去 padding 的宽度 和 字体的大小
        mWidth = Math.min(w, h) - 2 * PADDING - TEXT_SIZE;

        calculateEachItemWidth();
    }

    /**
     * 计算每个格子的大小
     */
    private void calculateEachItemWidth() {

        // 获取 y正半轴 的分割个数
        mPositiveCount = (int) Math.abs(Math.ceil(mMaxPoint.y / GRID_INTERVAL_LENGTH));
        // 获取 y负半轴 的分割个数
        mNegativeCount = (int) Math.abs(Math.floor(mMinPoint.y / GRID_INTERVAL_LENGTH));

        // 计算需要分割的数量，最少十个
        int intervalCount = mPositiveCount + mNegativeCount;
        intervalCount = Math.max(intervalCount, GRID_INTERVAL_COUNT);

        mEachItemWidth = mWidth / intervalCount;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 构建轨迹
        buildDataPath();

        canvas.save();

        // 移至原点
        moveToTheOrigin(canvas);

        // 画坐标
        drawCoordination(canvas);
        // 画网格
        drawGrid(canvas);

        // 画数据线
        drawDataLine(canvas);

        // 画下标
        drawText(canvas);

        // 画当前的点
        drawPoint(canvas);

        canvas.restore();

    }

    /**
     * 画点
     *
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        if (mCurPoint == null) {
            return;
        }

        mPointPaint.setColor(CUR_POINT_COLOR);
        canvas.drawCircle(mCurPoint.x * mEachItemWidth * GRID_INTERVAL_COUNT,
                -mCurPoint.y * mEachItemWidth * GRID_INTERVAL_COUNT,
                CUR_POINT_RADIUS,
                mPointPaint);
    }

    /**
     * 画下标
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        canvas.drawText("0", -PADDING,
                0,
                mTextPaint);

        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 1; i <= mPositiveCount; ++i) {
            if (i <= 10) {
                mTextPaint.setColor(COORDINATION_LINE_COLOR);
            } else {
                mTextPaint.setColor(DATA_LINE_COLOR);
            }
            canvas.drawText(getNumString(i * 0.1f), -PADDING / 2,
                    -i * mEachItemWidth,
                    mTextPaint);
        }

        mTextPaint.setColor(DATA_LINE_COLOR);
        for (int i = 1; i <= mNegativeCount; ++i) {
            canvas.drawText(getNumString(i * -0.1f), -PADDING / 2,
                    i * mEachItemWidth,
                    mTextPaint);
        }

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(COORDINATION_LINE_COLOR);
        for (int i = 1; i <= GRID_INTERVAL_COUNT; ++i) {
            canvas.drawText(getNumString(i * 0.1f), i * mEachItemWidth,
                    PADDING / 2 + TEXT_SIZE,
                    mTextPaint);
        }
    }

    private String getNumString(float num) {
        return String.format("%.1f", num);
    }

    /**
     * 画网格线
     *
     * @param canvas
     */
    private void drawGrid(Canvas canvas) {
        mLinePaint.setStrokeWidth(dpToPx(0.5f));
        mLinePaint.setColor(GRID_LINE_COLOR);

        // 画y正轴横线
        for (int i = 1; i <= mPositiveCount; ++i) {
            canvas.drawLine(0,
                    -i * mEachItemWidth,
                    GRID_INTERVAL_COUNT * mEachItemWidth,
                    -i * mEachItemWidth,
                    mLinePaint);
        }

        // 画y负轴横线
        for (int i = 1; i <= mNegativeCount; ++i) {
            canvas.drawLine(0,
                    i * mEachItemWidth,
                    GRID_INTERVAL_COUNT * mEachItemWidth,
                    i * mEachItemWidth,
                    mLinePaint);
        }

        // 画x正轴竖线
        for (int i = 1; i <= GRID_INTERVAL_COUNT; ++i) {
            canvas.drawLine(i * mEachItemWidth,
                    -mPositiveCount * mEachItemWidth,
                    i * mEachItemWidth,
                    mNegativeCount * mEachItemWidth,
                    mLinePaint);
        }


    }

    /**
     * 画数据线
     *
     * @param canvas
     */
    private void drawDataLine(Canvas canvas) {

        mLinePaint.setStrokeWidth(dpToPx(1f));
        mLinePaint.setColor(DATA_LINE_COLOR);

        canvas.drawPath(mDataPath, mLinePaint);

    }

    /**
     * 画 x、y 轴
     *
     * @param canvas
     */
    private void drawCoordination(Canvas canvas) {

        mLinePaint.setStrokeWidth(dpToPx(1f));
        mLinePaint.setColor(COORDINATION_LINE_COLOR);

        // 画 y 轴
        canvas.drawLine(0,
                -mPositiveCount * mEachItemWidth,
                0,
                mNegativeCount * mEachItemWidth,
                mLinePaint);

        // 画 x 轴
        canvas.drawLine(0,
                0,
                GRID_INTERVAL_COUNT * mEachItemWidth,
                0,
                mLinePaint);

    }

    /**
     * 构建数据路径
     */
    private void buildDataPath() {
        mDataPath.reset();
        float width = mEachItemWidth * GRID_INTERVAL_COUNT;
        // 构建 路径，并选出最高和最低的point
        for (int i = 0; i < mLineDataList.size(); i++) {
            PointF curPoint = mLineDataList.get(i);
            if (i == 0) {
                mDataPath.moveTo(curPoint.x * width, -curPoint.y * width);
            } else {
                mDataPath.lineTo(curPoint.x * width, -curPoint.y * width);
            }
        }
    }

    /**
     * 将画布移至 原点
     */
    private void moveToTheOrigin(Canvas canvas) {

        float verHeight = mEachItemWidth * mPositiveCount;

        // 计算 横向移动距离
        float horPadding = mViewWidth - mEachItemWidth * GRID_INTERVAL_COUNT - 2 * PADDING;
        float verPadding = mViewHeight - mEachItemWidth * (mPositiveCount + mNegativeCount) - 2 * PADDING - TEXT_SIZE / 2;

        canvas.translate(horPadding / 2 + PADDING, verPadding / 2 + verHeight + PADDING);

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

    /**
     * 获取视图的宽
     *
     * @return 视图宽 - 左右的内边距
     */
    private float getViewEnableWidth() {
        return mViewWidth - PADDING * 2;
    }

    /**
     * 获取视图的高
     *
     * @return 视图高 - 上下的内边距
     */
    private float getViewEnableHeight() {
        return mViewHeight - PADDING * 2;
    }

}
