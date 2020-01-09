package com.lxk.animandcustomviewdemo.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author https://github.com/103style
 * @date 2019/12/10 13:30
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 提取边距
     */
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        //记录每一行的宽高
        int lineWidth = 0, lineHeight = 0;
        //记录整个FlowLayout内容所占高度
        int height = 0, width = 0;

        //计算内容的宽高
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            //int cW = layoutParams.leftMargin + layoutParams.rightMargin + child.getMeasuredWidth();
            int cWidth = layoutParams.getMarginEnd() + layoutParams.getMarginStart() + child.getMeasuredWidth();
            int cHeight = layoutParams.topMargin + layoutParams.bottomMargin + child.getMeasuredHeight();


            if (cWidth + lineWidth > measureWidth) {
                //超出剩余宽度
                //计算当前行的宽高
                width = Math.max(width, lineWidth);
                height += lineHeight;

                //新的一行的占有宽高
                lineHeight = cHeight;
                lineWidth = cWidth;
            } else {
                //未超出宽度 计算现有行的宽高
                lineHeight = Math.max(lineHeight, cHeight);
                lineWidth += cWidth;
            }

            //因为我们在超出宽度之后才新增的高度
            //因为最后一行确认是没有超过宽度的，所以需要在增加最后一行的高度
            if (i + 1 == childCount) {
                height += lineHeight;
                width = Math.max(lineWidth, width);
            }
        }

        //根据宽高的限制符 来设置对应的宽高
        setMeasuredDimension(measureWidthMode == MeasureSpec.EXACTLY ? measureWidth : width,
                measureHeightMode == MeasureSpec.EXACTLY ? measureHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        //累加当前行的行 宽高
        int lineWidth = 0, lineHeight = 0;
        //当前添加元素的右上角坐标的
        int top = 0, left = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            //子元素占据的宽高L
            //int cW = layoutParams.leftMargin + layoutParams.rightMargin + child.getMeasuredWidth();
            int cWidth = layoutParams.getMarginEnd() + layoutParams.getMarginStart() + child.getMeasuredWidth();
            int cHeight = layoutParams.topMargin + layoutParams.bottomMargin + child.getMeasuredHeight();

            if (lineWidth + cWidth > getMeasuredWidth()) {
                //重置为 开始到下一行的最左侧
                left = 0;
                top += lineHeight;
                //计算当前View 的 right 和 bottom
                lineWidth = cWidth;
                lineHeight = cHeight;
            } else {
                lineWidth += cWidth;
                lineHeight = Math.max(lineHeight, cHeight);
            }

            //子元素内容对应的坐标
            int ll = left + layoutParams.leftMargin;
            int lt = top + layoutParams.topMargin;
            int lr = ll + child.getMeasuredWidth();
            int lb = lt + child.getMeasuredHeight();
            child.layout(ll, lt, lr, lb);

            //更新left的值
            left += cWidth;
        }
    }
}
