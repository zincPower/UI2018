package com.zinc.canvas.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 下午5:05
 * desc         :
 * version      :
 */
public class PathTextView extends BaseDrawView {

    private static final String CONTENT = "zinc 猛猛的小盆友";

    private Path mPath;

    private int mTextSize;

    public PathTextView(Context context) {
        super(context);
    }

    public PathTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        initPath(mPath, 150, 200, getWidth());

        mPaint.setColor(mColor1);
        mPaint.setTextSize(mTextSize);

        canvas.drawTextOnPath(CONTENT, mPath, 0, 0, mPaint);

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

        path.moveTo(-screenWidth / 2, 0);

        for (int i = 0; i < screenWidth; i += length) {
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
