package com.zinc.canvas.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zinc.canvas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019/4/21 下午8:35
 * desc         :
 * version      :
 */
public class ClipView extends View {

    private static final int DEGREE_COUNT = 12;

    private Paint mPaint;
    private RectF mRectF;
    private Rect mRect;
    private int mBgColor;

    private Path mPath;


    public ClipView(Context context) {
        this(context, null);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mBgColor = ContextCompat.getColor(context, R.color.canvas_red_color);

        mRectF = new RectF();
        mRect = new Rect();
        mPath = new Path();
        createHeart(mPath);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth() / 2, getHeight() / 2);

        canvas.clipPath(mPath, Region.Op.DIFFERENCE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas.clipOutRect(mRect);
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas.clipOutRect(mRectF);
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas.clipOutRect(0, 0, 100, 100);
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas.clipOutRect(0f, 0f, 100f, 100f);
//        }

        canvas.drawColor(mBgColor);

    }

    /**
     * 构建心形
     */
    private void createHeart(Path path) {
        List<PointF> pointList = new ArrayList<>();
        pointList.add(new PointF(0, dpToPx(-38)));
        pointList.add(new PointF(dpToPx(50), dpToPx(-103)));
        pointList.add(new PointF(dpToPx(112), dpToPx(-61)));
        pointList.add(new PointF(dpToPx(112), dpToPx(-12)));
        pointList.add(new PointF(dpToPx(112), dpToPx(37)));
        pointList.add(new PointF(dpToPx(51), dpToPx(90)));
        pointList.add(new PointF(0, dpToPx(129)));
        pointList.add(new PointF(dpToPx(-51), dpToPx(90)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(37)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(-12)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(-61)));
        pointList.add(new PointF(dpToPx(-50), dpToPx(-103)));

        path.reset();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                path.moveTo(pointList.get(i * 3).x, pointList.get(i * 3).y);
            } else {
                path.lineTo(pointList.get(i * 3).x, pointList.get(i * 3).y);
            }

            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }

            path.cubicTo(pointList.get(i * 3 + 1).x, pointList.get(i * 3 + 1).y,
                    pointList.get(i * 3 + 2).x, pointList.get(i * 3 + 2).y,
                    pointList.get(endPointIndex).x, pointList.get(endPointIndex).y);
        }
        path.close();
    }

    protected int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }

}
