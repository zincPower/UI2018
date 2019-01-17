package com.zinc.class12_animation.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/15
 * @description 雷达图
 */
public class RadarChartView extends BaseView {

    // 默认6维度
    private static final int DEFAULT_DIMEN_COUNT = 6;
    // 360度
    private static final double CIRCLE_ANGLE = 360d;
    // 网格线的默认颜色
    private static final String DEFAULT_LINE_COLOR = "#7a7a7a";
    // 默认中心点
    private static final PointF DEFAULT_CENTER_POINT = new PointF(0, 0);
    // 雷达图 背景渐变 分割的层级
    private static final int RADAR_BG_LEVEL = 10;
    // 雷达图 背景渐变 显示的层级
    private static final int RADAR_BG_SHOW_LEVEL = 6;
    // 背景 阶梯 透明
    private static final int BG_ALPHA_LEVEL = 10;
    // 一个纬度的动画持续时间
    private static final int DURATION = 300;

    // 初始化状态
    private static final int INIT = 0x001;
    // 运行状态
    private static final int RUNNING = 0x002;

    // 边框宽度
    private float RADAR_BORDER_LINE_WIDTH = dpToPx(1.5f);
    // 维度分割线
    private float RADAR_DIMEN_LINE_WIDTH = dpToPx(1f);

    // 纬度数
    private int mDimenCount;
    // 每个纬度的线长
    private float mLength;
    // 每个纬度角度
    private double mAngle;

    private List<PointF> mVertexList;

    // 雷达图 线的画笔
    private Paint mLinePaint;
    // 雷达图 背景画笔
    private Paint mRadarBgPaint;
    // 绘制数据的画笔
    private Paint mDataPaint;
    // 绘制文字的画笔
    private Paint mTextPaint;

    // 雷达图 边框路径
    private Path mRadarLinePath;
    // 雷达图 维度分割线
    private Path mDimensionPath;
    // 雷达图 背景路径
    private Path mRadarBgPath;
    // 雷达图 正在运动的路径
    private Path mRunningPath;

    // 雷达图的中心点
    private PointF mRadarCenterPoint;

    // 雷达图数据
    private List<Data> mDataList;
    // 雷达图基础数据
    private List<Data> mBaseDataList;

    // 雷达图数据路径
    private List<Path> mDataPathList;
    // 雷达图基础数据路径
    private List<Path> mBaseDataPathList;

    // 插值器
    private ValueAnimator mAnimator;
    // 当前的插值器值
    private float mAnimCurValue;
    // 总共需要的循环数
    private int mTotalLoopCount;
    // 当前需要的循环数
    private int mCurLoopCount;

    // 当前动画状态
    private int mCurState;

    public RadarChartView(Context context) {
        super(context);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {

        mVertexList = new ArrayList<>();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.parseColor(DEFAULT_LINE_COLOR));
        // 连线圆角
        mLinePaint.setPathEffect(new CornerPathEffect(dpToPx(2.5f)));

        mRadarBgPaint = new Paint();
        mRadarBgPaint.setAntiAlias(true);
        mRadarBgPaint.setStyle(Paint.Style.FILL);
        mRadarBgPaint.setPathEffect(new CornerPathEffect(dpToPx(2.5f)));

        mDataPaint = new Paint();
        mDataPaint.setAntiAlias(true);
        mDataPaint.setStrokeWidth(dpToPx(1f));
        mDataPaint.setPathEffect(new CornerPathEffect(dpToPx(1f)));

        mRadarLinePath = new Path();

        mDimensionPath = new Path();

        mRadarBgPath = new Path();

        mRunningPath = new Path();

        mDataList = new ArrayList<>();
        mBaseDataList = new ArrayList<>();

        mDataPathList = new ArrayList<>();
        mBaseDataPathList = new ArrayList<>();

        mRadarCenterPoint = DEFAULT_CENTER_POINT;

        this.mDimenCount = DEFAULT_DIMEN_COUNT;
        mAngle = CIRCLE_ANGLE / mDimenCount;

    }

