package com.lxk.animandview.recyclerview;

import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 17:00
 */
public class TestLayout2Manager extends RecyclerView.LayoutManager {
    private static final String TAG = "TestLayout2Manager";

    /**
     * 当前recyclerview滑动的总距离
     * 即 第一个item的顶部到 recyclerview 顶部的距离
     */
    private int mScrollHeight = 0;
    /**
     * 所有item内容的高度 和  rrecyclerview的高度  两者之间较大的值
     */
    private int mTotalHeight = 0;
    /**
     * item的固定宽高
     */
    private int mItemWidth, mItemHeight;
    /**
     * 缓存每个item的位置
     */
    private SparseArray<Rect> mItemRects = new SparseArray<>();
    /**
     * 保存item是否在屏幕内显示
     */
    private SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        Log.e(TAG, "generateDefaultLayoutParams: ");
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.e(TAG, "onLayoutChildren: ");

        mHasAttachedItems.clear();
        mItemRects.clear();

        detachAndScrapAttachedViews(recycler);
        if (getItemCount() == 0) {
            //没有Item，界面空着吧
            return;
        }

        //计算第一个item的宽高
        View childView = recycler.getViewForPosition(0);
        measureChildWithMargins(childView, 0, 0);
        mItemWidth = getDecoratedMeasuredWidth(childView);
        mItemHeight = getDecoratedMeasuredHeight(childView);

        //计算recyclerview的高度最多能显示多少个item
        int visibleCount = getUsableHeight() / mItemHeight;
        if (getUsableHeight() % mItemHeight != 0) {
            visibleCount++;
        }
        //确认可以显示多少个item
        visibleCount = Math.min(visibleCount, getItemCount());

        //所有item的总高度
        int offsetHeight = 0;
        //缓存每个item的宽高
        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(0, offsetHeight, mItemWidth, offsetHeight + mItemHeight);
            mItemRects.put(i, rect);
            mHasAttachedItems.put(i, false);
            offsetHeight += mItemHeight;
        }

        //添加item到recyclerview的可见区域
        for (int i = 0; i < visibleCount; i++) {
            Rect rect = mItemRects.get(i);
            View view = recycler.getViewForPosition(i);
            addView(view);
            //addView后一定要先measure再layout
            measureChildWithMargins(view, 0, 0);
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);
        }
        //如果所有子View的高度和没有填满RecyclerView的高度，则将高度设置为RecyclerView的高度
        mTotalHeight = Math.max(offsetHeight, getUsableHeight());

    }

    /**
     * 获取recyclerView可以显示内容的总高度
     */
    private int getUsableHeight() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    @Override
    public boolean canScrollVertically() {
        //设置能够竖直滑动
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() <= 0) {
            return dy;
        }

        int travel = dy;
        if (mScrollHeight + dy < 0) {
            //如果滑动到最顶部
            travel = -mScrollHeight;
        } else if (mScrollHeight + getUsableHeight() + dy > mTotalHeight) {
            //如果滑动到最底部
            travel = mTotalHeight - getUsableHeight() - mScrollHeight;
        }

        //如果不需要滑动
        if (travel == 0) {
            return travel;
        }

        //计算当前滑动的距离
        mScrollHeight += travel;
        //滑动之后 recyclerview 内容的显示范围
        Rect visibleRect = getVisibleArea();

        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            int position = getPosition(child);
            Rect rect = mItemRects.get(position);
            if (!Rect.intersects(rect, visibleRect)) {
                //移除不在屏幕内的数据
                removeAndRecycleView(child, recycler);
                mHasAttachedItems.put(position, false);
            } else {
                //新增数据填充空白区域
                layoutDecoratedWithMargins(child, rect.left, rect.top - mScrollHeight, rect.right, rect.bottom - mScrollHeight);
                mHasAttachedItems.put(i, true);
                child.setTranslationX((10 * position) % child.getMeasuredWidth());
            }
        }

        //获取当前的屏幕可见区域的第一个和最后一个
        View firstVisibleItem = getChildAt(0);
        View lastVisibleItem = getChildAt(getChildCount() - 1);

        if (firstVisibleItem == null || lastVisibleItem == null) {
            return travel;
        }

        int offsetHeight = 0;
        //布局子View阶段
        if (travel >= 0) {
            //当前可见的最后一个位置的下一个开始
            int minPos = getPosition(firstVisibleItem);
            //依次填补空白区域
            for (int i = minPos; i < getItemCount(); i++) {
                if (offsetHeight > getUsableHeight()) {
                    mHasAttachedItems.put(i, false);
                } else {
                    insertView(recycler, visibleRect, false, i);
                    offsetHeight += mItemRects.get(i).height();
                }
            }
        } else {
            //当前可见的开头位置的上一个开始
            int maxPos = getPosition(lastVisibleItem);
            //依次填补空白区域
            for (int i = maxPos; i >= 0; i--) {
                if (offsetHeight < getUsableHeight()) {
                    insertView(recycler, visibleRect, true, i);
                    offsetHeight += mItemRects.get(i).height();
                } else {
                    mHasAttachedItems.put(i, false);
                }
            }
        }
        return travel;
    }


    /**
     * 添加view
     *
     * @param recycler 回收管理器
     * @param top      是否是顶部
     * @param pos      当前的位置
     */
    private void insertView(RecyclerView.Recycler recycler, Rect visibleRect, boolean top, int pos) {
        Rect rect = mItemRects.get(pos);
        if (Rect.intersects(visibleRect, rect) && !mHasAttachedItems.get(pos)) {
            View child = recycler.getViewForPosition(pos);
            if (top) {
                addView(child, 0);
            } else {
                addView(child);
            }
            measureChildWithMargins(child, 0, 0);
            layoutDecorated(child, rect.left, rect.top - mScrollHeight, rect.right, rect.bottom - mScrollHeight);
            child.setTranslationX((10 * getPosition(child)) % child.getMeasuredWidth());
            mHasAttachedItems.put(pos, true);
        }
    }


    /**
     * 滑动之后 recyclerview 内容的显示范围
     */
    private Rect getVisibleArea() {
        return new Rect(getPaddingLeft(), getPaddingTop() + mScrollHeight, getWidth() + getPaddingRight(), getUsableHeight() - mScrollHeight);
    }
}
