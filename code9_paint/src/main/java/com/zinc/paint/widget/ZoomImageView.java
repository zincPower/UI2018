package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zinc.paint.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/17
 * @description
 */
public class ZoomImageView extends View {

    // 放大倍数
    private static final int FACTOR = 2;
    // 放大镜大小 半径
    private static final int RADIUS = 100;

    private Bitmap orgBitmap;
    private Bitmap largeBitmap;

    private ShapeDrawable shapeDrawable;
    private Matrix matrix;

    public ZoomImageView(Context context) {
        this(context, null, 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        orgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xyjy3);

        largeBitmap = Bitmap.createScaledBitmap(orgBitmap,
                orgBitmap.getWidth() * FACTOR,
                orgBitmap.getHeight() * FACTOR,
                true);

        // BitmapShader 的作用 https://blog.csdn.net/nangeque/article/details/55682146
        BitmapShader shader = new BitmapShader(largeBitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        // 关于 OvalShape https://blog.csdn.net/zhangphil/article/details/52025152
        // OvalShape 是一个 圆形的 shape
        // RectShape 是一个 方形的 shape
        shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setShader(shader);
        // 切出矩形区域，用来画圆（内切圆）
        // Drawable的setBounds方法有四个参数，setBounds(int left, int top, int right, int bottom),
        // 这个四参数指的是drawable将在被绘制在canvas的哪个矩形区域内。
         shapeDrawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2);

        matrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画原图
        canvas.drawBitmap(orgBitmap, 0, 0, null);

        // 画放大图
        shapeDrawable.draw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // 偏移
        matrix.setTranslate(-x * FACTOR + RADIUS, -y * FACTOR + RADIUS);
        shapeDrawable.getPaint().getShader().setLocalMatrix(matrix);
        shapeDrawable.setBounds((int) x - RADIUS,
                (int) y - RADIUS,
                (int) x + RADIUS,
                (int) y + RADIUS);

        invalidate();

        return true;
    }
}
