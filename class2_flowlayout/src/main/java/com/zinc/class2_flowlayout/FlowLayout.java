package com.zinc.class2_flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/9/25
 * @description 流式 布局
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 必须要重写这个方法，因为子视图需要获取其 margin
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    // 保存每行的每个view
    private List<List<View>> lstLineView = new ArrayList<>();
    // 每行的高度
    private List<Integer> lstHeight = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 获取父亲的 模式 和 宽高
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 控件的宽高
        int measureWidth = 0;
        int measureHeight = 0;

        //每一行的宽高
        int lineWidth = 0;
        int lineHeight = 0;

        // 获取子视图个数
        int childCount = getChildCount();

        // 当前 行的 view
        List<View> curLineViewList = new ArrayList<>();

        // 遍历获取子视图的信息
        for (int i = 0; i < childCount; ++i) {

            // 1、获取子视图
            View childView = getChildAt(i);
            // 2、子视图进行测量
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            // 3、获取子视图宽高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            // 4、子视图真正占的大小（需要加上其margin）
            // 此处要使用MarginLayoutParams，则必须重写generateLayoutParams方法
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int childRealWidth = childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            int childRealHeight = childHeight + layoutParams.topMargin + layoutParams.bottomMargin;

            // 进行判读是否 加上当前 子视图会 导致超出行宽
            if (lineWidth + childRealWidth > widthSize) { // 超出行宽

                // 获取最大的宽值
                measureWidth = Math.max(measureWidth, lineWidth);
                // 保存高
                measureHeight += lineHeight;

                // 保存行的view数据
                lstLineView.add(curLineViewList);
                // 保存行高
                lstHeight.add(lineHeight);

                // 重新开辟一个list，保存新的行view
                curLineViewList = new ArrayList<>();
                curLineViewList.add(childView);

                // 重置行宽、高
                lineWidth = childRealWidth;
                lineHeight = childRealHeight;

            } else {

                // 保存行view
                curLineViewList.add(childView);
                // 增加行宽 和 保存 行高最大值
                lineWidth += childRealWidth;
                lineHeight = Math.max(lineHeight, childRealHeight);

            }

            // 最后一行数据要进行保存
            if (i == childCount - 1) {
                measureWidth = Math.max(measureWidth, lineWidth);
                measureHeight += lineHeight;
                lstLineView.add(curLineViewList);
                lstHeight.add(lineHeight);
            }

        }

        // 如果是 match_parent 则直接使用 宽 高 尺寸
        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight = heightSize;
        }

        setMeasuredDimension(measureWidth, measureHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int left, top, right, bottom;

        int curTop = 0;
        int curLeft = 0;

        int lineSize = lstLineView.size();
        // 遍历每行
        for (int i = 0; i < lineSize; ++i) {

            List<View> lineView = lstLineView.get(i);
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
            curLeft = 0;
            curTop += lstHeight.get(i);

        }

        lstLineView.clear();
        lstHeight.clear();

    }

}
