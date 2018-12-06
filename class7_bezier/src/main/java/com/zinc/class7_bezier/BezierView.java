package com.zinc.class7_bezier;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/30
 * @description
 */
public class BezierView extends View {

    // 最高支持阶数（可以自行增加）
    private static final int MAX_ORDER = 7;
    private static final int X_TYPE = 1;
    private static final int Y_TYPE = 2;
    // 有效触碰的范围
    private static final int TOUCH_REGION_WIDTH = 30;

    // 帧数：1000，即1000个点来绘制一条线
    private static final int FRAME = 1000;

    // handler 事件
    private static final int HANDLE_EVENT = 12580;

    // 准备状态
    private static final int PREPARE = 0x0001;
    // 运行状态
    private static final int RUNNING = 0x0002;
    // 停止状态
    private static final int STOP = 0x0004;

    // 当前状态
    private int mState = 0;
    // 普通线的宽度
    private int LINE_WIDTH;
    // 贝塞尔曲线的宽度
    private int BEZIER_LINE_WIDTH;
    // 控制点的半径
    private int POINT_RADIO_WIDTH;

    // 速率，每次绘制跳过的帧数，等于10，即表示每次绘制跳过10帧
    private int rate = 10;

    // 绘制贝塞尔曲线的画笔
    private Paint mBezierPaint;
    // 贝塞尔曲线的路径
    private Path mBezierPath;
    // 控制点的画笔
    private Paint mControlPaint;
    // 绘制端点的画笔
    private Paint mPointPaint;
    // 中间阶层的线画笔
    private Paint mIntermediatePaint;

    // 控制点的坐标
    private List<PointF> mControlPointList;
    // 贝塞尔曲线的路径点
    private List<PointF> mBezierPointList;
    // 色值，每一阶的色值
    private List<String> mLineColor = new ArrayList<>();

    private Handler mHandler;

    /**
     * 层级说明：
     * 第1层list.存放每一阶的值
     * 即：mOrderPointList.get(0) 即为第n阶的贝塞尔曲线的数值
     * mOrderPointList.get(1) 即为第(n-1)阶的贝塞尔曲线的数值
     * 第2层list.存放该阶的每条边的数据
     * 第3层list.存放这条边点的数据
     */
    private List<List<List<PointF>>> mIntermediateList = new ArrayList<>();

    /**
     * 层级说明：
     * 第1层：边的数据
     * 第2层：边中的点数据
     */
    private List<List<PointF>> mIntermediateDrawList = new ArrayList<>();

    // 绘制时，贝塞尔曲线的点
    private PointF mCurBezierPoint;
    // 当前选中的点
    private PointF mCurSelectPoint;

