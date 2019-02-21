package com.zinc.class15_velocitytracker_scroller.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/21
 * @description
 */
public class VelocityTrackerView extends View {

    private VelocityTracker mVelocityTracker;

    public VelocityTrackerView(Context context) {
        this(context, null, 0);
    }

    public VelocityTrackerView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public VelocityTrackerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

    }

}
