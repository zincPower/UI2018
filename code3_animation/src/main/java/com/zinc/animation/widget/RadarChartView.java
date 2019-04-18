package com.zinc.animation.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/15
 * @description 雷达图
 */
public class RadarChartView extends View {

    private static final String TAG = "RadarChartView";

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
    private static final int DURATION = 200;

    // 初始化状态
    private static final int INIT = 0x001;
    // 运行状态
    private static final int RUNNING = 0x002;

    // 边框宽度
    private float RADAR_BORDER_LINE_WIDTH = dpToPx(0.5f);
    // 维度分割线宽度
    private float RADAR_DIMEN_LINE_WIDTH = dpToPx(0.5f);
    // 数据线宽度
    private float DATA_LINE_WIDTH = dpToPx(1.5f);
    // 小点的半径
    private float DOT_RADIUS = dpToPx(1.5f);
    // 字体大小
    private float TEXT_SIZE = spToPx(10f);

    // 纬度数
    private int mDimenCount;
    // 每个纬度的线长
    private float mLength;
    // 每个纬度角度
    private double mAngle;

    // 雷达图 顶点坐标集
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

    // 文字描述
    private List<String> mTextDataList;

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

    protected float mWidth;
    protected float mHeight;

    public RadarChartView(Context context) {
        this(context, null, 0);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mLength = Math.min(w, h) / 4;

        calculateRadarChartVertex();
        initRadarLine();
    }

    protected void init(Context context) {

        mCurState = INIT;

        mVertexList = new ArrayList<>();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor(DEFAULT_LINE_COLOR));
        // 连线圆角
        mLinePaint.setPathEffect(new CornerPathEffect(dpToPx(2.5f)));

        mRadarBgPaint = new Paint();
        mRadarBgPaint.setAntiAlias(true);
        mRadarBgPaint.setStyle(Paint.Style.FILL);
        mRadarBgPaint.setPathEffect(new CornerPathEffect(dpToPx(2.5f)));

        mDataPaint = new Paint();
        mDataPaint.setAntiAlias(true);
        mDataPaint.setStrokeWidth(DATA_LINE_WIDTH);
        mDataPaint.setPathEffect(new CornerPathEffect(dpToPx(1f)));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setColor(Color.parseColor(DEFAULT_LINE_COLOR));

        mRadarLinePath = new Path();

        mDimensionPath = new Path();

        mRadarBgPath = new Path();

        mRunningPath = new Path();

        mDataList = new ArrayList<>();
        mBaseDataList = new ArrayList<>();
        mTextDataList = new ArrayList<>();

        mRadarCenterPoint = DEFAULT_CENTER_POINT;

