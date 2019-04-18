package com.zinc.xfermode.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/22
 * @description FerMode 的 样式使用
 * <p>
 * 注意的是，要和google的示例一模一样需要在manifest中增加
 * android:hardwareAccelerated="false"
 * 关闭硬件加速，其实就是避开 openGL 的绘制
 * 否则Clear、Darken、Lighten会和示例中的不太一样  k
 */
public class GoogleXFerModeView extends View {

    private String TAG = "XFerModeView";

    static Bitmap makeDst(int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xffFFCC44);

        canvas.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), paint);

        return bitmap;
    }

    static Bitmap makeSrc(int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff66AAFF);
        canvas.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, paint);

        return bitmap;

    }

    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),

            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),

            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),

            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };

    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "Xor",
            "SrcOver", "DstOver", "SrcIn", "DstIn",
            "SrcOut", "DstOut", "SrcATop", "DstATop",
            "Darken", "Lighten", "Multiply", "Screen"
    };

    public GoogleXFerModeView(Context context) {
        this(context, null, 0);
    }

    public GoogleXFerModeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleXFerModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 边框画笔
    private Paint borderPaint;
    // 字体画笔
    private Paint textPaint;

    private boolean isInit = false;
    // 每个框的宽度
    private float itemWidth;
    // 横向边界
    private float horizontalOffset = 10;
    // 纵向边界
    private float verticalOffset;

    // 边框的宽
    private int itemBorderWidth = 2;

    // 字体大小
    private int textSize = 25;

    private Bitmap dstBitmap;
    private Bitmap rectBitmap;

    private Paint bitmapPaint;

    // 背景
    private Shader itemBackground;

    private void init(Context context) {
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStrokeWidth(itemBorderWidth);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        // 字体居中[即画的点为字体中间]
        textPaint.setTextAlign(Paint.Align.CENTER);

        // 图的绘制笔
        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);

        Bitmap bm = Bitmap.createBitmap(new int[]{0xFFFFFFFF, 0xFFCCCCCC, 0xFFCCCCCC, 0xFFFFFFFF},
                2, 2, Bitmap.Config.RGB_565);
        itemBackground = new BitmapShader(bm,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);
        Matrix m = new Matrix();
        m.setScale(6, 6);
        itemBackground.setLocalMatrix(m);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isInit) {
            isInit = true;

            int width = getMeasuredWidth();
            // 计算每个宽的大小
            itemWidth = (width - 4 * horizontalOffset) / 4;

            verticalOffset = itemWidth * 0.1f;

            int borderWidth = itemBorderWidth * 2;
            dstBitmap = makeDst((int) itemWidth - borderWidth, (int) itemWidth - borderWidth);
            rectBitmap = makeSrc((int) itemWidth - borderWidth, (int) itemWidth - borderWidth);

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {

                float translateX = horizontalOffset / 2 + (horizontalOffset + itemWidth) * col;
                float translateY = (verticalOffset + itemWidth + textSize) * row;

                float translateYItem = textSize + verticalOffset;

                // 绘制边框
                borderPaint.setShader(null);
                borderPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(translateX,
                        translateY + translateYItem,
                        translateX + itemWidth,
                        translateY + itemWidth + translateYItem,
                        borderPaint);

                // 绘制背景
                borderPaint.setShader(itemBackground);
                borderPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(translateX,
                        translateY + translateYItem,
                        translateX + itemWidth,
                        translateY + itemWidth + translateYItem,
                        borderPaint);

                // 存储图层
                int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                        Canvas.ALL_SAVE_FLAG);

                canvas.translate(translateX, translateY);

                // 绘制字体
                canvas.drawText(sLabels[row * 4 + col],
                        itemWidth / 2,
                        (textSize + verticalOffset) / 2 + 10,
                        textPaint);

                canvas.translate(0, translateYItem);

                bitmapPaint.setXfermode(null);
                // 画圆
                canvas.drawBitmap(dstBitmap, itemBorderWidth, itemBorderWidth, bitmapPaint);
                bitmapPaint.setXfermode(sModes[row * 4 + col]);
                // 画矩形
                canvas.drawBitmap(rectBitmap, itemBorderWidth, itemBorderWidth, bitmapPaint);

                canvas.restoreToCount(layer);
            }
        }

    }

}
