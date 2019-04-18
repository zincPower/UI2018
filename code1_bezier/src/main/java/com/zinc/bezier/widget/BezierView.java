package com.zinc.bezier.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zinc.bezier.utils.BezierUtils;
import com.zinc.bezier.activity.BezierActivity;
import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/30
 * @description
 */
public class BezierView extends BaseView {

    // 帧数：1000，即1000个点来绘制一条线
    private static final int FRAME = 1000;

    // handler 事件
    private static final int HANDLE_EVENT = 12580;

    // 准备状态
    public static final int PREPARE = 0x0001;
    // 运行状态
    public static final int RUNNING = 0x0002;
    // 停止状态
    public static final int STOP = 0x0004;

    // 默认的控制点
    private List<PointF> DEFAULT_POINT;

    // 有效触碰的范围
    private int mTouchRegionWidth;

    // 当前状态
    private int mState = PREPARE;
    // 普通线的宽度
    private int LINE_WIDTH;
    // 贝塞尔曲线的宽度
    private int BEZIER_LINE_WIDTH;
    // 控制点的半径
    private int POINT_RADIO_WIDTH;

    // 速率，每次绘制跳过的帧数，等于10，即表示每次绘制跳过10帧
    private int mRate = 10;

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
    // 绘字笔
    private Paint mTextPaint;

    // 当前的比例
    private float mCurRatio;

    // 控制点的坐标
    private List<PointF> mControlPointList;
    // 贝塞尔曲线的路径点
    private List<PointF> mBezierPointList;
    // 色值，每一阶的色值
    private List<String> mLineColor;

    private Handler mHandler;

    // 最高阶的控制点个数
    private int mPointCount;
    // 是否绘制降阶线
    private boolean mIsShowReduceOrderLine;
    // 是否循环播放
    private boolean mIsLoop;

    /**
     * 层级说明：
     * 第1层list.存放每一阶的值
     * 即：mIntermediateList.get(0) 即为第(n-1)阶的贝塞尔曲线的数值
     * mIntermediateList.get(1) 即为第(n-2)阶的贝塞尔曲线的数值
     * 第2层list.存放该阶的每条边的数据
     * 第3层list.存放这条边点的数据
     */
    private final List<List<List<PointF>>> mIntermediateList = new ArrayList<>();

    /**
     * 层级说明：
     * 第1层：边的数据
     * 第2层：边中的点数据
     */
    private final List<List<PointF>> mIntermediateDrawList = new ArrayList<>();

    // 绘制时，贝塞尔曲线的点
    private PointF mCurBezierPoint;
    // 当前选中的点
    private PointF mCurSelectPoint;

