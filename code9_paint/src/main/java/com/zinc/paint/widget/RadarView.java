package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.paint.R;
import com.zinc.lib_base.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/30
 * @description
 */
public class RadarView extends View {

    // 最多显示的小点
    private static int DOT_COUNT = 3;

    // 雷达图宽 和 高
    private float mWidth;
    // 圈的间隔
    private float[] pots = new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1f};
    // 圈的画笔
    private Paint circlePaint;
    // 扫描的画笔
    private Paint scanPaint;
    // 圈的画笔的粗细
    private float circlePaintWidth;
    // 扫描渲染 shader
    private Shader scanShader;
    // 旋转矩阵
    private Matrix matrix;
    // 扫描线程
    private Runnable scanRunnable;
    // 扫描速度
    private float speed;
    // 更新间隔
    private static final int INTERVAL = 20;

    // 范围
    private RectF mRect;

    private float mViewWidth;
    private float mViewHeight;

    private final List<DotInfo> mDotInfoList = new ArrayList<>();

    public RadarView(Context context) {
        this(context, null, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {

        mDotInfoList.clear();
        for (int i = 0; i < DOT_COUNT; ++i) {
            mDotInfoList.add(new DotInfo());
        }

        mRect = new RectF();

        circlePaintWidth = UIUtils.dip2px(context, 0.5f);

        matrix = new Matrix();

        // 初始化圈画笔
        circlePaint = new Paint();
        // 设置宽
        circlePaint.setStrokeWidth(circlePaintWidth);
        // 设置抗锯齿
        circlePaint.setAntiAlias(true);
        // 设置颜色
        circlePaint.setColor(ContextCompat.getColor(context, R.color.color_circle));
        // 设置样式，只画线，不填充
        circlePaint.setStyle(Paint.Style.STROKE);

        // 初始化扫描笔
        scanPaint = new Paint();
        // 设置样式，即画线，又填充
        scanPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 设置抗锯齿
        scanPaint.setAntiAlias(true);

        // 初始化速度
        speed = 5;

        post(scanRunnable);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;

        float tempWidth = w - getPaddingLeft() - getPaddingRight() - circlePaintWidth;
        float tempHeight = h - getPaddingTop() - getPaddingBottom() - circlePaintWidth;

        mWidth = Math.min(tempHeight, tempWidth);

        // 初始化样式；判空是因为，7.0之后的onMeasure会走两次，避免不必要的初始化
        if (scanShader == null) {
            scanShader = new SweepGradient(0,
                    0,
                    new int[]{Color.TRANSPARENT, ContextCompat.getColor(getContext(), R.color.color_scan)},
                    null);
            scanPaint.setShader(scanShader);
        }

        mRect.left = -mWidth / 2;
        mRect.right = mWidth / 2;
        mRect.top = -mWidth / 2;
        mRect.bottom = mWidth / 2;

        for (DotInfo dotInfo : mDotInfoList) {
            dotInfo.setRect(mRect);
        }

    }

    public void start() {
        // 初始化扫描线程
        scanRunnable = new Runnable() {
            @Override
            public void run() {
                matrix.postRotate(speed, 0, 0);
                postDelayed(scanRunnable, INTERVAL);
                invalidate();
            }
        };

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.translate(mViewWidth / 2, mViewHeight / 2);

        // 循环画圈
        for (float pot : pots) {
            canvas.drawCircle(0, 0, pot * mWidth / 2, circlePaint);
        }

        canvas.drawLine(-mWidth / 2, 0, mWidth / 2, 0, circlePaint);
        canvas.drawLine(0, -mWidth / 2, 0, mWidth / 2, circlePaint);

        canvas.save();

        canvas.concat(matrix);
        canvas.drawCircle(0, 0, mWidth / 2, scanPaint);

        canvas.restore();

    }

    private static class DotInfo {

        private static final int DURATION_MIN = 300;
        private static final int DURATION_MAX = 800;

        private static final int RADIUS_MAX = UIUtils.dip2px(5);
        private static final int RADIUS_MIN = UIUtils.dip2px(5);

        private Random random;

        // 可用范围
        private RectF rectF;

        // 该点所在 x轴 坐标
        private float x;
        // 该点所在 y轴 坐标
        private float y;

        // 该点的动画时长
        private int duration;
        // 当前点已经运行的时长
        private int curTime;
        // 上次运行的时间
        private long beforeTime;

        // 点半径
        private float radius;

        public DotInfo() {
            this.random = new Random();
        }

        public void setRect(RectF rectF) {
            this.rectF = rectF;
            buildDot();
        }

        private void buildDot() {
            calculatePointCoordination();

            duration = DURATION_MIN + random.nextInt(DURATION_MAX - DURATION_MIN);
            curTime = 0;
            beforeTime = System.currentTimeMillis();
            radius = RADIUS_MIN + random.nextFloat() * (RADIUS_MAX - RADIUS_MIN);
        }

        private void calculatePointCoordination() {
            x = random.nextFloat() * rectF.width();
            x = x - rectF.left;

            float yRange = (float) Math.sqrt(Math.pow(rectF.left, 2) - Math.pow(x, 2)) * 2;

            y = random.nextFloat() * yRange - yRange / 2;
        }
    }
}
