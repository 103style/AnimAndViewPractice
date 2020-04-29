package com.lxk.animandview.practice.burningrabbit;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.R;
import com.lxk.animandview.utils.DensityUtils;
import com.lxk.animandview.viewgroup.MarginLayoutParamsViewGroup;

/**
 * @author https://github.com/103style
 * @date 2020/4/21 13:37
 * <p>
 * 模仿燃兔的列表滑动效果
 * 参考： https://blog.csdn.net/u011387817/article/details/79552699
 * <p>
 * 子view的布局类似FrameLayout  仅限两个
 * <p>
 */
public class ImitateBurningRabbitView extends MarginLayoutParamsViewGroup {

    private static final String TAG = "BurningRabbitView";
    /**
     * 过渡动画时长
     */
    private static final int ANIMATION_DURATION = 500;
    /**
     * 之前触摸事件的Y坐标
     */
    private int preX, preY;
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
    private View topBarView, bottomBarView, pullOutBottomView;

    /**
     * backChildView 和 frontChildView 中间的视图
     */
    private View middleView;
    private int middleViewColor;

    /**
     * 滑动速度测量
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 是否在拖动状态
     */
    private boolean isBeingDragged;

    private Scroller mScroller;

    /**
     * 顶部视图的高度
     */
    private int mTopViewHeight;

    /**
     * 触发拉出后面的视图 的阈值
     */
    private int pulloutOffsetThreshold;

    /**
     * 前面视图的当前偏移量,背后视图的当前偏移量,头部的当前偏移量
     */
    private int mFrontViewOffset, mBackViewOffset, mTopViewOffset;
    /**
     * 记录Scroller上次的y坐标
     */
    private int mScrollOffset;
    /**
     * 是否是新的惯性滑动
     */
    private boolean isNewScroll;
    private boolean isScrolling;
    /**
     * 最短滑动距离
     */
    private int touchSlop;
    private boolean interceptEvent;

