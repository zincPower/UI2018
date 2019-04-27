package com.zinc.canvas.widget.text;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.zinc.canvas.widget.draw.BaseDrawView;

/**
 * author       : zinc
 * time         : 2019/4/22 下午5:05
 * desc         :
 * version      :
 */
public class TextOnPathView extends BaseDrawView {

    private static final String CONTENT = "zinc 猛猛的小盆友";
    private static final char[] C = "https://blog.csdn.net/weixin_37625173".toCharArray();

    private Path mPath;

    private int mTextSize;

    public TextOnPathView(Context context) {
        super(context);
    }

    public TextOnPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextOnPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        mPath = new Path();
        mTextSize = dpToPx(14);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mPath.isEmpty()){
            initPath(mPath, 200, 50, getWidth());
        }

        mPaint.setColor(mColor1);
        mPaint.setTextSize(mTextSize);
        canvas.drawTextOnPath(CONTENT, mPath, 0, 0, mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);

        canvas.translate(0, 300);

        mPaint.setColor(mColor2);
        mPaint.setTextSize(dpToPx(18));
        canvas.drawTextOnPath(C, 2, 20, mPath, 0, 0, mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);

    }

    /**
     * @param path        路径
     * @param length      曲线的宽度
     * @param height      贝塞尔曲线的高度
     * @param screenWidth 屏幕宽
     */
    private void initPath(Path path,
                          int length,
                          int height,
                          int screenWidth) {

        path.moveTo(-screenWidth / 3, 0);

        for (int i = 0; i < screenWidth * 2 / 3; i += length) {
            path.rQuadTo(length / 4,
                    -height,
                    length / 2,
                    0);
            path.rQuadTo(length / 4,
                    height,
                    length / 2,
                    0);
        }
    }

}
