package com.lxk.animandview.practice.burningrabbit;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.R;
import com.lxk.animandview.drawapi.Utils;
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
    private int preY;
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

    public ImitateBurningRabbitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImitateBurningRabbitView);
        pulloutOffsetThreshold = typedArray.getDimensionPixelOffset(R.styleable.ImitateBurningRabbitView_ibr_pull_out_threshold, Utils.dpToPx(context, 32));
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
                break;
            case 1:
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
        super.addView(child, index, params);
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
        int top = lp.topMargin + mTopViewOffset;
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
        layoutXmlChildView(backChildView, mBackViewOffset + mTopViewHeight);
        layoutXmlChildView(frontChildView, mFrontViewOffset + mTopViewHeight);
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
        layoutBottomView(pullOutBottomView, !isNormalState());
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
        dispatchToChild(ev, bottomBarView, pullOutBottomView);
        return true;
    }

    private void dispatchToChild(MotionEvent ev, View... views) {
        for (View view : views) {
            if (view == null) {
                return;
            }
            view.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isNormalState()) {
                    offsetXmlView(frontChildView, y, true);
                } else if (isPullOutState()) {
                    offsetXmlView(backChildView, y, false);
                }
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
                if (!isHandle) {
                    mVelocityTracker.computeCurrentVelocity(1000);
                    mScroller.fling(0, 0, 0, (int) mVelocityTracker.getYVelocity(),
                            0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    invalidate();
                }
                //标记状态
                isBeingDragged = false;
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
        currentState = IState.ViewGroupState.PULLING;
        //front view 需要滑动的距离
        int offset = getBottom() + 100 - frontChildView.getTop();
        isNewScroll = true;
        mScroller.startScroll(0, 0, 0, offset, ANIMATION_DURATION);
        invalidate();
        doViewStateChangeAnim(bottomBarView, true);
        doViewStateChangeAnim(pullOutBottomView, false);
    }

    public void doBackNormal() {
        currentState = IState.ViewGroupState.CLOSING;
        //front view 需要滑动的距离
        int offset = mTopViewHeight - frontChildView.getTop();
        isNewScroll = true;
        mScroller.startScroll(0, offset, 0, 0, ANIMATION_DURATION);
        invalidate();
        doViewStateChangeAnim(pullOutBottomView, true);
        doViewStateChangeAnim(bottomBarView, false);
    }


    private void changeTopBarState() {
        if (!isNormalState()) {
            return;
        }
        mVelocityTracker.computeCurrentVelocity(1000);
        float velocityY = mVelocityTracker.getYVelocity();
        //根据手指滑动的速率和方向来判断是否要隐藏或显示TopBar
        if (Math.abs(velocityY) > 4000 && topBarView != null) {
            if (velocityY > 0) {
                if (topBarView.getTranslationY() == -getViewSpaceHeight(topBarView)) {
                    startValueAnimation(topBarView, -getViewSpaceHeight(topBarView), 0);
                }
            } else {
                if (topBarView.getTranslationY() == 0) {
                    startValueAnimation(topBarView, 0, -getViewSpaceHeight(topBarView));
                }
            }
        }
    }

    private void offsetXmlView(View view, int y, boolean front) {
        int offset = y - preY;
        int dy = -offset;
        boolean pullUp = offset < 0;

        offsetChildView(pullUp, offset);

        if (view == null) {
            return;
        }

        int topGap = view.getTop() - getTop();
        if (pullUp) {
            if (topGap > 0) {
                int t = Math.abs(offset) > topGap ? -topGap : offset;
                view.offsetTopAndBottom(t);
            } else {
                view.scrollBy(0, dy);
            }
        } else {
            view.scrollBy(0, dy);
            if (checkViewInContentTop(view)) {
                view.offsetTopAndBottom(offset);
                if (!front) {
                    if (view.getTop() > mTopViewHeight) {
                        view.offsetTopAndBottom(mTopViewHeight - view.getTop());
                    }
                }
            }
        }
        preY = y;
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
        if (topChildView != null) {
            if (pullUp) {
                topChildView.offsetTopAndBottom(offset);
            } else {
                if (topChildView.getTop() < 0) {
                    int t = offset + topChildView.getTop() > 0 ? -topChildView.getTop() : offset;
                    topChildView.offsetTopAndBottom(t);
                }
            }
        }
    }


//    private void offsetChildView(int offset) {
//        if (!isFrontViewOpeningOrClosing() && frontChildView.getTop() < getTopChildViewLayoutHeight()) {
//            //不是正在打开或关闭状态 并且  前面视图的 顶部 小于触发 拉出后面视图的高度
//            int bottomViewOffset = offset / 2;//损失一半
//            //判断越界
//            if (backChildView.getTop() > getTop() || backChildView.getTop() + bottomViewOffset > getTop()) {
//                bottomViewOffset = getTop() - backChildView.getTop();
//            }
//            //更新BottomView和HeaderView的位置
//            mBackViewOffset += bottomViewOffset;
//            backChildView.offsetTopAndBottom(bottomViewOffset);
//            mTopViewOffset += bottomViewOffset;
//            topChildView.offsetTopAndBottom(bottomViewOffset);
////            mTransitionView.offsetTopAndBottom(-bottomViewOffset);
//        }
//        //更新棺材盖的位置
//        mFrontViewOffset += offset;
//        frontChildView.offsetTopAndBottom(offset);
////        //更新TopBar的透明度
////        float percent = (float) mLidViewOffset / (getBottom() - mLidOffset);
////        mTransitionView.setAlpha(1F - percent);
////        percent = (float) (mLidView.getTop() - mTopBar.getHeight()) / (mLidOffset - mTopBar.getHeight());
////        if (percent > 1F) {
////            percent = 1F;
////        }
////        if (percent < 0) {
////            percent = 0;
////        }
////        setTopBarBackgroundAlpha(percent);
//    }

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


    //    /**
//     * 打断滚动动画
//     */
//    private void abortScrollerAnimation() {
//        if (!mScroller.isFinished()) {
//            mScroller.abortAnimation();
//        }
//    }
//
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int y = mScroller.getCurrY();
            //是新的一轮则刷新offset
            if (isNewScroll) {
                isNewScroll = false;
                mScrollOffset = y;
            }
            int offset = y - mScrollOffset;
            if (currentState == IState.ViewGroupState.PULLING) {
                frontChildView.offsetTopAndBottom(offset);
            } else if (currentState == IState.ViewGroupState.CLOSING) {
                frontChildView.offsetTopAndBottom(offset);
            }else if (currentState == IState.ViewGroupState.PULL_OUT){
                backChildView.scrollBy(0,-offset);
            }else if (currentState == IState.ViewGroupState.NORMAL){
                frontChildView.scrollBy(0,-offset);
            }
            mScrollOffset = y;
            invalidate();

            if (mScroller.isFinished()) {
                //滚动结束, 更新状态
                if (currentState == IState.ViewGroupState.PULLING) {
                    currentState = IState.ViewGroupState.PULL_OUT;
                    topChildView.offsetTopAndBottom(-topChildView.getTop());
                } else if (currentState == IState.ViewGroupState.CLOSING) {
                    currentState = IState.ViewGroupState.NORMAL;
                    topChildView.offsetTopAndBottom(-topChildView.getTop());
                }
            }
        }
    }
}