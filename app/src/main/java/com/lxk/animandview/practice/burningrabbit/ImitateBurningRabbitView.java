package com.lxk.animandview.practice.burningrabbit;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.R;
import com.lxk.animandview.viewgroup.MarginLayoutParamsViewGroup;

/**
 * @author https://github.com/103style
 * @date 2020/4/21 13:37
 * <p>
 * 模仿燃兔的列表滑动效果
 * 子view的布局类似FrameLayout  仅限两个
 */
public class ImitateBurningRabbitView extends MarginLayoutParamsViewGroup implements NestedScrollingParent {

    private static final String TAG = "BurningRabbitView";

    float preY;
    /**
     * 当前视图的状态
     */
    private int currentState = IState.ViewGroupState.NORMAL;
    /**
     * 下层的子view、上层的子View、 两个子view上面的视图
     */
    private View backChildView, frontChildView, topChildView;
    /**
     * 通过属性配置的三个视图： 顶部的渐变视图、底部的视图、下拉frontChildView 显示的 关闭视图
     */
    private View topBarView, bottomBarView, pullOutView;

    public ImitateBurningRabbitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImitateBurningRabbitView);
        topBarView = getInflaterView(typedArray, R.styleable.ImitateBurningRabbitView_ibr_top_bar_view);
        bottomBarView = getInflaterView(typedArray, R.styleable.ImitateBurningRabbitView_ibr_bottom_bar_view);
        pullOutView = getInflaterView(typedArray, R.styleable.ImitateBurningRabbitView_ibr_pull_out_view);
        typedArray.recycle();
    }

    private View getInflaterView(TypedArray typedArray, int index) {
        int resId = typedArray.getResourceId(index, 0);
        if (resId != 0) {
            return LayoutInflater.from(getContext()).inflate(resId, this, false);
        } else {
            return null;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        backChildView = findViewById(R.id.back_view);
        frontChildView = findViewById(R.id.front_view);
        topChildView = findViewById(R.id.top_view);
        if (topBarView != null) {
            addView(topBarView);
            topBarView.setAlpha(0);
        }
        if (pullOutView != null) {
            addView(pullOutView);
        }
        if (bottomBarView != null) {
            addView(bottomBarView);
        }
    }

    public View getBottomBarView() {
        return bottomBarView;
    }

    public View getPullOutView() {
        return pullOutView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();
    }

    private void layoutChildren() {
        layoutTopBar();
        layoutTopChildView();
        layoutBackAndFrontChildView();
        layoutBottomBarAndPullOutView();
    }

    private void layoutTopBar() {
        if (topBarView == null) {
            return;
        }
        MarginLayoutParams lp = (MarginLayoutParams) topBarView.getLayoutParams();
        int top = lp.topMargin + getScrollY();
        topBarView.layout(getLeft() + lp.leftMargin, top, getRight() - lp.rightMargin,
                top + topBarView.getMeasuredHeight());
    }

    private void layoutTopChildView() {
        if (topChildView == null) {
            return;
        }
        MarginLayoutParams lp = (MarginLayoutParams) topChildView.getLayoutParams();
        int top = lp.topMargin;
        topChildView.layout(getLeft() + lp.leftMargin, top,
                getRight() - lp.rightMargin,
                top + topChildView.getMeasuredHeight());
    }

    private int getTopChildViewLayoutHeight() {
        if (topChildView == null) {
            return 0;
        }
        MarginLayoutParams lp = (MarginLayoutParams) topChildView.getLayoutParams();
        return lp.topMargin + lp.bottomMargin + topChildView.getMeasuredHeight();
    }

    private void layoutBackAndFrontChildView() {
        layoutXmlChildView(backChildView);
        layoutXmlChildView(frontChildView);
    }

    private void layoutXmlChildView(View view) {
        if (view == null || view.getVisibility() != VISIBLE) {
            return;
        }
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        int top = getTopChildViewLayoutHeight() + lp.topMargin;
        view.layout(getLeft() + lp.leftMargin, top, getRight() - lp.rightMargin,
                top + view.getMeasuredHeight());
    }

    private void layoutBottomBarAndPullOutView() {
        layoutBottomView(bottomBarView, isNormalState());
        layoutBottomView(pullOutView, !isNormalState());
    }

    private boolean isNormalState() {
        return currentState == IState.ViewGroupState.NORMAL;
    }

    private void layoutBottomView(View view, boolean show) {
        if (view != null) {
            MarginLayoutParams lp = (MarginLayoutParams) bottomBarView.getLayoutParams();
            int bottom;
            if (show) {
                bottom = getBottom() + getScrollY() - lp.bottomMargin;
            } else {
                bottom = getBottom() + getScrollY() + lp.topMargin + view.getMeasuredHeight();
            }
            view.layout(getLeft() + lp.leftMargin,
                    bottom - view.getMeasuredHeight(),
                    getRight() - lp.rightMargin,
                    bottom);
        }
    }

    public void changeState(int state) {
        currentState = state;
        if (isNormalState()) {
            updateVisibility(GONE, pullOutView);
            updateVisibility(VISIBLE, bottomBarView);
        } else {
            updateVisibility(GONE, bottomBarView);
            updateVisibility(VISIBLE, pullOutView);
        }
        layoutChildren();
        scrollBy(0, -getScrollY());
    }

    private void updateVisibility(int visibility, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(visibility);
            }
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        if (y + getScrollY() < 0) {
            y = -getScrollY();
        }
        super.scrollBy(x, y);
        layoutChildren();

        if (topBarView != null) {
            int h = topBarView.getMeasuredHeight();
            float alpha = 1.0f * (getScrollY() - h) / h;
            topBarView.setAlpha(alpha >= 1 ? 1.0f : alpha);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
        return true;
    }

    private boolean checkShowTop(@NonNull View target, boolean showTop) {
        if (!showTop || !(target instanceof RecyclerView)) {
            return false;
        }
        RecyclerView.LayoutManager layoutManager = ((RecyclerView) target).getLayoutManager();
        if (layoutManager == null) {
            return true;
        }
        View view = layoutManager.getChildAt(0);
        if (view != null) {
            showTop = layoutManager.isViewPartiallyVisible(view, true, true);
        }
        return showTop;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.e(TAG, "onNestedPreScroll: target = " + target.getClass().getSimpleName() + ", dy = " + dy);
        boolean hideTop = getScrollY() < getTopChildViewLayoutHeight() && dy > 0;
        boolean showTop = getScrollY() > 0 && dy < 0;
        showTop = checkShowTop(target, showTop);
        if (hideTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }
}