    public BezierView(Context context) {
        super(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LINE_WIDTH = dpToPx(2);
        BEZIER_LINE_WIDTH = dpToPx(3);
        POINT_RADIO_WIDTH = dpToPx(4);

        // 初始化为准备状态
        mState |= PREPARE;

        mHandler = new MyHandler(this);

        // 初始化颜色
        mLineColor.add("#f4ea2a");    //黄色
        mLineColor.add("#1afa29");    //绿色
        mLineColor.add("#13227a");    //蓝色
        mLineColor.add("#515151");    //黑色
        mLineColor.add("#e89abe");    //粉色
        mLineColor.add("#efb336");    //橙色

        // 初始化 控制点
        int width = context.getResources().getDisplayMetrics().widthPixels;
        mControlPointList = new ArrayList<>();
        mControlPointList.add(new PointF(width / 5, width / 5));
        mControlPointList.add(new PointF(width / 3, width / 2));
        mControlPointList.add(new PointF(width / 3 * 2, width / 4));
        mControlPointList.add(new PointF(width / 2, width / 3));
        mControlPointList.add(new PointF(width / 4 * 2, width / 8));
        mControlPointList.add(new PointF(width / 5 * 4, width / 12));

        // 初始化贝塞尔的路径的画笔
        mBezierPaint = new Paint();
        mBezierPaint.setAntiAlias(true);
        mBezierPaint.setColor(getBezierLineColor());
        mBezierPaint.setStrokeWidth(BEZIER_LINE_WIDTH);
        mBezierPaint.setStyle(Paint.Style.STROKE);

        // 初始 控制点画笔
        mControlPaint = new Paint();
        mControlPaint.setAntiAlias(true);
        mControlPaint.setColor(getControlLineColor());
        mControlPaint.setStrokeWidth(LINE_WIDTH);

        // 初始化 端点画笔
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mIntermediatePaint = new Paint();
        mIntermediatePaint.setAntiAlias(true);
        mIntermediatePaint.setStrokeWidth(LINE_WIDTH);

        mBezierPath = new Path();
//        mBezierPointList = buildBezierPoint();
//        prepareBezierPath();

    }



    public void start() {

        mBezierPath.reset();

        mState = RUNNING;

        mBezierPointList = buildBezierPoint();
        prepareBezierPath();

        calculateIntermediateLine();
        setCurBezierPoint(mBezierPointList.get(0));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制控制基线和点
        drawControlLine(canvas);

        // 绘制贝塞尔曲线
        canvas.drawPath(mBezierPath, mBezierPaint);

        if (mState == RUNNING) {
            // 画中间阶层的线
            mPointPaint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < mIntermediateDrawList.size(); ++i) {

                List<PointF> lineList = mIntermediateDrawList.get(i);
                mIntermediatePaint.setColor(getColor(i));
                mPointPaint.setColor(getColor(i));

                for (int j = 0; j < lineList.size() - 1; ++j) {

                    // 画线
                    canvas.drawLine(lineList.get(j).x,
                            lineList.get(j).y,
                            lineList.get(j + 1).x,
                            lineList.get(j + 1).y,
                            mIntermediatePaint);

                    // 画点
                    canvas.drawCircle(lineList.get(j).x,
                            lineList.get(j).y,
                            POINT_RADIO_WIDTH,
                            mPointPaint);
                }

                canvas.drawCircle(lineList.get(lineList.size() - 1).x,
                        lineList.get(lineList.size() - 1).y,
                        POINT_RADIO_WIDTH,
                        mPointPaint);
            }

            mPointPaint.setColor(getBezierLineColor());
            canvas.drawCircle(mCurBezierPoint.x,
                    mCurBezierPoint.y,
                    POINT_RADIO_WIDTH,
                    mPointPaint);

            mHandler.sendEmptyMessage(HANDLE_EVENT);

        }

    }

    /**
     * 绘制 控制基线
     */
    private void drawControlLine(Canvas canvas) {
        mPointPaint.setColor(getControlLineColor());

        for (PointF point : mControlPointList) {
            mPointPaint.setStyle(Paint.Style.FILL);
            mPointPaint.setStrokeWidth(0);
            canvas.drawCircle(point.x, point.y, POINT_RADIO_WIDTH, mPointPaint);

            mPointPaint.setStyle(Paint.Style.STROKE);
            mPointPaint.setStrokeWidth(1);
            canvas.drawCircle(point.x, point.y, POINT_RADIO_WIDTH + 2, mPointPaint);
        }

        for (int i = 0; i < mControlPointList.size() - 1; ++i) {
            canvas.drawLine(mControlPointList.get(i).x,
                    mControlPointList.get(i).y,
                    mControlPointList.get(i + 1).x,
                    mControlPointList.get(i + 1).y,
                    mControlPaint);
        }
    }

