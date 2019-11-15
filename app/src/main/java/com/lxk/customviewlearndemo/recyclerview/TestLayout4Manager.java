package com.lxk.customviewlearndemo.recyclerview;

import android.graphics.Rect;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 17:00
 */
public class TestLayout4Manager extends RecyclerView.LayoutManager {
    private static final String TAG = "TestLayout4Manager";
    private int mScrollWidth = 0;
    private int mTotalWidth = 0;
    private int mItemWidth, mItemHeight;
    private SparseArray<Rect> mItemRects = new SparseArray<>();
    /**
     * 记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
     */
    private SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();

    private int offsetX, startX;

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            //没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        mHasAttachedItems.clear();
        mItemRects.clear();

        detachAndScrapAttachedViews(recycler);

        //将item的位置存储起来
        View childView = recycler.getViewForPosition(0);
        measureChildWithMargins(childView, 0, 0);
        mItemWidth = getDecoratedMeasuredWidth(childView);
        mItemHeight = getDecoratedMeasuredHeight(childView);
        offsetX = mItemWidth / 2;
        int visibleCount = getVerticalSpace() / offsetX;

        startX = (getWidth() - mItemWidth) / 2;

        //定义竖直方向的偏移量
        int offsetWidth = 0;
        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(startX + offsetWidth, 0, startX + offsetWidth + mItemWidth, mItemHeight);
            mItemRects.put(i, rect);
            mHasAttachedItems.put(i, false);
            offsetWidth += offsetX;
        }


        for (int i = 0; i < visibleCount; i++) {
            Rect rect = mItemRects.get(i);
            View view = recycler.getViewForPosition(i);
            addView(view);
            //addView后一定要measure，先measure再layout
            measureChildWithMargins(view, 0, 0);
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);
        }

        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        mTotalWidth = Math.max(offsetWidth, getVerticalSpace());
    }

    private int getVerticalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    private int getMaxOffset() {
        return (getItemCount() - 1) * offsetX;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() <= 0) {
            return dx;
        }

        int travel = dx;
        //如果滑动到最顶部
        if (mScrollWidth + dx < 0) {
            travel = -mScrollWidth;
        } else if (mScrollWidth + dx > getMaxOffset()) {
            //如果滑动到最底部
            travel = getMaxOffset() - mScrollWidth;
        }

        mScrollWidth += travel;

        Rect visibleRect = getVisibleArea();

        //回收越界子View
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            int position = getPosition(child);
            Rect rect = mItemRects.get(position);

            if (!Rect.intersects(rect, visibleRect)) {
                removeAndRecycleView(child, recycler);
                mHasAttachedItems.put(position, false);
            } else {
                layoutDecoratedWithMargins(child, rect.left - mScrollWidth, rect.top, rect.right - mScrollWidth, rect.bottom);
                handleChildView(child, rect.left - offsetX - mScrollWidth);
                mHasAttachedItems.put(position, true);
            }
        }

        View lastView = getChildAt(getChildCount() - 1);
        View firstView = getChildAt(0);
        if (travel >= 0) {
            int minPos = getPosition(firstView);
            for (int i = minPos; i < getItemCount(); i++) {
                insertView(i, visibleRect, recycler, false);
            }
        } else {
            int maxPos = getPosition(lastView);
            for (int i = maxPos; i >= 0; i--) {
                insertView(i, visibleRect, recycler, true);
            }
        }
        return travel;
    }

    private void insertView(int pos, Rect visibleRect, RecyclerView.Recycler recycler, boolean firstPos) {
        Rect rect = mItemRects.get(pos);
        if (Rect.intersects(visibleRect, rect) && !mHasAttachedItems.get(pos)) {
            View child = recycler.getViewForPosition(pos);
            if (firstPos) {
                addView(child, 0);
            } else {
                addView(child);
            }
            measureChildWithMargins(child, 0, 0);
            layoutDecoratedWithMargins(child, rect.left - mScrollWidth, rect.top, rect.right - mScrollWidth, rect.bottom);
            handleChildView(child, rect.left - mScrollWidth - startX);
            mHasAttachedItems.put(pos, true);
        }
    }

    /**
     * 获取可见的区域Rect
     */
    private Rect getVisibleArea() {
        return new Rect(getPaddingLeft() + mScrollWidth, getPaddingTop(), getWidth() - getPaddingRight() + mScrollWidth, getHeight() - getPaddingBottom());
    }

    public int getCenterPosition() {
        int pos = mScrollWidth / offsetX;
        int more = mScrollWidth % offsetX;
        if (more > offsetX * 0.5f) pos++;
        return pos;
    }

    public int getFirstVisiblePosition() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View view = getChildAt(0);
        int pos = getPosition(view);
        return pos;
    }


    private void handleChildView(View child, int moveX) {
        float radio = computeScale(moveX);

        child.setScaleX(radio);
        child.setScaleY(radio);

        int pos = getPosition(child);
        int centerPos = getCenterPosition();
        if (pos + 1 == centerPos) {
            child.setRotationY(30);
        } else if (pos == centerPos) {
            child.setRotationY(0);
        } else if (pos - 2 == centerPos) {
            child.setRotationY(-60);
        } else if (pos + 2 == centerPos) {
            child.setRotationY(60);
        } else if (pos - 1 == centerPos) {
            child.setRotationY(-30);
        }


    }

    private float computeScale(int x) {
        float scale = 1 - Math.abs(x * 1.0f / (4f * offsetX));
        if (scale < 0) scale = 0;
        if (scale > 1) scale = 1;
        return scale;
    }
}
