package com.zinc.class8_pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/2
 * @description 测试 {@link android.graphics.PathMeasure#getSegment(float, float, Path, boolean)} api
 */
public class GetSegmentView extends BaseView {

    // 测试 getSegment方法 的属性
    Path mRectPath;   // 进行截取的矩形
    Path mDst;      // 存放截取的路径
    PathMeasure mGetSegmentPathMeasure;

    Paint mPaint;

    public GetSegmentView(Context context) {
        super(context);
        init(context);
    }

    public GetSegmentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GetSegmentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        mRectPath = new Path();
        mDst = new Path();

        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.color_purple));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2.5f);
        mPaint.setStyle(Paint.Style.STROKE);

        mGetSegmentPathMeasure = new PathMeasure();
        mRectPath.addCircle(0, 0, 400, Path.Direction.CW);
        mGetSegmentPathMeasure.setPath(mRectPath, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        // 平移至画布中间
        canvas.translate(mWidth / 2, mHeight / 2);

        mDst.moveTo(0, 0);
        mDst.lineTo(200, 200);

        mGetSegmentPathMeasure.getSegment(mGetSegmentPathMeasure.getLength() * 0.25f,
                mGetSegmentPathMeasure.getLength() * 0.5f,
                mDst,
                true);

        canvas.drawPath(mDst, mPaint);
    }
}
