package com.zinc.code8_canvas_clip.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019/4/26 上午12:22
 * desc         :
 * version      :
 */
public class ClipOpView extends BaseView {

    // 是A形状中不同于B的部分显示出来
    public static final String DIFFERENCE = "DIFFERENCE";
    // 是A和B交集的形状
    public static final String INTERSECT = "INTERSECT";
    // 是A和B的全集
    public static final String UNION = "UNION";
    // 是全集形状减去交集形状之后的部分
    public static final String XOR = "XOR";
    // 是B形状中不同于A的部分显示出来，这是没有设置时候默认的
    public static final String REVERSE_DIFFERENCE = "REVERSE_DIFFERENCE";
    // 是只显示B的形状
    public static final String REPLACE = "REPLACE";

    private Path mHeartPath;
    private Path mCirclePath;

    private RectF mHeartRect;

    private int mHeartAlphaColor;
    private int mCircleAlphaColor;
    private int mBackgroundColor;
    private int mTextColor;

    private int mTextSize;

    private Paint mPaint;

    private String mType;

    public ClipOpView(Context context) {
        this(context, null);
    }

    public ClipOpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipOpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setType(String type) {
        this.mType = type;
        postInvalidate();
    }

    @Override
    protected void init(Context context) {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mHeartAlphaColor = Color.parseColor("#aabe97dc");
        mCircleAlphaColor = Color.parseColor("#aa226089");
        mBackgroundColor = Color.parseColor("#df0e62");
        mTextColor = Color.parseColor("#f54291");

        mTextSize = dpToPx(14);

        mHeartPath = new Path();
        createHeart(mHeartPath);

        mHeartRect = new RectF();
        mHeartPath.computeBounds(mHeartRect, true);

        mCirclePath = new Path();
        mCirclePath.addCircle(0,
                mHeartRect.bottom,
                mHeartRect.width() / 2,
                Path.Direction.CCW);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvasText(canvas);

        drawSourcePic(canvas);

        drawClip(canvas);
    }

    private void canvasText(Canvas canvas) {
        canvas.save();

        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mType, getWidth() / 2, mTextSize * 2, mPaint);

        canvas.restore();
    }

    private void drawSourcePic(Canvas canvas) {
        canvas.save();

        mPaint.setColor(mHeartAlphaColor);
        canvas.translate(mHeartRect.width() / 2 + 50,
                mHeartRect.height() / 2 + 50);
        canvas.drawPath(mHeartPath, mPaint);

        mPaint.setColor(mCircleAlphaColor);
        canvas.translate(mHeartRect.width() + 50, -mHeartRect.bottom + 50);
        canvas.drawPath(mCirclePath, mPaint);

        canvas.restore();
    }

    private void drawClip(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);

        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(mHeartAlphaColor);
        canvas.drawPath(mHeartPath, mPaint);

        mPaint.setColor(mCircleAlphaColor);
        canvas.drawPath(mCirclePath, mPaint);

        canvas.clipPath(mHeartPath);

        Region.Op op;
        switch (mType) {
            case DIFFERENCE:
                op = Region.Op.DIFFERENCE;
                break;
            // 是A和B交集的形状
            case INTERSECT:
                op = Region.Op.INTERSECT;
                break;
            // 是A和B的全集
            case UNION:
                op = Region.Op.UNION;
                break;
            // 是全集形状减去交集形状之后的部分
            case XOR:
                op = Region.Op.XOR;
                break;
            // 是B形状中不同于A的部分显示出来，这是没有设置时候默认的
            case REVERSE_DIFFERENCE:
                op = Region.Op.REVERSE_DIFFERENCE;
                break;
            // 是只显示B的形状
            case REPLACE:
                op = Region.Op.REPLACE;
                break;
            default:
                op = Region.Op.INTERSECT;
                break;
        }
        canvas.clipPath(mCirclePath, op);
        canvas.drawColor(mBackgroundColor);
    }

    /**
     * 构建心形
     */
    private void createHeart(Path path) {
        List<PointF> pointList = new ArrayList<>();
        pointList.add(new PointF(0, -76));
        pointList.add(new PointF(100, -206));
        pointList.add(new PointF(224, -122));
        pointList.add(new PointF(224, -24));
        pointList.add(new PointF(224, 74));
        pointList.add(new PointF(102, 180));
        pointList.add(new PointF(0, 258));
        pointList.add(new PointF(-102, 180));
        pointList.add(new PointF(-224, 74));
        pointList.add(new PointF(-224, -24));
        pointList.add(new PointF(-224, -122));
        pointList.add(new PointF(-100, -206));

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
