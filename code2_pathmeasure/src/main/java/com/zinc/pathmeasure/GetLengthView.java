package com.zinc.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.zinc.lib_base.BaseView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/4
 * @description 测试 {@link PathMeasure#getLength()}
 */
public class GetLengthView extends BaseView {

    Path mPath;

    PathMeasure mClosePathMeasure;
    PathMeasure mNoClosePathMeasure;
    PathMeasure mNoClosePathMeasure2;

    Paint mPaint;

    public GetLengthView(Context context) {
        super(context);
    }

    public GetLengthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GetLengthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean mIsInit;

    @Override
    protected void init(Context context) {

        if (!mIsInit) {
            mIsInit = true;

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(ContextCompat.getColor(context, R.color.color_blue));
            mPaint.setStrokeWidth(5f);
            mPaint.setStyle(Paint.Style.STROKE);

            mPath = new Path();

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
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        // 平移至画布中间
        canvas.translate(mWidth / 2, mHeight / 2);
        // 画折线路径
        canvas.drawPath(mPath, mPaint);
    }
}
