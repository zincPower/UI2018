package com.zinc.code8_canvas_draw.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 上午9:23
 * desc         : 画矩阵
 * version      :
 */
public class RectView extends BaseDrawView {

    public RectView(Context context) {
        super(context);
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // ======================= 圆角矩形 =========================
        canvas.save();
        canvas.translate(0, -mRectF.height() * 3 / 2 - 100);

        setPaint(mColor2, mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(-150, -150, 400,
                    150, 100, 50, mPaint);
        }
        canvas.restore();

        // ======================= 直角矩形 =========================
        canvas.save();

        setPaint(mColor1, mLineWidth);
        canvas.drawRect(mRectF, mPaint);
        
        canvas.restore();

        // ======================= 圆角矩形 =========================
        canvas.save();
        canvas.translate(0, mRectF.height() * 3 / 2 + 100);

        setPaint(mColor2, mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(mRectF, 80, 100, mPaint);
        canvas.restore();

    }
}
