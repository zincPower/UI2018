package com.zinc.class8_pathmeasure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/5
 * @description
 */
public class TestView extends View {

    private static final float DELAY = 0.005f;

    // PathMeasure 测量过程中的坐标
    private float mPos[];
    // PathMeasure 测量过程中的正切
    private float mTan[];

    // 宽度
    private float mWidth;
    // 高度
    private float mHeight;

    // 圈的画笔
    private Paint mCirclePaint;

    // 箭头图片
    private Bitmap mArrowBitmap;

    // 圆路径
    private Path mCirclePath;

    // 路径测量
    private PathMeasure mPathMeasure;

    // 当前移动值
    private float mCurrentValue = 0;

    private Matrix mMatrix;

    public TestView(Context context) {
        super(context);
        init(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStrokeWidth(2);

        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置缩放
        options.inSampleSize = 8;
        // 获取图片
        mArrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, options);

        mCirclePath = new Path();
        mCirclePath.addCircle(0, 0, 200, Path.Direction.CCW);

        mPos = new float[2];
        mTan = new float[2];

        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mCirclePath, false);

        mMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        this.mWidth = getMeasuredWidth();
        this.mHeight = getMeasuredHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCurrentValue = 0.875f;

        // 画背景
        canvas.drawColor(Color.WHITE);

        // 画圆
        canvas.drawPath(mCirclePath, mCirclePaint);

        // 测量 pos(坐标) 和 tan(正切)
        mPathMeasure.getPosTan(mPathMeasure.getLength() * mCurrentValue, mPos, mTan);

        // 计算角度
//        float degree = (float) (Math.atan2(mTan[1], mTan[0]) * 180 / Math.PI);
        float degree = (float) (Math.atan2(mPos[1], mPos[0]) * 180 / Math.PI) - 90;

        Log.i("PlaneLoadingView_1", "------------pos[0] = " + mPos[0] + "; pos[1] = " + mPos[1]);
        Log.i("PlaneLoadingView_2", "------------tan[0] = " + mTan[0] + "; tan[1] = " + mTan[1]);
        Log.i("PlaneLoadingView_3", "path length = " + mPathMeasure.getLength() * mCurrentValue);
        Log.i("PlaneLoadingView_4", "degree = " + degree);
        Log.i("PlaneLoadingView_5", "mmmm = " + (Math.atan2(mPos[1], mPos[0]) * 180 / Math.PI));

        // 重置矩阵
        mMatrix.reset();
        // 设置旋转角度
        mMatrix.postRotate(degree, mArrowBitmap.getWidth() / 2, mArrowBitmap.getHeight() / 2);
        // 设置偏移量
        mMatrix.postTranslate(mPos[0] - mArrowBitmap.getWidth() / 2,
                mPos[1] - mArrowBitmap.getHeight() / 2);

        canvas.drawCircle(0, 0, 3, mCirclePaint);

        canvas.drawBitmap(mArrowBitmap, mMatrix, mCirclePaint);

        canvas.drawCircle(mPos[0], mPos[1], 3, mCirclePaint);

    }
}
