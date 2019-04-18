package com.zinc.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.zinc.lib_base.BaseView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/2
 * @description 测试 {@link android.graphics.PathMeasure#getSegment(float, float, Path, boolean)} api
 */
public class GetSegmentView extends BaseView {

    // 测试 getSegment方法 的属性
    private Path mPath;   // 进行截取的路径
    private Path mDst;      // 存放截取的路径
    private PathMeasure mGetSegmentPathMeasure;

    private Paint mPaint;

    private boolean mStartWithMoveTo = false;

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
        mPath = new Path();
        mDst = new Path();

        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.color_purple));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2.5f);
        mPaint.setStyle(Paint.Style.STROKE);

        mGetSegmentPathMeasure = new PathMeasure();
        // 顺时针画 半径为400px的圆
        mPath.addCircle(0, 0, 200, Path.Direction.CW);
        mGetSegmentPathMeasure.setPath(mPath, false);
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

        // 重置dst，清空之前路径
        mDst.reset();

        mDst.moveTo(0, 0);
        mDst.lineTo(100, 100);

        mGetSegmentPathMeasure.getSegment(mGetSegmentPathMeasure.getLength() * 0.25f,
                mGetSegmentPathMeasure.getLength() * 0.5f,
                mDst,
                mStartWithMoveTo);

        canvas.drawPath(mDst, mPaint);
    }

    public void setStartWithMoveTo(boolean value) {
        mStartWithMoveTo = value;
        invalidate();
    }

}
