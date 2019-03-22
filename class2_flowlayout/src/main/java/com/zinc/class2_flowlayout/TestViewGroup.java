package com.zinc.class2_flowlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * author       : zinc
 * time         : 2019/3/18 上午11:30
 * desc         :
 * version      :
 */
public class TestViewGroup extends FrameLayout {


    public TestViewGroup(Context context) {
        super(context);
    }

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public TestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        String widthModeString = "";
        String heightModeString = "";

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                widthModeString = "EXACTLY";
                break;
            case MeasureSpec.AT_MOST:
                widthModeString = "AT_MOST";
                break;
            case MeasureSpec.UNSPECIFIED:
                widthModeString = "UNSPECIFIED";
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                heightModeString = "EXACTLY";
                break;
            case MeasureSpec.AT_MOST:
                heightModeString = "AT_MOST";
                break;
            case MeasureSpec.UNSPECIFIED:
                heightModeString = "UNSPECIFIED";
                break;
        }

        Log.i("zincPower",
                "[widthMode: " + widthModeString + "\n" +
                        "heightMode: " + heightModeString + "\n" +
                        "widthSize: " + widthSize + "\n" +
                        "heightSize: " + heightSize + "]"
        );

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i("zincPower", "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("zincPower", "onDraw: ");
        super.onDraw(canvas);
        Log.i("zincPower", "onDraw: ");
    }
}
