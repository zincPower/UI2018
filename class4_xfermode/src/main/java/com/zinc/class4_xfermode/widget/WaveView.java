package com.zinc.class4_xfermode.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zinc.class4_xfermode.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/18
 * @description 波浪
 */
public class WaveView extends View {

    private Bitmap mWavBitmap;
    private Bitmap mCircleBitmap;

    private Xfermode mXfermode;

    private Paint mPaint;

    private ValueAnimator mAnimator;

    private int dx;
    private Rect src;
    private Rect dst;

    public WaveView(Context context) {
        this(context, null, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        mWavBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wav, options);
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inSampleSize = 2;
        mCircleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circle, options1);

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mAnimator = ValueAnimator.ofInt(0, mWavBitmap.getWidth());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);

        src = new Rect(dx, 0, dx + mCircleBitmap.getWidth(), mCircleBitmap.getHeight());
        dst = new Rect(0, 0, mCircleBitmap.getWidth(), mCircleBitmap.getHeight());

    }

    public void start() {
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mCircleBitmap, 0, 0, mPaint);

        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        src.left = dx;
        src.top = 0;
        src.right = dx + mCircleBitmap.getWidth();
        src.bottom = mCircleBitmap.getHeight();

        canvas.drawBitmap(mWavBitmap,
                src,
                dst,
                mPaint);
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(mCircleBitmap, 0, 0, mPaint);
        mPaint.setXfermode(null);

        canvas.restoreToCount(layer);

        mPaint.setXfermode(null);


    }
}
