package com.lxk.animandcustomviewdemo.recyclerview

import android.graphics.Rect
import android.util.Log
import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView

/**
 * @author https://github.com/103style
 * @date  2020/1/12 17:27
 */
class TestLayoutManager : RecyclerView.LayoutManager() {
    private val TAG = TestLayoutManager::class.java.name

    /**
     * 当前recyclerview滑动的总距离
     * 即 第一个item的顶部到 recyclerview 顶部的距离
     */
    private var mScrollHeight = 0
    /**
     * 所有item内容的高度 和  rrecyclerview的高度  两者之间较大的值
     */
    private var mTotalHeight = 0
    /**
     * item的固定宽高
     */
    private var mItemWidth: Int = 0
    private var mItemHeight: Int = 0
    /**
     * 缓存每个item的位置
     */
    private val mItemRects = SparseArray<Rect>()

    /**
     * 获取recyclerView可以显示内容的总高度
     */
    private val usableHeight: Int
        get() = height - paddingBottom - paddingTop

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        Log.e(TAG, "generateDefaultLayoutParams: ")
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        Log.e(TAG, "onLayoutChildren: ")

        detachAndScrapAttachedViews(recycler!!)
        if (itemCount == 0) {
            //没有Item，界面空着吧
            return
        }

        //计算第一个item的宽高
        val childView = recycler.getViewForPosition(0)
        measureChildWithMargins(childView, 0, 0)
        mItemWidth = getDecoratedMeasuredWidth(childView)
        mItemHeight = getDecoratedMeasuredHeight(childView)

        //计算recyclerview的高度最多能显示多少个item
        var visibleCount = usableHeight / mItemHeight
        if (usableHeight % mItemHeight != 0) {
            visibleCount++
        }
        //确认可以显示多少个item
        visibleCount = Math.min(visibleCount, itemCount)

        //所有item的总高度
        var offsetHeight = 0

        //缓存每个item的宽高
        for (i in 0 until itemCount) {
            val rect = Rect(0, offsetHeight, mItemWidth, offsetHeight + mItemHeight)
            mItemRects.put(i, rect)
            offsetHeight += mItemHeight
        }

        //添加item到recyclerview的可见区域
        for (i in 0 until visibleCount) {
            val rect = mItemRects.get(i)
            val view = recycler.getViewForPosition(i)
            addView(view)
            //addView后一定要先measure再layout
            measureChildWithMargins(view, 0, 0)
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom)
        }
        //如果所有子View的高度和没有填满RecyclerView的高度，则将高度设置为RecyclerView的高度
        mTotalHeight = Math.max(offsetHeight, usableHeight)

    }

    override fun canScrollVertically(): Boolean {
        //设置能够竖直滑动
        return true
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        var travel = dy
        if (mScrollHeight + dy < 0) {
            //如果滑动到最顶部
            travel = -mScrollHeight
        } else if (mScrollHeight + usableHeight + dy > mTotalHeight) {
            //如果滑动到最底部
            travel = mTotalHeight - usableHeight - mScrollHeight
        }

        //移除离开屏幕的view  在空白处填充view
        removeAndInsertView(recycler, travel)

        //计算当前滑动的距离
        mScrollHeight += travel
        // 平移容器内的item
        offsetChildrenVertical(-travel)
        return dy
    }


    /**
     * 移除离开屏幕的view  在空白处填充view
     *
     * @param travel >0 向上滑  <0 向下滑动
     */
    private fun removeAndInsertView(recycler: RecyclerView.Recycler?, travel: Int) {
        if (travel == 0) {
            return
        }
        //回收离开屏幕上下方的item
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i) ?: continue
            val needRemoveAndRecycle: Boolean
            if (travel > 0) {
                needRemoveAndRecycle = getDecoratedBottom(child) - travel < 0
            } else {
                needRemoveAndRecycle = getDecoratedTop(child) + paddingBottom - travel > height
            }
            if (needRemoveAndRecycle) {
                removeAndRecycleView(child, recycler!!)
            }
        }

        val visibleRect = getVisibleArea(travel)
        //布局子View阶段
        if (travel > 0) {
            val lastView = getChildAt(childCount - 1) ?: return
//当前可见的最后一个位置的下一个开始
            val minPos = getPosition(lastView) + 1
            //依次填补空白区域
            for (i in minPos until itemCount) {
                val rect = mItemRects.get(i)
                if (Rect.intersects(visibleRect, rect)) {
                    insertView(recycler!!, rect, false, i)
                } else {
                    break
                }
            }
        } else {
            val firstView = getChildAt(0) ?: return
//当前可见的开头位置的上一个开始
            val maxPos = getPosition(firstView) - 1
            //依次填补空白区域
            for (i in maxPos downTo 0) {
                val rect = mItemRects.get(i)
                if (Rect.intersects(visibleRect, rect)) {
                    insertView(recycler!!, rect, true, i)
                } else {
                    break
                }
            }
        }
    }

    /**
     * 添加view
     *
     * @param recycler 回收管理器
     * @param rect     item的位置
     * @param top      是否是顶部
     * @param i        当前的位置
     */
    private fun insertView(recycler: RecyclerView.Recycler, rect: Rect, top: Boolean, i: Int) {
        val child = recycler.getViewForPosition(i)
        if (top) {
            addView(child, 0)
        } else {
            addView(child)
        }
        measureChildWithMargins(child, 0, 0)
        layoutDecorated(child, rect.left, rect.top - mScrollHeight, rect.right, rect.bottom - mScrollHeight)
    }


    /**
     * 滑动之后 recyclerview 内容的显示范围
     *
     * @param travel 滑动距离
     */
    private fun getVisibleArea(travel: Int): Rect {
        return Rect(paddingLeft, paddingTop + mScrollHeight + travel, width + paddingRight, usableHeight + mScrollHeight + travel)
    }


}
