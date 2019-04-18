package com.zinc.xfermode.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zinc.xfermode.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/19
 * @description
 */
public class PingPongView extends View {

    private Paint mPaint;
    private int mItemWaveLength = 0;
    private int dx = 0;

    private Bitmap mPingPongBmp;
    private Bitmap mSrcBmp;

    private Xfermode mXfermode;
    private Canvas mSrcCanvas;

    public PingPongView(Context context) {
        this(context, null, 0);
    }

    public PingPongView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PingPongView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = 2;
        mPingPongBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ping_pong, opt);
        mSrcBmp = Bitmap.createBitmap(mPingPongBmp.getWidth(),
                mPingPongBmp.getHeight(),
                Bitmap.Config.ARGB_8888);

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        mSrcCanvas = new Canvas(mSrcBmp);

        mItemWaveLength = mPingPongBmp.getWidth();
        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //清空bitmap
        mSrcCanvas.drawColor(Color.RED, PorterDuff.Mode.CLEAR);
        //画上矩形
        mSrcCanvas.drawRect(mPingPongBmp.getWidth() - dx,
                0,
                mPingPongBmp.getWidth(),
                mPingPongBmp.getHeight(),
                mPaint);

        //模式合成
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(mPingPongBmp, 0, 0, mPaint);
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(mSrcBmp, 0, 0, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }

    public void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mItemWaveLength);
        animator.setDuration(4500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}