        mDimenCount = DEFAULT_DIMEN_COUNT;
        mAngle = CIRCLE_ANGLE / mDimenCount;

    }

    /**
     * 设置文字描述数据
     *
     * @param textDataList
     */
    public void setTextDataList(List<String> textDataList) {
        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        mTextDataList.clear();
        mTextDataList.addAll(textDataList);
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

        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        mBaseDataList.clear();
        mDataList.clear();

        // 设置维度，并重新计算每个维度角度
        mDimenCount = dimenCount;
        mAngle = CIRCLE_ANGLE / mDimenCount;

        calculateRadarChartVertex();
        initRadarLine();
        invalidate();
    }

    /**
     * 设置数据
     *
     * @param dataList
     */
    public void setDataList(List<Data> dataList) {
        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        this.mDataList.clear();
        this.mDataList.addAll(dataList);
    }

    /**
     * 设置基本数据
     *
     * @param baseDataList
     */
    public void setBaseDataList(List<Data> baseDataList) {
        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        this.mBaseDataList.clear();
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

        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        mCurState = RUNNING;

        // 检测基线数据
        checkData(mBaseDataList);
        // 检测数据
        checkData(mDataList);
        // 检测文字描述数据
        if (mTextDataList.size() != 0 && mTextDataList.size() != mDimenCount) {
            throw new RuntimeException("Text data length should be zero or equal with dimension count.");
        }

        // 将当前循环数置为 0
        mCurLoopCount = 0;

        // 计算数据
        calculateDataVertex(true);
        calculateDataVertex(false);

        /**
         * 第一个维度不需要展开，所以维度数需要-1
         * 这里不使用 setRepeatMode 设置多次循环,
         * 是因为 {@link AnimatorListenerAdapter#onAnimationRepeat(Animator)} 和
         * {@link android.animation.ValueAnimator.AnimatorUpdateListener#onAnimationUpdate(ValueAnimator)}
         * 无法确保其顺序，有时会出现乱值现象，这种现象目前有概率出现在 mate10（8.1.0）手机上，所以使用这种方法进行规避
         **/
        mTotalLoopCount = (mDimenCount - 1) * mDataList.size();
        mAnimator = ValueAnimator.ofFloat(0f, mTotalLoopCount);
        mAnimator.setDuration(DURATION * mTotalLoopCount);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();

                // 整数部分即为当前的动画数据下标
                mCurLoopCount = (int) value;

                // 小数部分极为当前维度正在展开的进度百分比
                mAnimCurValue = value - mCurLoopCount;

                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 动画结束，将状态置为初始状态，并再刷新一次，让最后的数据全部显示
                mCurState = INIT;
                invalidate();
            }
        });

        // 开启动画
        mAnimator.start();
    }

    /**
     * 停止动画
     */
    public void stop() {
        if (mAnimator == null) {
            return;
        }
        mAnimator.cancel();
    }

    /**
     * 清空数据
     */
    public void reset() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mBaseDataList.clear();
        mDataList.clear();
        invalidate();
    }

    /**
     * 计算数据的顶点坐标
     *
     * @param isBase 是否为 基础数据
     */
    private void calculateDataVertex(boolean isBase) {

        List<Data> calDataList = isBase ? mBaseDataList : mDataList;

        for (int i = 0; i < calDataList.size(); ++i) {

            Data data = calDataList.get(i);

            // 获取 比例数据
            List<Float> pointDataList = data.getData();

            // 设置路径
            Path curPath = new Path();
            data.setPath(curPath);

            curPath.reset();
            for (int j = 0; j < pointDataList.size(); ++j) {

                // 当前维度的数据比例
                float ratio = pointDataList.get(j);
                // 当前维度的顶点坐标
                PointF curDimenPoint = mVertexList.get(j);

                if (j == 0) {
                    curPath.moveTo(curDimenPoint.x * ratio,
                            curDimenPoint.y * ratio);
                } else {
                    curPath.lineTo(curDimenPoint.x * ratio,
                            curDimenPoint.y * ratio);
                }

            }
            curPath.close();

        }

    }

    /**
     * 计算雷达图的顶点，这里只是计算，没有进行路径拼凑
     * {@link #initRadarLine()}进行拼凑路径
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
    protected void initRadarLine() {
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

        // 平移画布至中心
        canvas.translate(mWidth / 2, mHeight / 2);

        // 画雷达 框和维度线
        drawRadarLine(canvas);
        // 画雷达 背景
        drawRadarBackground(canvas);
        // 画 顶点的小点
        drawDot(canvas);
        // 画文字
        drawText(canvas);

        // 画基线数据
        drawData(canvas, true);

        if (mCurState == INIT) {
            drawData(canvas, false);
        } else {
            drawRunningData(canvas);
        }

    }

    /**
     * 画文字
     *
     * @param canvas 画布
     */
    protected void drawText(Canvas canvas) {

        if (mTextDataList == null || mTextDataList.size() != mDimenCount) {
            Log.w(TAG, "The length of description text list is not equal with dimension.");
            return;
        }

        for (int i = 0; i < mDimenCount; ++i) {

            PointF vertexPoint = mVertexList.get(i);

            // 所在象限
            int dimension = checkThePointDimension(vertexPoint);

            Paint.Align align;
            float y = vertexPoint.y * 1.15f;
            switch (dimension) {
                case 1:
                    align = Paint.Align.CENTER;
                    break;
                case 2:
                    align = Paint.Align.LEFT;
                    break;
                case 3:
                    align = Paint.Align.LEFT;
                    y -= ((mTextPaint.descent() + mTextPaint.ascent()) / 2);
                    break;
                case 4:
                    align = Paint.Align.LEFT;
                    y -= (mTextPaint.descent() + mTextPaint.ascent());
                    break;
                case 5:
                    align = Paint.Align.CENTER;
                    y -= (mTextPaint.descent() + mTextPaint.ascent());
                    break;
                case 6:
                    align = Paint.Align.RIGHT;
                    y -= (mTextPaint.descent() + mTextPaint.ascent());
                    break;
                case 7:
                    align = Paint.Align.RIGHT;
                    y -= (mTextPaint.descent() + mTextPaint.ascent() / 2);
                    break;
                case 8:
                    align = Paint.Align.RIGHT;
                    break;
                default:
                    align = Paint.Align.CENTER;
                    break;
            }
            mTextPaint.setTextSize(TEXT_SIZE);

            mTextPaint.setTextAlign(align);

            canvas.drawText(mTextDataList.get(i),
                    vertexPoint.x * 1.15f,
                    y,
                    mTextPaint);

        }

    }

    /**
     * 画 顶点的小点，有描述文字时，才绘制小点
     *
     * @param canvas 画布
     */
    protected void drawDot(Canvas canvas) {
        if (mTextDataList == null || mTextDataList.size() != mDimenCount) {
            Log.w(TAG, "The length of description text list is not equal with dimension.");
            return;
        }

        for (PointF point : mVertexList) {

            mLinePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(point.x * 1.08f, point.y * 1.08f, DOT_RADIUS, mLinePaint);

        }
    }

    /**
     * 绘制运动中的数据
     *
     * @param canvas 画布
     */
    protected void drawRunningData(Canvas canvas) {

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
                path = mDataList.get(i).getPath();
            }

            // 画轮廓
            mDataPaint.setStyle(Paint.Style.STROKE);
            mDataPaint.setColor(curData.getColor());
            mDataPaint.setStrokeWidth(DATA_LINE_WIDTH);
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
    protected void drawRadarBackground(Canvas canvas) {

        if (RADAR_BG_SHOW_LEVEL > RADAR_BG_LEVEL) {
            throw new RuntimeException("RADAR_BG_SHOW_LEVEL can not bigger than RADAR_BG_LEVEL.");
        }

        for (int i = 0; i < RADAR_BG_SHOW_LEVEL; ++i) {

            mRadarBgPath.reset();
            for (int j = 0; j < mDimenCount; ++j) {

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
    protected void drawRadarLine(Canvas canvas) {

        // 绘制雷达图边框
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(RADAR_BORDER_LINE_WIDTH);
        canvas.drawPath(mRadarLinePath, mLinePaint);

        // 绘制雷达图维度分割线
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(RADAR_DIMEN_LINE_WIDTH);
        canvas.drawPath(mDimensionPath, mLinePaint);

    }

    /**
     * 绘制基线数据
     *
     * @param canvas 画布
     * @param isBase 是否为基线
     */
    protected void drawData(Canvas canvas, boolean isBase) {

        List<Data> dataList = isBase ? mBaseDataList : mDataList;

        for (int i = 0; i < dataList.size(); ++i) {
            Data data = dataList.get(i);

            int color = data.getColor();

            mDataPaint.setColor(color);
            mDataPaint.setStyle(Paint.Style.STROKE);
            if (isBase) {
                mDataPaint.setStrokeWidth(RADAR_BORDER_LINE_WIDTH);
            } else {
                mDataPaint.setStrokeWidth(DATA_LINE_WIDTH);
            }
            canvas.drawPath(data.getPath(), mDataPaint);

            if (!isBase) {
                mDataPaint.setStyle(Paint.Style.FILL);
                mDataPaint.setColor(getAlphaColor(color, 127));
                canvas.drawPath(data.getPath(), mDataPaint);
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
     * 获取点所在的象限
     *
     * <pre>
     *              ┃
     *              ┃
     *              ┃
     *      8       1       2
     *              ┃
     *              ┃
     * ━━━━━7━━━━━━━╋━━━━━━━3━━━━━━▶ x
     *              ┃
     *              ┃
     *      6       5       4
     *              ┃
     *              ┃
     *              ┃
     *              ▼
     *              y
     * </pre>
     *
     * @return
     */
    protected int checkThePointDimension(PointF pointF) {

        if (pointF == null) {
            return -1;
        }

        int x = (int) pointF.x;
        int y = (int) pointF.y;

        if (x == 0 && y < 0) {
            return 1;
        } else if (x > 0 && y < 0) {
            return 2;
        } else if (x > 0 && y == 0) {
            return 3;
        } else if (x > 0 && y > 0) {
            return 4;
        } else if (x == 0 && y > 0) {
            return 5;
        } else if (x < 0 && y > 0) {
            return 6;
        } else if (x < 0 && y == 0) {
            return 7;
        } else if (x < 0 && y < 0) {
            return 8;
        }
        return -1;
    }

    /**
     * 转换 sp 至 px
     *
     * @param spValue sp值
     * @return px值
     */
    protected int spToPx(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 转换 dp 至 px
     *
     * @param dpValue dp值
     * @return px值
     */
    protected int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
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

        private Path path;

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

        private Path getPath() {
            return path;
        }

        private void setPath(Path path) {
            this.path = path;
        }
    }

}
