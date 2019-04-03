//package com.zinc.class21_svg;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.View;
//
///**
// * author       : zinc
// * time         : 2019/4/1 上午11:07
// * desc         :
// * version      :
// */
//public class CanvasView extends View {
//
//    private Paint mPaint;
//
//    private Matrix mMatrix;
//
//    public CanvasView(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public CanvasView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//
//        mMatrix = new Matrix();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 3.5f, mPaint);
//
//        mMatrix.postTranslate();
//        mMatrix.postScale();
//
//    }
//}
