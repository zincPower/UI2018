package com.zinc.xfermode.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zinc.lib_base.UIUtils;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/14
 * @description
 */
public class ScratchCardImageView extends android.support.v7.widget.AppCompatImageView {

    private final static String CARD_COLOR = "#A9A9A9";
    private final static float PERCENT = 0.2f;

    private Paint mPaint;
    private Path mPath;

    private PorterDuffXfermode mXfermode;

    // 涂层图像
    private Bitmap mCoatingLayerBitmap;

    private Canvas mCanvas;

    private float mPreX;
    private float mPreY;

    private boolean isShowAll = false;

    private boolean isInit = false;

    /**
     * 用于计算是否已经到达全部显现的阈值
     */
    private Runnable calculatePixelsRunnable = new Runnable() {
        @Override
        public void run() {

            int width = getWidth();
            int height = getHeight();

            float totalPixel = width * height;

            int[] pixel = new int[width * height];

            mCoatingLayerBitmap.getPixels(pixel, 0, width, 0, 0, width, height);

            int cleanPixel = 0;
            for (int col = 0; col < height; ++col) {
                for (int row = 0; row < width; ++row) {
                    if (pixel[col * width + row] == 0) {
                        cleanPixel++;
                    }
                }
            }

            float result = cleanPixel / totalPixel;

            if (result >= PERCENT) {
                isShowAll = true;
                postInvalidate();
            }

        }
    };

    public ScratchCardImageView(Context context) {
        this(context, null, 0);
    }

    public ScratchCardImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchCardImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(CARD_COLOR));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(UIUtils.dip2px(context, 10));
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置圆角

        mPath = new Path();

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInit) {
            mCoatingLayerBitmap = createCoatingLayer(getMeasuredWidth(), getMeasuredHeight());
            mCanvas = new Canvas(mCoatingLayerBitmap);

            isInit = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isShowAll) {
            return;
        }

        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(mCoatingLayerBitmap, 0, 0, mPaint);
        mPaint.setXfermode(mXfermode);
        canvas.drawPath(mPath, mPaint);

        mCanvas.drawPath(mPath, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layer);

    }

    private Bitmap createCoatingLayer(int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(CARD_COLOR));

        canvas.drawRect(new RectF(0, 0, w, h), paint);
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreX = event.getX();
                mPreY = event.getY();
                mPath.moveTo(mPreX, mPreY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (mPreX + event.getX()) / 2;
                float endY = (mPreY + event.getY()) / 2;
                mPath.quadTo(mPreX, mPreY, endX, endY);
                mPreX = endX;
                mPreY = endY;
                break;
            case MotionEvent.ACTION_UP:
                post(calculatePixelsRunnable);
                break;
        }

        postInvalidate();

        return true;
    }

    public void recycle() {
        if (mCoatingLayerBitmap != null) {
            mCoatingLayerBitmap.recycle();
        }
    }
}