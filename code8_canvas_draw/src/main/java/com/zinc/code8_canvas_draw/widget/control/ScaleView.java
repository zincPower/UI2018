package com.zinc.code8_canvas_draw.widget.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zinc.code8_canvas_draw.widget.draw.BaseDrawView;
import com.zinc.lib_base.BaseView;

/**
 * author       : zinc
 * time         : 2019/4/25 下午11:44
 * desc         :
 * version      :
 */
public class ScaleView extends BaseView {

    protected Paint mPaint;
    protected RectF mRectF;

    public ScaleView(Context context) {
        super(context);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mRectF = new RectF();
        mRectF.left = -200;
        mRectF.top = -300;
        mRectF.right = 200;
        mRectF.bottom = 300;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvas.translate(getWidth() / 2, getHeight() / 2);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);

        mPaint.setColor(Color.RED);
        canvas.drawRect(mRectF, mPaint);

        canvas.scale(0.5f,0.33f);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(mRectF, mPaint);

    }
}
