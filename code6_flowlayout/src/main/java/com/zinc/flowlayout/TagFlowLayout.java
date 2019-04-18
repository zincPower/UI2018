package com.zinc.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : Jiang zinc
 * time         : 2019/3/26 下午11:54
 * email        : 56002982@qq.com
 * desc         : 标签流式布局
 * version      : 1.0.0
 */
public class TagFlowLayout extends ViewGroup {

    private String TAG = "FlowLayout";

    // 测量次数
    private int onMeasureCount = 0;
    // 摆放次数
    private int onLayoutCount = 0;

    // 保存每行的每个View
    private List<List<View>> mRowViewList = new ArrayList<>();
    // 每行的高度
    private List<Integer> mRowHeightList = new ArrayList<>();
    // 当前行的View
    private List<View> mCurLineViewList = new ArrayList<>();

    public TagFlowLayout(Context context) {
        this(context, null, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 注意：必须要重写这个方法，因为子视图需要获取其 margin
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.i(TAG, "onMeasure: " + onMeasureCount++);

        // 会进行两次绘制，所以需要先进行清理
        mRowViewList.clear();
        mRowHeightList.clear();
        mCurLineViewList.clear();

        // 获取 自身的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 获取 自身的宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - (getPaddingLeft() + getPaddingRight());
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // TagFlowLayout控件的宽高，用于确定自己的大小
        int myselfMeasureWidth = 0;
        int myselfMeasureHeight = 0;

        // 每一行的宽高
        int lineWidth = 0;
        int lineHeight = 0;

        // 获取子视图个数
        int childCount = getChildCount();

        // 遍历获取子视图的信息
        for (int i = 0; i < childCount; ++i) {

            // 1、获取子视图
            View childView = getChildAt(i);

            // 2、子视图进行测量
            measureChildWithMargins(childView, widthMeasureSpec, 0,
                    heightMeasureSpec, 0);

            // 3、获取子视图宽高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            // 4、子视图真正占的大小（需要加上其margin）
            // 此处要使用MarginLayoutParams，则必须重写generateLayoutParams方法
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int childRealWidth = childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            int childRealHeight = childHeight + layoutParams.topMargin + layoutParams.bottomMargin;

            // 进行判读是否 加上当前 子视图会 导致超出行宽
            if (lineWidth + childRealWidth > widthSize) {

                // 获取最大的宽值
                myselfMeasureWidth = Math.max(myselfMeasureWidth, lineWidth);
                // 保存高
                myselfMeasureHeight += lineHeight;

                // 保存行的view数据
                mRowViewList.add(mCurLineViewList);
                // 保存行高
                mRowHeightList.add(lineHeight);

                // 重新开辟一个list，保存新的行view
                mCurLineViewList = new ArrayList<>();
                mCurLineViewList.add(childView);

                // 重置行宽、高
                lineWidth = childRealWidth;
                lineHeight = childRealHeight;

            } else {

                // 保存行view
                mCurLineViewList.add(childView);
                // 增加行宽 和 保存 行高最大值
                lineWidth += childRealWidth;
                lineHeight = Math.max(lineHeight, childRealHeight);

            }

            // 最后一行数据要进行保存
            if (i == childCount - 1) {
                // 加上 padding
                myselfMeasureWidth = Math.max(myselfMeasureWidth, lineWidth)
                        + getPaddingRight() + getPaddingLeft();

                myselfMeasureHeight += lineHeight;
                mRowViewList.add(mCurLineViewList);
                mRowHeightList.add(lineHeight);
            }

        }

        // 如果是 match_parent 则直接使用 宽 高 尺寸
        if (widthMode == MeasureSpec.EXACTLY) {
            myselfMeasureWidth = widthSize + getPaddingRight() + getPaddingLeft();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            myselfMeasureHeight = heightSize;
        } else {
            myselfMeasureHeight += getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(myselfMeasureWidth, myselfMeasureHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.i(TAG, "onLayout: " + onLayoutCount++);

        int left, top, right, bottom;

        int curTop = getPaddingTop();
        int curLeft = getPaddingLeft();

        int lineSize = mRowViewList.size();
        // 遍历每行
        for (int i = 0; i < lineSize; ++i) {

            List<View> lineView = mRowViewList.get(i);
            int viewSize = lineView.size();
            for (int j = 0; j < viewSize; ++j) {
                View view = lineView.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();

                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;
                right = left + view.getMeasuredWidth();
                bottom = top + view.getMeasuredHeight();

                view.layout(left, top, right, bottom);

                curLeft += view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }

            // 每行重置
            curLeft = getPaddingLeft();
            curTop += mRowHeightList.get(i);

        }

        mRowViewList.clear();
        mRowHeightList.clear();
    }

}
