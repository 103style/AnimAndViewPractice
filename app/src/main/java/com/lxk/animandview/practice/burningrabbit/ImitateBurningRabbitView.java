package com.lxk.animandview.practice.burningrabbit;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.NestedScrollingParent;

import com.lxk.animandview.R;

/**
 * @author https://github.com/103style
 * @date 2020/4/21 13:37
 * <p>
 * 模仿燃兔的列表滑动效果
 */
public class ImitateBurningRabbitView extends ViewGroup implements NestedScrollingParent {

    private View topBarView, bottomBarView, closeBarView;


    public ImitateBurningRabbitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeArray(context, attrs);
    }

    private void initTypeArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImitateBurningRabbitView);
        int resId = typedArray.getResourceId(R.styleable.ImitateBurningRabbitView_top_bar_view, 0);
        if (resId != 0) {
            topBarView = LayoutInflater.from(context).inflate(resId, this, false);
        }
        resId = typedArray.getResourceId(R.styleable.ImitateBurningRabbitView_bottom_bar_view, 0);
        if (resId != 0) {
            bottomBarView = LayoutInflater.from(context).inflate(resId, this, false);
        }
        resId = typedArray.getResourceId(R.styleable.ImitateBurningRabbitView_close_bar_view, 0);
        if (resId != 0) {
            closeBarView = LayoutInflater.from(context).inflate(resId, this, false);
        }
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (topBarView != null) {
            addView(topBarView);
        }
    }

    public void normalState() {
        removeView(bottomBarView);
        removeView(closeBarView);
        if (bottomBarView != null) {
            addView(bottomBarView);
        }
    }

    public void linearState() {
        removeView(bottomBarView);
        removeView(closeBarView);

        if (closeBarView != null) {
            addView(closeBarView);
        }
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            //子view想要多高,就给它多高
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        measureOther(widthMeasureSpec, heightMeasureSpec, topBarView, bottomBarView, closeBarView);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    private void measureOther(int widthMeasureSpec, int heightMeasureSpec, View... views) {
        if (views.length == 0) {
            return;
        }
        for (View v : views) {
            if (v != null) {
                measureChild(v, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    private int getTopViewHeight() {
        if (topBarView == null) {
            return 0;
        }
        MarginLayoutParams lp = (MarginLayoutParams) topBarView.getLayoutParams();
        return lp.topMargin + lp.bottomMargin + topBarView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topStart = getTopViewHeight();
        //类似FrameLayout
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int left = l + lp.leftMargin;
            int top = topStart + lp.topMargin;
            int right = left + child.getMeasuredWidth() + lp.leftMargin;
            int bottom = top + child.getMeasuredHeight() + lp.bottomMargin;
            child.layout(left, top, right, bottom);
        }
        if (topBarView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) topBarView.getLayoutParams();
            int left = l + lp.leftMargin;
            int top = t + lp.topMargin;
            int right = left + topBarView.getMeasuredWidth() - lp.rightMargin;
            int bottom = top + topBarView.getMeasuredHeight();
            topBarView.layout(left, top, right, bottom);
        }
        if (bottomBarView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) bottomBarView.getLayoutParams();
            int left = l + lp.leftMargin;
            int top = b - lp.bottomMargin - bottomBarView.getMeasuredHeight();
            int right = left + bottomBarView.getMeasuredWidth() - lp.rightMargin;
            int bottom = b - lp.bottomMargin;
            bottomBarView.layout(left, top, right, bottom);
        }
        if (closeBarView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) closeBarView.getLayoutParams();
            int left = l + lp.leftMargin;
            int top = b - lp.bottomMargin - closeBarView.getMeasuredHeight();
            int right = left + closeBarView.getMeasuredWidth() - lp.rightMargin;
            int bottom = b - lp.bottomMargin;
            closeBarView.layout(left, top, right, bottom);
        }
    }

    public View getTopBarView() {
        return topBarView;
    }

    public View getBottomBarView() {
        return bottomBarView;
    }

    public View getCloseBarView() {
        return closeBarView;
    }
}