    /**
     * 计算中间降阶过程的绘制点
     */
    private void calculateIntermediateLine() {
        mIntermediateList.clear();

        // 获取阶数
        int order = mControlPointList.size() - 1;
        // 每次增加的偏量
        float delta = 1f / FRAME;

        // i 的取值范围必须为 [0, order-1]，
        // order-1是因为n阶的贝塞尔曲线只需要计算n-1次就可以，
        // 因为最高阶不需要计算，已经直接绘制
        for (int i = 0; i < order - 1; ++i) {

            List<List<PointF>> orderPointList = new ArrayList<>();

            // 终止条件为每一阶的边的条数，阶数与边数相等
            // 随着i的增大，即阶数的降低，相应的需要计算的边数对应减少
            for (int j = 0; j < order - i; ++j) {

                List<PointF> pointList = new ArrayList<>();

                // 计算每个偏移量的点
                for (float u = 0; u <= 1; u += delta) {

                    /**
                     *  p1(x,y)◉--------○----------------◉p2(x,y)
                     *            u    p0(x,y)
                     */
                    float p1x;
                    float p1y;
                    float p2x;
                    float p2y;

                    // 上一阶中，对应的当前帧的下标
                    int beforeOrderCurPointIndex = (int) (u * FRAME);

                    // 当 mIntermediateList==0 时，说明要依赖于此时需要依赖于控制基线绘制
                    if (mIntermediateList.size() == 0) {
                        p1x = mControlPointList.get(j).x;
                        p1y = mControlPointList.get(j).y;
                        p2x = mControlPointList.get(j + 1).x;
                        p2y = mControlPointList.get(j + 1).y;
                    } else {
                        p1x = mIntermediateList.get(i - 1).get(j).get(beforeOrderCurPointIndex).x;
                        p1y = mIntermediateList.get(i - 1).get(j).get(beforeOrderCurPointIndex).y;
                        p2x = mIntermediateList.get(i - 1).get(j + 1).get(beforeOrderCurPointIndex).x;
                        p2y = mIntermediateList.get(i - 1).get(j + 1).get(beforeOrderCurPointIndex).y;
                    }

                    /**
                     * 这里的公式 和{@link #calculatePointCoordinate(int, float, int, int)} 的原理是一样的，
                     * 只是不用递归，直接计算当前阶数的点即可
                     */
                    float p0x = (1 - u) * p1x + u * p2x;
                    float p0y = (1 - u) * p1y + u * p2y;

                    pointList.add(new PointF(p0x, p0y));

                }

                orderPointList.add(pointList);

            }

            mIntermediateList.add(orderPointList);

        }
    }

    /**
     * 构建贝塞尔曲线，具体点数由 {@link #FRAME} 决定
     *
     * @return
     */
    private List<PointF> buildBezierPoint() {
        List<PointF> pointList = new ArrayList<>();

        // 此处注意，要用1f，否则得出结果为0
        float delta = 1f / FRAME;

        // 阶数，阶数=绘制点数-1
        int order = mControlPointList.size() - 1;

        // 循环递增
        for (float u = 0; u <= 1; u += delta) {
            pointList.add(new PointF(calculatePointCoordinate(X_TYPE, u, order, 0),
                    calculatePointCoordinate(Y_TYPE, u, order, 0)));
        }

        return pointList;

    }

    private void prepareBezierPath(){
        for (int i = 0; i < mBezierPointList.size(); ++i) {
            PointF point = mBezierPointList.get(i);
            if (i == 0) {
                mBezierPath.moveTo(point.x, point.y);
            } else {
                mBezierPath.lineTo(point.x, point.y);
            }
        }
    }

    /**
     * 计算坐标 [贝塞尔曲线的核心关键]
     *
     * @param type {@link #X_TYPE} 表示x轴的坐标， {@link #Y_TYPE} 表示y轴的坐标
     * @param u    当前的比例
     * @param k    阶数
     * @param p    当前坐标（具体为 x轴 或 y轴）
     * @return
     */
    private float calculatePointCoordinate(@IntRange(from = X_TYPE, to = Y_TYPE) int type,
                                           float u,
                                           int k,
                                           int p) {

        /**
         * 公式解说：（p表示坐标点，后面的数字只是区分）
         * 场景：有一条线p1到p2，p0在中间，求p0的坐标
         *      p1◉--------○----------------◉p2
         *            u    p0
         *
         * 公式：p0 = p1+u*(p2-p1) 整理得出 p0 = (1-u)*p1+u*p2
         */
        // 一阶贝塞尔，直接返回
        if (k == 1) {

            float p1;
            float p2;

            // 根据是 x轴 还是 y轴 进行赋值
            if (type == X_TYPE) {
                p1 = mControlPointList.get(p).x;
                p2 = mControlPointList.get(p + 1).x;
            } else {
                p1 = mControlPointList.get(p).y;
                p2 = mControlPointList.get(p + 1).y;
            }

            return (1 - u) * p1 + u * p2;

        } else {

            /**
             * 这里应用了递归的思想：
             * 1阶贝塞尔曲线的端点 依赖于 2阶贝塞尔曲线
             * 2阶贝塞尔曲线的端点 依赖于 3阶贝塞尔曲线
             * ....
             * n-1阶贝塞尔曲线的端点 依赖于 n阶贝塞尔曲线
             *
             * 1阶贝塞尔曲线 则为 真正的贝塞尔曲线存在的点
             */
            return (1 - u) * calculatePointCoordinate(type, u, k - 1, p)
                    + u * calculatePointCoordinate(type, u, k - 1, p + 1);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 没有在准备状态不能进行操作
        if (mState != PREPARE) {
            return true;
        }

        // 触碰的坐标
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isLegalControlPoint(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mCurSelectPoint == null) {
                    return true;
                }

                mCurSelectPoint.x = x;
                mCurSelectPoint.y = y;
                invalidate();

                break;

            case MotionEvent.ACTION_UP:
                mCurSelectPoint = null;
                break;
        }

        return true;
    }

