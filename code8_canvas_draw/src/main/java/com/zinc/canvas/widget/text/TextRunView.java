package com.zinc.canvas.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zinc.canvas.widget.draw.BaseDrawView;

/**
 * author       : zinc
 * time         : 2019/4/22 下午5:05
 * desc         :
 * version      :
 */
public class TextRunView extends BaseDrawView {

    private static final String CONTENT_S = "عرفي تحكي عربي";

    private static final char[] CONTENT = CONTENT_S.toCharArray();

    private int mTextSize;

    public TextRunView(Context context) {
        super(context);
    }

    public TextRunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextRunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        mTextSize = dpToPx(14);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x = -100;

        mPaint.setColor(mColor1);
        mPaint.setTextSize(mTextSize);
        canvas.drawText(CONTENT, 0, CONTENT.length, x, -300, mPaint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canvas.drawTextRun(CONTENT, 0, CONTENT.length,
                    0, CONTENT.length, x, -200, false, mPaint);
        }

//        mPaint.setColor(mColor2);
//        mPaint.setTextSize(mTextSize);
//        canvas.drawText(CONTENT, 0, CONTENT.length - 1, x, 0, mPaint);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            canvas.drawTextRun(CONTENT, 0, CONTENT.length - 1,
//                    0, CONTENT.length - 1, x, 100, false, mPaint);
//        }

        mPaint.setColor(mColor1);
        mPaint.setTextSize(mTextSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canvas.drawTextRun(CONTENT, 1, CONTENT.length - 2,
                    0, CONTENT.length, x, 300, true, mPaint);
        }

    }

}
