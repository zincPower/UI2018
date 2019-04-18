package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.paint.R;
import com.zinc.lib_base.UIUtils;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/16
 * @description 雷达图
 */
public class RadarGradientView extends View {

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
    private static final int INTERVAL = 50;

    public RadarGradientView(Context context) {
        this(context, null, 0);
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {

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

        // 初始化扫描线程
        scanRunnable = new Runnable() {
            @Override
            public void run() {
                matrix.postRotate(speed, mWidth / 2, mWidth / 2);
                invalidate();
                postDelayed(scanRunnable, INTERVAL);
            }
        };

        post(scanRunnable);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float tempWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - circlePaintWidth;
        float tempHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - circlePaintWidth;

        mWidth = Math.min(tempHeight, tempWidth);

        // 初始化样式；判空是因为，7.0之后的onMeasure会走两次，避免不必要的初始化
        if (scanShader == null) {
            scanShader = new SweepGradient(mWidth / 2, mWidth / 2,
                    new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
            scanPaint.setShader(scanShader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        // 循环画圈
        for (float pot : pots) {
            canvas.drawCircle(mWidth / 2, mWidth / 2, pot * mWidth / 2, circlePaint);
        }

        canvas.save();

        canvas.concat(matrix);
        canvas.drawCircle(mWidth / 2, mWidth / 2,
                pots[pots.length - 1] * mWidth / 2, scanPaint);
//        canvas.drawRect(new RectF(0, 0, mWidth, mWidth), scanPaint);

        canvas.restore();

    }

}
