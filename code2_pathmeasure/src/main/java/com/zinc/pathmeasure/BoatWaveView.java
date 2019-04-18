package com.zinc.pathmeasure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.zinc.lib_base.BaseView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/7
 * @description
 */
public class BoatWaveView extends BaseView {

    public BoatWaveView(Context context) {
        super(context);
    }

    public BoatWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BoatWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 小船浪花的宽度
    private static final int BOAT_LENGTH = 200;
    // 浪花的宽度
    private int WAVE_LENGTH;

    // 小船浪花的高度
    private static final int BOAT_WAVE_HEIGHT = 20;
    // 波浪高度
    private static final int WAVE_HEIGHT = 35;

    // 浪花每次的偏移量
    private final static int WAVE_OFFSET = 5;

    private boolean isInit = false;

    private int mWidth;
    private int mHeight;

    public Paint mWavePaint;
    // 海浪的路径
    public Path mWavePath;
    // 小船的浪路径
    public Path mBoatWavePath;
    // 小船的路径
    public Path mBoatPath;

    private PathMeasure mBoatPathMeasure;

    // 小船的浪色值
    private int mBoatBlue;
    // 浪花的色值
    private int mWaveBlue;

    // 小船当前所处的值
    private float mCurValue = 0f;

    // 浪花当前的偏移量
    private int mCurWaveOffset = 0;
    // 小船的浪花偏移量
    private int mBoatWaveOffset = 0;

    // 用于变换小船的
    private Matrix mMatrix;

    // 小船的图片
    private Bitmap mBoatBitmap;

    private ValueAnimator mAnimator;

    @Override
    protected void init(Context context) {

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);

        mBoatPath = new Path();
        mWavePath = new Path();
        mBoatWavePath = new Path();

        mBoatPathMeasure = new PathMeasure();

        mBoatBlue = ContextCompat.getColor(context, R.color.color_boat_blue);
        mWaveBlue = ContextCompat.getColor(context, R.color.color_wave_blue);

        mMatrix = new Matrix();

        // 加载图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        mBoatBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boat, options);

        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(4000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurValue = (float) animation.getAnimatedValue();
                mCurWaveOffset = (mCurWaveOffset + WAVE_OFFSET) % mWidth;
                mBoatWaveOffset = (mBoatWaveOffset + WAVE_OFFSET/2) % mWidth;

                postInvalidate();
            }
        });

    }

    /**
     * @param path       路径
     * @param length     浪花的宽度
     * @param height     浪花的高度
     * @param isClose    是否要闭合
     * @param lengthTime 浪花长的倍数
     */
    private void initPath(Path path, int length, int height, boolean isClose, float lengthTime) {
        // 初始化 小船的路径
        path.moveTo(-length, mHeight / 2);
        for (int i = -length; i < mWidth * lengthTime + length; i += length) {
            // rQuadTo 和 quadTo 区别在于
            // rQuadTo 是相对上一个点 而 quadTo是相对于画布
            path.rQuadTo(length / 4,
                    -height,
                    length / 2,
                    0);
            path.rQuadTo(length / 4,
                    height,
                    length / 2,
                    0);
        }

        if (isClose) {
            path.rLineTo(0, mHeight / 2);
            path.rLineTo(-(mWidth * 2 + 2 * length), 0);
            path.close();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isInit) {
            isInit = true;

            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();

            WAVE_LENGTH = mWidth / 3;

            // 初始化 小船的浪路径
            initPath(mBoatWavePath, WAVE_LENGTH, BOAT_WAVE_HEIGHT, true, 2);

            // 初始化 浪的路径
            initPath(mWavePath, WAVE_LENGTH, WAVE_HEIGHT, true, 2);

            // 初始化 小船的路径
            initPath(mBoatPath, WAVE_LENGTH, BOAT_WAVE_HEIGHT, false, 1);

            // 让 PathMeasure 与 Path 关联
            mBoatPathMeasure.setPath(mBoatPath, false);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        float length = mBoatPathMeasure.getLength();
        mBoatPathMeasure.getMatrix(length * mCurValue,
                mMatrix,
                PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        mMatrix.preTranslate(-mBoatBitmap.getWidth() / 2, -mBoatBitmap.getHeight() * 5 / 6);

        canvas.drawBitmap(mBoatBitmap, mMatrix, null);

        // 画船的浪花
        canvas.save();
        canvas.translate(-mBoatWaveOffset, 0);
        mWavePaint.setColor(mBoatBlue);
        canvas.drawPath(mBoatWavePath, mWavePaint);
        canvas.restore();

        // 画浪花
        canvas.save();
        canvas.translate(-mCurWaveOffset, 0);
        mWavePaint.setColor(mWaveBlue);
        canvas.drawPath(mWavePath, mWavePaint);
        canvas.restore();

    }

    public void startAnim() {
        mAnimator.start();
    }

    public void stopAnim() {
        mAnimator.cancel();
    }

}
