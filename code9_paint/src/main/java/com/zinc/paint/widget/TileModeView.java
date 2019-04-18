package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.paint.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/18
 * @description TileMode
 */
public class TileModeView extends View {
    private Paint paint;

    private BitmapShader clampShader;
    private BitmapShader mirrorShader;
    private BitmapShader repeatShader;

    private ShapeDrawable shapeDrawable;

    private Matrix matrix;

    private int width;
    private int height;

    public TileModeView(Context context) {
        this(context, null, 0);
    }

    public TileModeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.xyjy2)).getBitmap();
        clampShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mirrorShader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        repeatShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        paint = new Paint();
        paint.setShader(mirrorShader);
        paint.setAntiAlias(true);

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        float scale = Math.max(width, height) / Math.min(width, height);

        matrix = new Matrix();
        matrix.setScale(scale, scale);
        clampShader.setLocalMatrix(matrix);
        mirrorShader.setLocalMatrix(matrix);
        repeatShader.setLocalMatrix(matrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    public void setMode(int type) {
        switch (type) {
            case 0:
                paint.setShader(clampShader);
                break;
            case 1:
                paint.setShader(mirrorShader);
                break;
            case 2:
                paint.setShader(repeatShader);
                break;
        }
        invalidate();
    }
}