    public BezierView(Context context) {
        super(context);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置速率
     *
     * @param rate 速率
     */
    public void setRate(int rate) {
        this.mRate = rate;
    }

    /**
     * 设置阶 [2-7]
     *
     * @param order
     */
    public void setOrder(int order) {
        this.mPointCount = order + 1;
        mControlPointList.clear();
        for (int i = 0; i < mPointCount; ++i) {
            if (i >= DEFAULT_POINT.size()) {
                break;
            }
            mControlPointList.add(DEFAULT_POINT.get(i));
        }
    }

    /**
     * 设置是否显示降阶线
     *
     * @param isShowReduceOrderLine
     */
    public void setIsShowReduceOrderLine(boolean isShowReduceOrderLine) {
        this.mIsShowReduceOrderLine = isShowReduceOrderLine;
    }

    public boolean isShowReduceOrderLine() {
        return mIsShowReduceOrderLine;
    }

    /**
     * 设置是否循环播放
     *
     * @param isLoop
     */
    public void setIsLoop(boolean isLoop) {
        this.mIsLoop = isLoop;
    }

    public boolean isLoop() {
        return mIsLoop;
    }

    public int getState() {
        return mState;
    }

    public void setCurRatio(float curRatio) {
        this.mCurRatio = curRatio;
    }

    @Override
    protected void init(Context context) {
        LINE_WIDTH = dpToPx(2);
        BEZIER_LINE_WIDTH = dpToPx(3);
        POINT_RADIO_WIDTH = dpToPx(4);

        // 初始化为准备状态
        mState = PREPARE;

        mHandler = new MyHandler(this);

        // 初始化颜色
        mLineColor = new ArrayList<>();
        mLineColor.add("#f4ea2a");    //黄色
        mLineColor.add("#1afa29");    //绿色
        mLineColor.add("#13227a");    //蓝色
        mLineColor.add("#515151");    //黑色
        mLineColor.add("#e89abe");    //粉色
        mLineColor.add("#efb336");    //橙色

        int width = context.getResources().getDisplayMetrics().widthPixels;
        DEFAULT_POINT = new ArrayList<>();
        DEFAULT_POINT.add(new PointF(width / 5, width / 5));
        DEFAULT_POINT.add(new PointF(width / 3, width / 2));
        DEFAULT_POINT.add(new PointF(width / 3 * 2, width / 4));
        DEFAULT_POINT.add(new PointF(width / 2, width / 3));
        DEFAULT_POINT.add(new PointF(width / 4 * 2, width / 8));
        DEFAULT_POINT.add(new PointF(width / 5 * 4, width / 12));
        DEFAULT_POINT.add(new PointF(width / 5 * 4, width));
        DEFAULT_POINT.add(new PointF(width / 2, width));

        // 初始化 控制点
        mControlPointList = new ArrayList<>();
        mPointCount = 8;
        for (int i = 0; i < mPointCount; ++i) {
            if (i >= DEFAULT_POINT.size()) {
                break;
            }
            mControlPointList.add(DEFAULT_POINT.get(i));
        }

        // 初始化贝塞尔的路径的画笔
        mBezierPaint = new Paint();
        mBezierPaint.setAntiAlias(true);
        mBezierPaint.setColor(getBezierLineColor());
        mBezierPaint.setStrokeWidth(BEZIER_LINE_WIDTH);
        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setStrokeCap(Paint.Cap.ROUND);

        // 初始 控制点画笔
        mControlPaint = new Paint();
        mControlPaint.setAntiAlias(true);
        mControlPaint.setColor(getControlLineColor());
        mControlPaint.setStrokeWidth(LINE_WIDTH);

        // 初始化 端点画笔
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(spToPx(15));

        // 初始化中间阶级的画笔
        mIntermediatePaint = new Paint();
        mIntermediatePaint.setAntiAlias(true);
        mIntermediatePaint.setStrokeWidth(LINE_WIDTH);

        // 初始化存放贝塞尔曲线最终结果的路径
        mBezierPath = new Path();

        // 触碰范围
        mTouchRegionWidth = dpToPx(20);

    }


    public void start() {

        // 重置 贝塞尔曲线结果 的路径
        mBezierPath.reset();

        // 状态至为运行中
        mState = RUNNING;

        // 计算 贝塞尔曲线结果 的每个点
        mBezierPointList = BezierUtils.buildBezierPoint(mControlPointList, FRAME);
        // 将计算好的 贝塞尔曲线的点 组装成路径
        prepareBezierPath();

        if (mIsShowReduceOrderLine) {
            // 计算 中间阶级的控制点
            BezierUtils.calculateIntermediateLine(mIntermediateList, mControlPointList, FRAME);
        }

        mCurRatio = 0;

        setCurBezierPoint(mBezierPointList.get(0));

        invalidate();

    }

    /**
     * 暂停 或 继续
     */
    public void pause() {
        if (mState == RUNNING) {
            mState = STOP;
        } else if (mState == STOP) {
            mState = RUNNING;
            mHandler.sendEmptyMessage(HANDLE_EVENT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画坐标和网格
        drawCoordinate(canvas);

        // 绘制控制基线和点
        drawControlLine(canvas);

        // 绘制贝塞尔曲线
        canvas.drawPath(mBezierPath, mBezierPaint);

        if (mState != PREPARE) {
            mPointPaint.setStyle(Paint.Style.FILL);
            if (mIsShowReduceOrderLine) {
                // 画中间阶层的线
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
            }

            mPointPaint.setColor(getBezierLineColor());
            canvas.drawCircle(mCurBezierPoint.x,
                    mCurBezierPoint.y,
                    POINT_RADIO_WIDTH,
                    mPointPaint);

            mHandler.sendEmptyMessage(HANDLE_EVENT);

        }

        if (mCurRatio == 1 && !mIsLoop && getContext() instanceof BezierActivity) {
            ((BezierActivity) getContext()).resetPlayBtn();
        }

        canvas.drawText("u = " + mCurRatio, mWidth / 4, mHeight * 11 / 12, mTextPaint);

    }

    /**
     * 绘制 控制基线 和 点
     */
    private void drawControlLine(Canvas canvas) {
        mPointPaint.setColor(getControlLineColor());

        // 绘制 控制点
        for (PointF point : mControlPointList) {
            mPointPaint.setStyle(Paint.Style.FILL);
            mPointPaint.setStrokeWidth(0);
            canvas.drawCircle(point.x, point.y, POINT_RADIO_WIDTH, mPointPaint);

            mPointPaint.setStyle(Paint.Style.STROKE);
            mPointPaint.setStrokeWidth(1);
            canvas.drawCircle(point.x, point.y, POINT_RADIO_WIDTH + 2, mPointPaint);
        }

        // 绘制第 n 阶的控制基线
        for (int i = 0; i < mControlPointList.size() - 1; ++i) {
            canvas.drawLine(mControlPointList.get(i).x,
                    mControlPointList.get(i).y,
                    mControlPointList.get(i + 1).x,
                    mControlPointList.get(i + 1).y,
                    mControlPaint);
        }
    }

    /**
     * 将计算好的 贝塞尔曲线的点 组装成路径
     * 至于这路径中有多少个点，取决于{@link #FRAME}属性的值
     */
    private void prepareBezierPath() {
        for (int i = 0; i < mBezierPointList.size(); ++i) {
            PointF point = mBezierPointList.get(i);
            if (i == 0) {
                mBezierPath.moveTo(point.x, point.y);
            } else {
                mBezierPath.lineTo(point.x, point.y);
            }
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

                mIntermediateList.clear();
                mIntermediateDrawList.clear();
                if (mBezierPointList != null) {
                    mBezierPointList.clear();
                }
                mBezierPath.reset();

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
                RectF pointRange = new RectF(controlPoint.x - mTouchRegionWidth,
                        controlPoint.y - mTouchRegionWidth,
                        controlPoint.x + mTouchRegionWidth,
                        controlPoint.y + mTouchRegionWidth);

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
     * 设置绘制值
     *
     * @param intermediateDrawList
     */
    private void setIntermediateDrawList(List<List<PointF>> intermediateDrawList) {
        mIntermediateDrawList.clear();
        mIntermediateDrawList.addAll(intermediateDrawList);
    }

    private void setState(int state) {
        this.mState = state;
    }

    private int getRate() {
        return mRate;
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

                // 按了 暂停，则不在进行
                if (mView.getState() == STOP) {
                    return;
                }

                // 增加 帧数，让线移动起来
                mCurFrame += mView.getRate();
                // 当帧数超出界限则不在运行，让当前 帧数 和 状态复位，并且清空降阶线的数据
                if (mCurFrame >= mView.getSize()) {

                    mCurFrame = 0;

                    if (!mView.isLoop()) {
                        mView.setState(PREPARE);
                        mView.setIntermediateDrawList(new ArrayList<List<PointF>>());
                        mView.setCurRatio(1);
                        mView.invalidate();
                        return;
                    }

                }

                // 获取当前的贝塞尔曲线点
                List<PointF> bezierPointList = mView.getBezierPointList();
                mView.setCurBezierPoint(bezierPointList.get(mCurFrame));

                // 是否要显示 降阶线
                if (mView.isShowReduceOrderLine()) {
                    List<List<List<PointF>>> intermediateList = mView.getIntermediateList();

                    // 实时变动的线
                    List<List<PointF>> intermediateDrawList = new ArrayList<>();

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
                }


//                if (mCurFrame >= mView.getSize() - 1 && !mView.isLoop()) {
//                    mCurFrame = 0;
//                    mView.setState(PREPARE);
//                    mView.setIntermediateDrawList(new ArrayList<List<PointF>>());
//                }

                float ratio = (int) (((float) mCurFrame / mView.getSize()) * 100) / 100f;
                mView.setCurRatio(ratio > 1 ? 1 : ratio);

                // 刷新视图
                mView.invalidate();

            }

        }
    }

}
