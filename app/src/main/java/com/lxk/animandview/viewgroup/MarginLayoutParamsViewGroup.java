package com.lxk.animandview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * @author https://github.com/103style
 * @date 2020/4/22 10:55
 * 实现了创建布局参数 MarginLayoutParams 的方法
 */
public abstract class MarginLayoutParamsViewGroup extends ViewGroup {

    public MarginLayoutParamsViewGroup(Context context) {
        super(context);
    }

    public MarginLayoutParamsViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarginLayoutParamsViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