    /**
     * 获取 触碰点 范围内有效的 控制点
     *
     * @param x
     * @param y
     */
    private void isLegalControlPoint(float x, float y) {

        if (mCurSelectPoint == null) {

            for (PointF controlPoint : mControlPointList) {
                RectF pointRange = new RectF(controlPoint.x - TOUCH_REGION_WIDTH,
                        controlPoint.y - TOUCH_REGION_WIDTH,
                        controlPoint.x + TOUCH_REGION_WIDTH,
                        controlPoint.y + TOUCH_REGION_WIDTH);

                // 如果包含了就，返回true
                if (pointRange.contains(x, y)) {
                    mCurSelectPoint = controlPoint;
                    return;
                }

            }
        }

    }

    /**
     * 获取控制线、控制点的色值
     *
     * @return
     */
    private int getControlLineColor() {
        return getColor("#1296db");
    }

    /**
     * 获取贝塞尔曲线的色值
     *
     * @return
     */
    private int getBezierLineColor() {
        return getColor("#d81e06");
    }

    /**
     * 获取 {@link #mLineColor} 的对应下标色值，如果越界则取余
     *
     * @param index 色值下标
     * @return
     */
    private int getColor(int index) {
        return getColor(mLineColor.get(index % mLineColor.size()));
    }

    /**
     * 获取颜色
     *
     * @param color 色值，格式：#xxxxxx
     * @return
     */
    private int getColor(String color) {
        return Color.parseColor(color);
    }

    /**
     * 转换 dp 至 px
     *
     * @param dp dp像素
     * @return
     */
    private int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }

    /**
     * 设置绘制值
     *
     * @param intermediateDrawList
     */
    private void setIntermediateDrawList(List<List<PointF>> intermediateDrawList) {
        mIntermediateDrawList = intermediateDrawList;
    }

    private int getState() {
        return mState;
    }

    private void setState(int state) {
        this.mState = state;
    }

    private int getRate() {
        return rate;
    }

    private int getSize() {
        return mBezierPointList.size();
    }

    private void setCurBezierPoint(PointF curBezierPoint) {
        this.mCurBezierPoint = curBezierPoint;
    }

    private List<PointF> getBezierPointList() {
        return mBezierPointList;
    }

    private List<List<List<PointF>>> getIntermediateList() {
        return mIntermediateList;
    }

    private static final class MyHandler extends Handler {

        // 贝塞尔曲线的视图
        private final BezierView mView;
        // 当前帧数
        private int mCurFrame;

        public MyHandler(BezierView bezierView) {
            this.mView = bezierView;
            this.mCurFrame = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLE_EVENT) {

                mCurFrame += mView.getRate();
                if (mCurFrame > mView.getSize()) {
                    mCurFrame = 0;
                    mView.setState(PREPARE);
                    return;
                }

                // 实时变动的线
                List<List<PointF>> intermediateDrawList = new ArrayList<>();

                // 获取当前的贝塞尔曲线点
                List<PointF> bezierPointList = mView.getBezierPointList();
                mView.setCurBezierPoint(bezierPointList.get(mCurFrame));

                List<List<List<PointF>>> intermediateList = mView.getIntermediateList();

                for (int i = 0; i < intermediateList.size(); ++i) {

                    List<List<PointF>> lineList = intermediateList.get(i);
                    List<PointF> intermediatePoint = new ArrayList<>();

                    for (int j = 0; j < lineList.size(); ++j) {
                        float x = lineList.get(j).get(mCurFrame).x;
                        float y = lineList.get(j).get(mCurFrame).y;
                        intermediatePoint.add(new PointF(x, y));
                    }

                    intermediateDrawList.add(intermediatePoint);

                }

                mView.setIntermediateDrawList(intermediateDrawList);

                if (mCurFrame >= mView.getSize() - 1) {
                    mView.setState(STOP);
                }

                // 刷新视图
                mView.invalidate();

            }

        }
    }

}
