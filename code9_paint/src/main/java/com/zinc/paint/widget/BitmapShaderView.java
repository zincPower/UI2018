package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zinc.paint.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/18
 * @description
 */
public class BitmapShaderView extends View {

    private Paint paint;

    private Bitmap bitmap;

    private Matrix matrix;

    private float radius;

    private int width;

    private BitmapShader bitmapShader;

    private RectF rectF;

    public BitmapShaderView(Context context) {
        this(context, null, 0);
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.xyjy2);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        matrix = new Matrix();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = Math.min(getMeasuredHeight(), getMeasuredWidth());
        float bitWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Log.d("zinc", "mBitmap.getWidth() = " + bitmap.getWidth());
        Log.d("zinc", "mBitmap.getHeight() = " + bitmap.getHeight());

        // 半径
        radius = width * 1.0f / 2;

        float scale = width * 1.0f / bitWidth;
        Log.d("zinc", "scale = " + scale);

        // 设置缩放
        matrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);

        if (rectF == null) {
            rectF = new RectF(0, 0, width, width);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (type == 1) {
            canvas.drawRect(0, 0, width, width, paint);
        } else if (type == 2) {
            canvas.drawRoundRect(rectF, 20, 20, paint);
        }
        canvas.drawCircle(radius, radius, radius, paint);

    }

    private int type;

    public void setType(int type) {
        this.type = type;
        invalidate();
    }

}
