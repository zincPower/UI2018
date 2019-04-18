package com.zinc.drawable_gravity;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/29
 * @description
 */
public class RevealDrawable extends Drawable {

    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    private Drawable unselectedDrawable;
    private Drawable selectedDrawable;
    //方向
    private int orientation;

    // 比率
    private float ratio;

    private int leftWidth;
    private int rightWidth;

    private int topHeight;
    private int bottomHeight;

    private Rect rect;

    public RevealDrawable(Drawable unselectedDrawable,
                          Drawable selectedDrawable,
                          int orientation) {
        this.unselectedDrawable = unselectedDrawable;
        this.selectedDrawable = selectedDrawable;
        this.orientation = orientation;
        this.rect = new Rect();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        Log.i("RevealDrawable", "draw: ");
        // 在onLevelChange时，bounds中还没有值
        Rect bounds = getBounds();

        if (orientation == HORIZONTAL) {      // 横向

            leftWidth = (int) (ratio * bounds.width());
            rightWidth = bounds.width() - leftWidth;
            topHeight = bounds.height();
            bottomHeight = bounds.height();

        } else if (orientation == VERTICAL) {  // 纵向

            leftWidth = bounds.width();
            rightWidth = bounds.width();
            topHeight = (int) (ratio * bounds.height());
            bottomHeight = bounds.height() - topHeight;

        }

        Log.i("onLevelChange", "ratio:" + ratio + "; " +
                "leftWidth:" + leftWidth + "; " +
                "topHeight:" + topHeight + "; " +
                "rightHeight:" + rightWidth + "; " +
                "bottomHeight:" + bottomHeight);

        Gravity.apply(Gravity.LEFT,
                leftWidth,
                topHeight,
                bounds,
                rect);

        canvas.save();
        canvas.clipRect(rect);
        unselectedDrawable.draw(canvas);
        canvas.restore();


        Gravity.apply(Gravity.RIGHT,
                rightWidth,
                bottomHeight,
                bounds,
                rect
        );

        canvas.save();
        canvas.clipRect(rect);
        selectedDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        unselectedDrawable.setBounds(bounds);
        selectedDrawable.setBounds(bounds);
        Log.i("onBoundsChange", "width:" + bounds.width() +
                "; height:" + bounds.height());
    }

    @Override
    public int getIntrinsicHeight() {
        return Math.max(selectedDrawable.getIntrinsicHeight(),
                unselectedDrawable.getIntrinsicHeight());
    }

    @Override
    public int getIntrinsicWidth() {
        return Math.max(selectedDrawable.getIntrinsicWidth(),
                unselectedDrawable.getIntrinsicWidth());
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    /**
     * 当调用了 {@link Drawable#setLevel(int)} 时，会回调这个方法
     *
     * @param level 取值范围为 0-10000
     * @return
     */
    @Override
    protected boolean onLevelChange(int level) {

        // 计算比率
        ratio = level / 10000f;

        // 刷新自己，会调用draw方法
        invalidateSelf();

        return true;
    }

    /**
     * 获取透明度
     *
     * @return
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}
