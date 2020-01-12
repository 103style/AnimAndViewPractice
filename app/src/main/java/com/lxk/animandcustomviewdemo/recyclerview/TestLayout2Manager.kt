package com.lxk.animandcustomviewdemo.recyclerview

import android.graphics.Rect
import android.util.Log
import android.util.SparseArray
import android.util.SparseBooleanArray
import androidx.recyclerview.widget.RecyclerView

/**
 * @author https://github.com/103style
 * @date 2020/1/12 17:07
 */
class TestLayout2Manager : RecyclerView.LayoutManager() {
    private val TAG: String = TestLayout2Manager::class.java.name
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
     * 保存item是否在屏幕内显示
     */
    private val mHasAttachedItems = SparseBooleanArray()

    /**
     * 获取recyclerView可以显示内容的总高度
     */
    private val usableHeight: Int
        get() = height - paddingBottom - paddingTop


    /**
     * 滑动之后 recyclerview 内容的显示范围
     */
    private val visibleArea: Rect
        get() =
            Rect(paddingLeft, paddingTop + mScrollHeight, width + paddingRight, usableHeight - mScrollHeight)

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        Log.e(TAG, "generateDefaultLayoutParams: ")
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        Log.e(TAG, "onLayoutChildren: ")

        mHasAttachedItems.clear()
        mItemRects.clear()

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
            mHasAttachedItems.put(i, false)
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
        if (childCount <= 0) {
            return dy
        }

        var travel = dy
        if (mScrollHeight + dy < 0) {
            //如果滑动到最顶部
            travel = -mScrollHeight
        } else if (mScrollHeight + usableHeight + dy > mTotalHeight) {
            //如果滑动到最底部
            travel = mTotalHeight - usableHeight - mScrollHeight
        }

        //如果不需要滑动
        if (travel == 0) {
            return travel
        }

        //计算当前滑动的距离
        mScrollHeight += travel
        //滑动之后 recyclerview 内容的显示范围
        val visibleRect = visibleArea

        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i) ?: continue
            val position = getPosition(child)
            val rect = mItemRects.get(position)
            if (!Rect.intersects(rect, visibleRect)) {
                //移除不在屏幕内的数据
                removeAndRecycleView(child, recycler!!)
                mHasAttachedItems.put(position, false)
            } else {
                //新增数据填充空白区域
                layoutDecoratedWithMargins(child, rect.left, rect.top - mScrollHeight, rect.right, rect.bottom - mScrollHeight)
                mHasAttachedItems.put(i, true)
                child.translationX = (10 * position % child.measuredWidth).toFloat()
            }
        }

        //获取当前的屏幕可见区域的第一个和最后一个
        val firstVisibleItem = getChildAt(0)
        val lastVisibleItem = getChildAt(childCount - 1)

        if (firstVisibleItem == null || lastVisibleItem == null) {
            return travel
        }

        var offsetHeight = 0
        //布局子View阶段
        if (travel >= 0) {
            //当前可见的最后一个位置的下一个开始
            val minPos = getPosition(firstVisibleItem)
            //依次填补空白区域
            for (i in minPos until itemCount) {
                if (offsetHeight > usableHeight) {
                    mHasAttachedItems.put(i, false)
                } else {
                    insertView(recycler, visibleRect, false, i)
                    offsetHeight += mItemRects.get(i).height()
                }
            }
        } else {
            //当前可见的开头位置的上一个开始
            val maxPos = getPosition(lastVisibleItem)
            //依次填补空白区域
            for (i in maxPos downTo 0) {
                if (offsetHeight < usableHeight) {
                    insertView(recycler, visibleRect, true, i)
                    offsetHeight += mItemRects.get(i).height()
                } else {
                    mHasAttachedItems.put(i, false)
                }
            }
        }
        return travel
    }


    /**
     * 添加view
     *
     * @param recycler 回收管理器
     * @param top      是否是顶部
     * @param pos      当前的位置
     */
    private fun insertView(recycler: RecyclerView.Recycler?, visibleRect: Rect, top: Boolean, pos: Int) {
        val rect = mItemRects.get(pos)
        if (Rect.intersects(visibleRect, rect) && !mHasAttachedItems.get(pos)) {
            val child = recycler!!.getViewForPosition(pos)
            if (top) {
                addView(child, 0)
            } else {
                addView(child)
            }
            measureChildWithMargins(child, 0, 0)
            layoutDecorated(child, rect.left, rect.top - mScrollHeight, rect.right, rect.bottom - mScrollHeight)
            child.translationX = (10 * getPosition(child) % child.measuredWidth).toFloat()
            mHasAttachedItems.put(pos, true)
        }
    }
}
