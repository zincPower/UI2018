package com.zinc.class8_pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/6
 * @description
 */
public class ABCView extends BaseView {

    public ABCView(Context context) {
        super(context);
    }

    public ABCView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ABCView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Path mPath;

    PathMeasure mClosePathMeasure;
    PathMeasure mNoClosePathMeasure;
    PathMeasure mNoClosePathMeasure2;

    // 用于验证 nextContour 方法的路径
    Path mNextContourPath;
    PathMeasure mNextContourPathMeasure;

    // ===============公用属性 start==================
    int width;
    int height;

    boolean isInit = false;

    Paint mPaint;
    // ===============公用属性 end  ==================

    @Override
    protected void init(Context context) {
        mPath = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(context, R.color.color_blue));
        mPaint.setStyle(Paint.Style.STROKE);

        mNextContourPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isInit) {
            isInit = true;

            width = getMeasuredWidth() / 2;
            height = getMeasuredHeight() / 2;

            mPath.lineTo(0, 100);
            mPath.lineTo(100, 100);
            mPath.lineTo(100, -100);
            mPath.lineTo(200, -100);
            mPath.lineTo(200, 0);

            mClosePathMeasure = new PathMeasure(mPath, true);
            float closeLength = mClosePathMeasure.getLength();

            mNoClosePathMeasure = new PathMeasure(mPath, false);
            float noCloseLength = mNoClosePathMeasure.getLength();

            // 当路径Path变动后，PathMeasure需要重新关联，否则保存的还是之前的path数据。
            // 所以执行了 mPath.close()后，此处的 noCloseLength2 便为800，而 noCloseLength 还是 600
            mPath.close();
            mNoClosePathMeasure2 = new PathMeasure(mPath, false);
            float noCloseLength2 = mNoClosePathMeasure2.getLength();

            Log.i(TAG, "PathMeasure Info( forceClose=true )");
            Log.i(TAG, "Length:" + closeLength);
            Log.i(TAG, "isClose: " + mClosePathMeasure.isClosed());
            Log.i(TAG, "=======================================");

            Log.i(TAG, "PathMeasure Info( forceClose=false )");
            Log.i(TAG, "Length:" + noCloseLength);
            Log.i(TAG, "isClose: " + mNoClosePathMeasure.isClosed());
            Log.i(TAG, "=======================================");

            Log.i(TAG, "PathMeasure Info( forceClose=false ) Path is Close");
            Log.i(TAG, "Length:" + noCloseLength2);
            Log.i(TAG, "isClose: " + mNoClosePathMeasure2.isClosed());
            Log.i(TAG, "=======================================");

            mNextContourPath.moveTo(-100, -100);
            mNextContourPath.lineTo(-100, 100);
            mNextContourPath.lineTo(100, 100);
            mNextContourPath.lineTo(100, -100);
            mNextContourPath.lineTo(-100, -100);

            mNextContourPath.moveTo(-50, -50);
            mNextContourPath.lineTo(-50, 50);
            mNextContourPath.lineTo(50, 50);
            mNextContourPath.lineTo(50, -50);


            mNextContourPath.moveTo(50, -50);
            mNextContourPath.lineTo(-50, -50);

            mNextContourPathMeasure = new PathMeasure(mNextContourPath, false);

            Log.i(TAG, "NextContour");
            int i = 0;
            while (mNextContourPathMeasure.nextContour()) {
                ++i;
                Log.i(TAG, "第" + i + "个轮廓的 Length:" + mNextContourPathMeasure.getLength());
//                Log.i(TAG, "isClose: " + mNextContourPathMeasure.isClosed());
            }
//            Log.i(TAG, "=======================================");

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 平移至画布中间
        canvas.translate(width, height);

        // 画折线路径
//        canvas.drawPath(mPath, mPaint);

        // 画 测试nextContour 的路径
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_purple));
        mPaint.setStrokeWidth(2.5f);
//        canvas.drawPath(mNextContourPath, mPaint);

//        mDst.rLineTo(-200,-200);

    }
}
