package com.zinc.code8_canvas_draw.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zinc.code8_canvas_draw.R;

/**
 * author       : zinc
 * time         : 2019/5/9 下午10:10
 * desc         :
 * version      :
 */
public class CanvasView extends View {

    private Paint mPaint;
    private RectF mRect;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(context, R.color.canvas_orange_color));

        mRect = new RectF();
        mRect.left = -100;
        mRect.top = -100;
        mRect.right = 100;
        mRect.bottom = 100;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        log(canvas);

        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(),
                mPaint, Canvas.ALL_SAVE_FLAG);
        log(canvas);

        canvas.save();
        log(canvas);

        canvas.saveLayer(0, 0, getWidth(), getHeight(),
                mPaint, Canvas.ALL_SAVE_FLAG);
        log(canvas);

        canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(),
                50, Canvas.ALL_SAVE_FLAG);
        log(canvas);

        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawRect(mRect, mPaint);

        canvas.restore();
        log(canvas);

        canvas.restoreToCount(layer);
        log(canvas);

    }

    private void log(Canvas canvas) {
        Log.i("canvas", "canvas count:" + canvas.getSaveCount());
    }

}