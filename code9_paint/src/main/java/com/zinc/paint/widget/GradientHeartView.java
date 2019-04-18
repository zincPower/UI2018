package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.paint.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/21
 * @description 心形渐变
 */
public class GradientHeartView extends View {

    private int[] gradientColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

    private BitmapShader bitmapShader;

    private Shader curShader;

    private int width, height;

    private Paint paint;

    private ComposeShader linearShader;
    private ComposeShader radialShader;
    private ComposeShader sweepShader;

    public GradientHeartView(Context context) {
        this(context, null, 0);
    }

    public GradientHeartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientHeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        width = bitmap.getWidth();
        height = bitmap.getHeight();

        LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                gradientColors, null, Shader.TileMode.CLAMP);

        RadialGradient radialGradient = new RadialGradient(width / 2, height / 2, 100,
                gradientColors, null, Shader.TileMode.CLAMP);

        SweepGradient sweepGradient = new SweepGradient(width / 2, height / 2, gradientColors, null);

        linearShader = new ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY);
        radialShader = new ComposeShader(bitmapShader, radialGradient, PorterDuff.Mode.MULTIPLY);
        sweepShader = new ComposeShader(bitmapShader, sweepGradient, PorterDuff.Mode.MULTIPLY);

        curShader = linearShader;
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setShader(curShader);
        canvas.drawRect(0, 0, width, height, paint);

    }

    public void setType(int type) {

        switch (type) {
            case 1:
                curShader = radialShader;
                break;
            case 2:
                curShader = sweepShader;
                break;
            default:
                curShader = linearShader;
                break;
        }
        invalidate();

    }
}