    public ImitateBurningRabbitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImitateBurningRabbitView);
        pulloutOffsetThreshold = typedArray.getDimensionPixelOffset(R.styleable.ImitateBurningRabbitView_ibr_pull_out_threshold, DensityUtils.dpToPx(context, 32));
        middleViewColor = typedArray.getColor(R.styleable.ImitateBurningRabbitView_ibr_middle_view_color, Color.WHITE);
        topBarView = getInflaterView(typedArray, R.styleable.ImitateBurningRabbitView_ibr_top_bar_view);
        topChildView = getInflaterView(typedArray, R.styleable.ImitateBurningRabbitView_ibr_top_view);
        bottomBarView = getInflaterView(typedArray, R.styleable.ImitateBurningRabbitView_ibr_bottom_bar_view);
        pullOutBottomView = getInflaterView(typedArray, R.styleable.ImitateBurningRabbitView_ibr_pull_out_bottom_view);
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

    /**
     * 添加 通过属性配置的视图
     */
    @Override
    public void addView(View child, int index, LayoutParams params) {
        //加载xml布局中的子view时调用
        switch (getChildCount()) {
            case 0:
                //第一个xml子view设置为 backChildView
                backChildView = child;
                if (child != null) {
                    super.addView(child, index, params);
                }
                middleView = new View(getContext());
                middleView.setLayoutParams(new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                middleView.setBackgroundColor(middleViewColor);
                addConfigView(middleView, index);
                return;
            case 2:
                //第二个xml子view设置为 frontChildView
                frontChildView = child;
                if (child != null) {
                    super.addView(child, index, params);
                }
                //添加通过属性配置的 下拉 frontChildView 显示的 pulloutview
                addConfigView(pullOutBottomView, index);
                //添加通过属性配置的 显示在 backChildView 和 frontChildView 上面的视图
                addConfigView(topChildView, index);
                //添加通过属性配置的 顶部渐变的视图
                addConfigView(topBarView, index);
                //添加通过属性配置的 底部的视图
                addConfigView(bottomBarView, index);
                return;
            default:
                throw new IllegalStateException("CoffinLayout child can't > 2");
        }
    }

    private void addConfigView(View view, int index) {
        if (view != null) {
            LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = generateDefaultLayoutParams();
            }
            super.addView(view, index, layoutParams);
        }
    }

    public View getBottomBarView() {
        return bottomBarView;
    }

    public View getPullOutBottomView() {
        return pullOutBottomView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        mTopViewHeight = getTopChildViewLayoutHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
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
        int top = lp.topMargin;
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
        return getViewSpaceHeight(topChildView);
    }

    /**
     * 获取view所占空间的高度
     */
    private int getViewSpaceHeight(View view) {
        if (view == null) {
            return 0;
        }
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        return lp.topMargin + lp.bottomMargin + view.getMeasuredHeight();
    }

    private void layoutBackAndFrontChildView() {
        layoutXmlChildView(backChildView, 0);
        layoutXmlChildView(frontChildView, Math.max(mTopViewHeight, mFrontViewOffset));
        layoutXmlChildView(middleView, mTopViewHeight);
    }

    private void layoutXmlChildView(View view, int offset) {
        if (view == null || view.getVisibility() != VISIBLE) {
            return;
        }
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        int top = offset + lp.topMargin;
        view.layout(getLeft() + lp.leftMargin, top, getRight() - lp.rightMargin,
                top + view.getMeasuredHeight());
    }

    private void layoutBottomBarAndPullOutView() {
        layoutBottomView(bottomBarView, isNormalState());
        layoutBottomView(pullOutBottomView, isPullOutState());
    }

    private boolean isNormalState() {
        return currentState == IState.ViewGroupState.NORMAL;
    }

    private boolean isPullOutState() {
        return currentState == IState.ViewGroupState.PULL_OUT;
    }

    private void layoutBottomView(View view, boolean show) {
        if (view != null) {
            MarginLayoutParams lp = (MarginLayoutParams) bottomBarView.getLayoutParams();
            int bottom;
            if (show) {
                bottom = getBottom() - lp.bottomMargin;
            } else {
                bottom = getBottom() + lp.topMargin + view.getMeasuredHeight();
            }
            view.layout(getLeft() + lp.leftMargin,
                    bottom - view.getMeasuredHeight(),
                    getRight() - lp.rightMargin,
                    bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrolling) {
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE && interceptEvent) {
            return true;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        //拦截move事件
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下的坐标
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - preX) > touchSlop
                        || Math.abs(y - preY) > touchSlop) {
                    interceptEvent = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                interceptEvent = false;
                break;
        }
        return interceptEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isScrolling) {
            return false;
        }
        mVelocityTracker.addMovement(event);
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isNormalState()) {
                    offsetFront(y - preY);
                } else if (isPullOutState()) {
                    offsetBack(y - preY);
                }
                preY = y;
                //显示或者影藏top bar
                changeTopBarState();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                boolean isHandle = false;
                if (isNormalState()) {
                    //检查 frontChildView 当前位置 是否需要改变状态
                    isHandle = checkoutFrontViewState();
                }
