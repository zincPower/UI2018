package com.zinc.code8_canvas_draw.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画点
 * version      :
 */
public class PathView extends BaseDrawView {

    private Path mPath;

    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mPath = new Path();
        createHeart(mPath);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor1);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
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
}