    /**
     * 设置维度
     *
     * @param dimenCount
     */
    public void setDimenCount(int dimenCount) {

        if (dimenCount < 3) {
            Log.w(TAG, "Dimension is must be bigger than two.");
            dimenCount = 3;
        }

        this.mDimenCount = dimenCount;
        mAngle = CIRCLE_ANGLE / mDimenCount;

        calculateRadarChartVertex();
        initRadarLine();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mLength = Math.min(w, h) / 3;

        calculateRadarChartVertex();
        initRadarLine();
    }

    /**
     * 设置数据
     *
     * @param dataList
     */
    public void setDataList(List<Data> dataList) {
        this.mDataList.clear();
        this.mDataPathList.clear();
        this.mDataList.addAll(dataList);
    }

    /**
     * 设置基本数据
     *
     * @param baseDataList
     */
    public void setBaseDataList(List<Data> baseDataList) {
        this.mBaseDataList.clear();
        this.mBaseDataPathList.clear();
        this.mBaseDataList.addAll(baseDataList);
    }

    /**
     * 检测数据中的 {@link Data#data} 长度是否和 {@link #mDimenCount} 相同
     *
     * @param dataList 数据
     */
    private void checkData(List<Data> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getData().size() != mDimenCount) {
                throw new RuntimeException("The Data size is not equal to dimension count.");
            }
        }

    }

    /**
     * 开始绘制
     */
    public void start() {

        mCurState = RUNNING;

        checkData(mBaseDataList);
        checkData(mDataList);

        // 将当前循环数置为 0
        mCurLoopCount = 0;

        // 计算数据
        calculateDataVertex(true, 1);
        calculateDataVertex(false, 1);

        // 第一个维度不需要展开
        mTotalLoopCount = (mDimenCount - 1) * mDataList.size();
        mAnimator = ValueAnimator.ofFloat(0f, mTotalLoopCount);
        mAnimator.setDuration(DURATION * mTotalLoopCount);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();

                mCurLoopCount = (int) value;

                mAnimCurValue = value - mCurLoopCount;

                invalidate();
            }
        });

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurState = INIT;
                invalidate();
            }
        });

        mAnimator.start();
    }

    public void stop() {

        mAnimator.cancel();

    }

    /**
     * 计算数据的顶点坐标
     *
     * @param isBase  是否为 基础数据
     * @param process 进度 估值器的数值，范围[0-1]
     */
    private void calculateDataVertex(boolean isBase, float process) {

        List<Data> calDataList = isBase ? mBaseDataList : mDataList;
        List<Path> calPathList = isBase ? mBaseDataPathList : mDataPathList;

        for (int i = 0; i < calDataList.size(); ++i) {

            List<Float> pointDataList = calDataList.get(i).getData();

            Path curPath;
            if (i + 1 > calPathList.size()) {
                curPath = new Path();
                calPathList.add(curPath);
            } else {
                curPath = calPathList.get(i);
            }

            curPath.reset();
            for (int j = 0; j < pointDataList.size(); ++j) {

                // 当前维度的数据比例
                float ratio = pointDataList.get(j);
                // 当前维度的顶点坐标
                PointF curDimenPoint = mVertexList.get(j);

                if (j == 0) {
                    curPath.moveTo(curDimenPoint.x * ratio * process,
                            curDimenPoint.y * ratio * process);
                } else {
                    curPath.lineTo(curDimenPoint.x * ratio * process,
                            curDimenPoint.y * ratio * process);
                }

            }
            curPath.close();

        }

    }

    /**
     * 计算雷达图的顶点
     */
    private void calculateRadarChartVertex() {

        // 清除之前顶点
        mVertexList.clear();

        // 循环遍历计算顶点坐标
        for (int i = 0; i < mDimenCount; ++i) {

            PointF point = new PointF();

            // 当前角度
            double curAngle = i * mAngle;
            // 转弧度制
            double radian = Math.toRadians(curAngle);

            // 计算其 x、y 的坐标
            // y轴需要进行取反，因为canvas的坐标轴和我们数学中的坐标轴的y轴正好是上下相反的
            point.x = (float) (mLength * Math.sin(radian));
            point.y = (float) -(mLength * Math.cos(radian));

            mVertexList.add(point);
        }

    }

    /**
     * 初始化 雷达图 外边框和维度分割线
     */
    private void initRadarLine() {
        // 先清空
        mRadarLinePath.reset();
        mDimensionPath.reset();

        // 画 外边框
        for (int i = 0; i < mVertexList.size(); ++i) {
            if (i == 0) {
                mRadarLinePath.moveTo(mVertexList.get(i).x, mVertexList.get(i).y);
            } else {
                mRadarLinePath.lineTo(mVertexList.get(i).x, mVertexList.get(i).y);
            }
        }
        mRadarLinePath.close();

        // 维度分割线
        for (int i = 0; i < mVertexList.size(); ++i) {
            mDimensionPath.moveTo(mVertexList.get(i).x, mVertexList.get(i).y);
            mDimensionPath.lineTo(mRadarCenterPoint.x, mRadarCenterPoint.y);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 画背景
        canvas.drawColor(Color.parseColor("#201F23"));

        // 平移画布至中心
        canvas.translate(mWidth / 2, mHeight / 2);

        // 画雷达 框和维度线
        drawRadarLine(canvas);
        // 画雷达 背景
        drawRadarBackground(canvas);

        // 画基线数据
        drawData(canvas, true);

        if (mCurState == INIT) {
            drawData(canvas, false);
        } else {
            drawRunningData(canvas);
        }

    }

    /**
     * 绘制运动中的数据
     *
     * @param canvas
     */
    private void drawRunningData(Canvas canvas) {

        // 数据为空 则不绘制
        if (mDataList.size() <= 0) {
            return;
        }

        // 当前数据的下标（-1因为第一个维度不用动画）
        int curIndex = mCurLoopCount / (mDimenCount - 1);
        // 当前数据的维度（-1因为第一个维度不用动画）
        int curDimen = (mCurLoopCount % (mDimenCount - 1)) + 1;

        for (int i = 0; i <= curIndex; ++i) {

            Path path;

            // 当前对比的数据
            Data curData = mDataList.get(i);

            // 当前需要进行运动展开的对比
            if (i == curIndex) {
                // 重制运动中的路径
                mRunningPath.reset();

                // 第一维度 的 顶点是固定的
                mRunningPath.moveTo(curData.getData().get(0) * mVertexList.get(0).x,
                        curData.getData().get(0) * mVertexList.get(0).y);

                // 绘制 2-curDimen 维度
                for (int j = 1; j <= curDimen; ++j) {

                    // 当前维度的对比数据 所占该维度的比例
                    Float curDimenRatio = curData.getData().get(j);

                    // 当前维度的顶点坐标
                    PointF curDimenVertexPoint = mVertexList.get(j);

                    float x = curDimenVertexPoint.x * curDimenRatio;
                    float y = curDimenVertexPoint.y * curDimenRatio;

                    if (j == curDimen) {
                        // 绘制正在移动的点
                        mRunningPath.lineTo(x * mAnimCurValue, y * mAnimCurValue);
//                        Log.i(TAG, "drawRunningData: [count: " + mCurLoopCount + "; v:" + mAnimCurValue + "]");
                    } else {
                        // 绘制已经固定的点
                        mRunningPath.lineTo(x, y);
                    }
                }

                // 不是最后的点则还需连接原点
                if (curDimen != mDimenCount - 1) {
                    mRunningPath.lineTo(mRadarCenterPoint.x, mRadarCenterPoint.y);
                }

                mRunningPath.close();

                path = mRunningPath;

            } else {
                path = mDataPathList.get(i);
            }

            // 画轮廓
            mDataPaint.setStyle(Paint.Style.STROKE);
            mDataPaint.setColor(curData.getColor());
            canvas.drawPath(path, mDataPaint);

            // 画背景
            mDataPaint.setStyle(Paint.Style.FILL);
            mDataPaint.setColor(getAlphaColor(curData.getColor(), 127));
            canvas.drawPath(path, mDataPaint);


        }

    }

    /**
     * 画背景
     */
    private void drawRadarBackground(Canvas canvas) {

        if (RADAR_BG_SHOW_LEVEL > RADAR_BG_LEVEL) {
            throw new RuntimeException("RADAR_BG_SHOW_LEVEL can not bigger than RADAR_BG_LEVEL.");
        }

        for (int i = 0; i < RADAR_BG_SHOW_LEVEL; ++i) {

            mRadarBgPath.reset();
            for (int j = 0; j < mVertexList.size(); ++j) {

                PointF curVertexPoint = mVertexList.get(j);

                float x = curVertexPoint.x * (RADAR_BG_LEVEL - i) / RADAR_BG_LEVEL;
                float y = curVertexPoint.y * (RADAR_BG_LEVEL - i) / RADAR_BG_LEVEL;

                if (j == 0) {
                    mRadarBgPath.moveTo(x, y);
                } else {
                    mRadarBgPath.lineTo(x, y);
                }

            }
            mRadarBgPath.close();

            mRadarBgPaint.setColor(getAlphaColor(Color.parseColor(DEFAULT_LINE_COLOR),
                    i * BG_ALPHA_LEVEL));

            canvas.drawPath(mRadarBgPath, mRadarBgPaint);

        }

    }

    /**
     * 绘制雷达图的网格线
     */
    private void drawRadarLine(Canvas canvas) {

        // 绘制雷达图边框
        mLinePaint.setStrokeWidth(RADAR_BORDER_LINE_WIDTH);
        canvas.drawPath(mRadarLinePath, mLinePaint);

        // 绘制雷达图维度分割线
        mLinePaint.setStrokeWidth(RADAR_DIMEN_LINE_WIDTH);
        canvas.drawPath(mDimensionPath, mLinePaint);

    }

    /**
     * 绘制基线数据
     *
     * @param canvas 画布
     * @param isBase 是否为基线
     */
    private void drawData(Canvas canvas, boolean isBase) {

        List<Path> pathList = isBase ? mBaseDataPathList : mDataPathList;
        List<Data> dataList = isBase ? mBaseDataList : mDataList;

        for (int i = 0; i < pathList.size(); ++i) {
            int color = dataList.get(i).getColor();

            mDataPaint.setColor(color);
            mDataPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(pathList.get(i), mDataPaint);

            if (!isBase) {
                mDataPaint.setStyle(Paint.Style.FILL);
                mDataPaint.setColor(getAlphaColor(color, 127));
                canvas.drawPath(pathList.get(i), mDataPaint);
            }

        }

    }

    /**
     * 给颜色加透明度
     *
     * @param color 颜色
     * @param alpha 透明
     * @return 加了透明度的颜色
     */
    protected int getAlphaColor(int color, int alpha) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        color = Color.HSVToColor(alpha, hsv);
        return color;
    }

    /**
     * @author Jiang zinc
     * @date 创建时间：2019/1/16
     * @description 雷达图数据
     */
    public static class Data {

        /**
         * 雷达图的数据，数据范围 [0-1]
         * 低于0，处理为0
         * 大于1，处理为1
         * 数据长度，要和雷达图维度相同
         */
        private List<Float> data;
        /**
         * 数据色值
         */
        private int color;

        public Data(List<Float> data) {
            this.data = data;
        }

        public Data(List<Float> data, int color) {
            this.data = data;
            this.color = color;
        }

        public List<Float> getData() {
            return data;
        }

        public int getColor() {
            return color;
        }

    }

}