//                if (!isHandle) {
//                    mVelocityTracker.computeCurrentVelocity(1000);
//                    mScroller.fling(0, 0, 0, (int) mVelocityTracker.getYVelocity() / 3,
//                            0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
//                    invalidate();
//                }
                //标记状态
                isBeingDragged = false;
                interceptEvent = false;
                break;
            default:
                break;
        }
        return true;
    }

    private boolean checkoutFrontViewState() {
        if (frontChildView == null) {
            return false;
        }
        int top = frontChildView.getTop();
        Log.e(TAG, "checkoutFrontViewState frontChildView.top = " + top);
        if (top > mTopViewHeight + pulloutOffsetThreshold) {
            doPullOut();
            return true;
        } else if (top > mTopViewHeight) {
            frontChildView.offsetTopAndBottom(mTopViewHeight - top);
            return true;
        }
        return false;
    }

    private void doPullOut() {
        isScrolling = true;
        isNewScroll = true;
        //front view 需要滑动的距离
        int offset = getBottom() - frontChildView.getTop();
        mScroller.startScroll(0, 0, 0, offset, ANIMATION_DURATION);
        currentState = IState.ViewGroupState.PULLING;
        setViewVisible(middleView, false);
        setViewVisible(topBarView, false);
        invalidate();
        doViewStateChangeAnim(bottomBarView, true);
        doViewStateChangeAnim(pullOutBottomView, false);
        hideTopBarAnim();
        hideTopViewAnim();
    }

    public void doBackNormal() {
        isScrolling = true;
        isNewScroll = true;
        //front view 需要滑动的距离
        int offset = mTopViewHeight - frontChildView.getTop();
        mScroller.startScroll(0, 0, 0, offset, ANIMATION_DURATION);
        currentState = IState.ViewGroupState.CLOSING;
        invalidate();
        setViewVisible(middleView, true);
        setViewVisible(topBarView, true);
        doViewStateChangeAnim(bottomBarView, false);
        doViewStateChangeAnim(pullOutBottomView, true);
        showTopViewAnim();
    }

    private void offsetBack(int offset) {
        int dy = -offset;
        boolean pullUp = offset < 0;

        offsetChildView(pullUp, offset);
        if (backChildView == null) {
            return;
        }

        int topGap = backChildView.getTop() - getTop();
        if (pullUp) {
            if (topGap > 0) {
                int t = Math.abs(offset) > topGap ? -topGap : offset;
                backChildView.offsetTopAndBottom(t);
            } else {
                backChildView.scrollBy(0, dy);
            }
        } else {
            if (!checkViewInContentTop(backChildView)) {
                backChildView.scrollBy(0, dy);
            }
        }
        mBackViewOffset = backChildView.getTop();
    }

    private void offsetFront(int offset) {
        int dy = -offset;
        boolean pullUp = offset < 0;

        offsetChildView(pullUp, offset);
        if (frontChildView == null) {
            return;
        }
        int topGap = frontChildView.getTop() - getTop();
        if (pullUp) {
            if (topGap > 0) {
                int t = Math.abs(offset) > topGap ? -topGap : offset;
                frontChildView.offsetTopAndBottom(t);
            } else {
                frontChildView.scrollBy(0, dy);
            }
        } else {
            frontChildView.scrollBy(0, dy);
            if (checkViewInContentTop(frontChildView)) {
                frontChildView.offsetTopAndBottom(offset);
            }
        }
        mFrontViewOffset = frontChildView.getTop();
    }

    private boolean checkViewInContentTop(View view) {
        boolean res = view.getScrollY() == 0;
        if (view instanceof RecyclerView) {
            RecyclerView.LayoutManager lp = ((RecyclerView) view).getLayoutManager();
            if (lp == null) {
                return res;
            }
            View child = lp.getChildAt(0);
            if (child == null) {
                return res;
            }
            return lp.isViewPartiallyVisible(child, true, true);
        }
        return res;
    }

    private void offsetChildView(boolean pullUp, int offset) {
        if (topChildView != null && isNormalState()) {
            if (pullUp && frontChildView != null && frontChildView.getTop() <= mTopViewHeight) {
                //上拉 并且 frontChild 已经和top child 连接在一起
                topChildView.offsetTopAndBottom(offset);
            } else {
                if (topChildView.getTop() < 0) {
                    int t = offset + topChildView.getTop() > 0 ? -topChildView.getTop() : offset;
                    topChildView.offsetTopAndBottom(t);
                }
            }
        }
        setTopBarAndMiddleViewAlpha();
    }

    private void setTopBarAndMiddleViewAlpha() {
        if (!isNormalState()) {
            return;
        }
        float percent = (frontChildView.getTop() - mTopViewHeight) * 0.5f / pulloutOffsetThreshold;
        if (percent > 1F) {
            percent = 1F;
        }
        if (percent < 0) {
            percent = 0;
        }
        percent = 1F - percent;
        if (middleView != null && middleView.getVisibility() == VISIBLE) {
            middleView.setAlpha(percent);
        }
        if (topBarView != null) {
            topBarView.setAlpha(percent);
        }
    }

    private boolean isFrontViewOpeningOrClosing() {
        return currentState == IState.ViewGroupState.PULLING || currentState == IState.ViewGroupState.CLOSING;
    }

    private void doViewStateChangeAnim(View view, boolean hide) {
        if (view == null) {
            return;
        }
        int height = getViewSpaceHeight(view);
        if (hide) {
            startValueAnimation(view, 0, height);
        } else {
            startValueAnimation(view, 0, -height);
        }
    }

    private void changeTopBarState() {
        if (!isNormalState()) {
            return;
        }
        mVelocityTracker.computeCurrentVelocity(1000);
        float velocityY = mVelocityTracker.getYVelocity();
        //根据手指滑动的速率和方向来判断是否要隐藏或显示TopBar
        if (Math.abs(velocityY) > 4000) {
            if (velocityY > 0) {
                showTopBarAnim();
            } else {
                hideTopBarAnim();
            }
        }
    }

    private void hideTopBarAnim() {
        if (topBarView != null) {
            if (topBarView.getTranslationY() == 0) {
                startValueAnimation(topBarView, 0, -getViewSpaceHeight(topBarView));
            }
        }
    }

    private void showTopBarAnim() {
        if (topBarView != null) {
            if (topBarView.getTranslationY() == -getViewSpaceHeight(topBarView)) {
                startValueAnimation(topBarView, -getViewSpaceHeight(topBarView), 0);
            }
        }
    }

    private void hideTopViewAnim() {
        if (topChildView != null) {
            if (topChildView.getTranslationY() == 0) {
                startValueAnimation(topChildView, 0, -getViewSpaceHeight(topChildView));
            }
        }
    }

    private void showTopViewAnim() {
        if (topChildView != null) {
            if (topChildView.getTranslationY() == -getViewSpaceHeight(topChildView)) {
                startValueAnimation(topChildView, -getViewSpaceHeight(topChildView), 0);
            }
        }
    }

    /**
     * 执行动画
     *
     * @param target 要执行动画的view
     * @param startY 开始值
     * @param endY   结束值
     */
    private void startValueAnimation(View target, int startY, int endY) {
        if (target == null) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofInt(startY, endY).setDuration(ANIMATION_DURATION);
        animator.addUpdateListener(animation -> target.setTranslationY((int) animation.getAnimatedValue()));
        animator.start();
    }

    private void setViewVisible(View view, boolean visible) {
        if (view == null) {
            return;
        }
        view.setVisibility(visible ? VISIBLE : GONE);
        view.setAlpha(1F);
    }

    /**
     * 打断滚动动画
     */
    private void abortScrollerAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {

            setTopBarAndMiddleViewAlpha();

            int y = mScroller.getCurrY();
            //是新的一轮则刷新offseta
            if (isNewScroll) {
                isNewScroll = false;
                mScrollOffset = y;
            }
            int offset = y - mScrollOffset;
            if (currentState == IState.ViewGroupState.PULLING) {
                Log.e(TAG, "computeScroll frontChildView.top = " + frontChildView.getTop());
                frontChildView.offsetTopAndBottom(offset);
            } else if (currentState == IState.ViewGroupState.CLOSING) {
                frontChildView.offsetTopAndBottom(offset);
            } else if (currentState == IState.ViewGroupState.PULL_OUT) {
                offsetBack(offset);
            } else if (currentState == IState.ViewGroupState.NORMAL) {
                offsetFront(offset);
            }
            mScrollOffset = y;
            invalidate();

            if (mScroller.isFinished()) {
                isNewScroll = true;
                //滚动结束, 更新状态
                if (currentState == IState.ViewGroupState.PULLING) {
                    currentState = IState.ViewGroupState.PULL_OUT;
                    topChildView.offsetTopAndBottom(-topChildView.getTop());
                } else if (currentState == IState.ViewGroupState.CLOSING) {
                    currentState = IState.ViewGroupState.NORMAL;
                    topChildView.offsetTopAndBottom(-topChildView.getTop());
                    frontChildView.offsetTopAndBottom(mTopViewHeight - frontChildView.getTop());
                }
                mBackViewOffset = backChildView.getTop();
                mFrontViewOffset = frontChildView.getTop();
                isScrolling = false;
            }
        }
    }
}