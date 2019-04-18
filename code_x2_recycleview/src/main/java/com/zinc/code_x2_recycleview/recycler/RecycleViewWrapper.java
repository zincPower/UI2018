package com.zinc.code_x2_recycleview.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/27
 * @description
 */
public class RecycleViewWrapper extends RecyclerView {

    private LayoutListener mLayoutListener;

    public RecycleViewWrapper(Context context) {
        super(context);
    }

    public RecycleViewWrapper(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleViewWrapper(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLayoutListener(LayoutListener layoutListener) {
        this.mLayoutListener = layoutListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mLayoutListener != null) {
            mLayoutListener.beforeLayout();
        }

        super.onLayout(changed, l, t, r, b);

        if (mLayoutListener != null) {
            mLayoutListener.afterLayout();
        }
    }

    public interface LayoutListener {
        void beforeLayout();

        void afterLayout();
    }
}
