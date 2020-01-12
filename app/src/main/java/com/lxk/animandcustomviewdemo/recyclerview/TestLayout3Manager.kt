package com.lxk.animandcustomviewdemo.recyclerview

import android.graphics.Rect
import android.util.SparseArray
import android.util.SparseBooleanArray

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams

/**
 * @author https://github.com/103style
 * @date 2020/1/12 17:21
 */
class TestLayout3Manager : RecyclerView.LayoutManager() {
    private val TAG = TestLayout3Manager::class.java.name
    private var mSumDy = 0
    private var mTotalHeight = 0
    private var mItemWidth: Int = 0
    private var mItemHeight: Int = 0
    private val mItemRects = SparseArray<Rect>()

    /**
     * 记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收
     */
    private val mHasAttachedItems = SparseBooleanArray()

    private val verticalSpace: Int
        get() = height - paddingBottom - paddingTop

    /**
     * 获取可见的区域Rect
     *
     * @return
     */
    private val visibleArea: Rect
        get() = Rect(paddingLeft, paddingTop + mSumDy, width + paddingRight, verticalSpace + mSumDy)

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount == 0) {//没有Item，界面空着吧
            detachAndScrapAttachedViews(recycler!!)
            return
        }
        mHasAttachedItems.clear()
        mItemRects.clear()

        detachAndScrapAttachedViews(recycler!!)

        //将item的位置存储起来
        val childView = recycler.getViewForPosition(0)
        measureChildWithMargins(childView, 0, 0)
        mItemWidth = getDecoratedMeasuredWidth(childView)
        mItemHeight = getDecoratedMeasuredHeight(childView)

        val visibleCount = verticalSpace / mItemHeight


        //定义竖直方向的偏移量
        var offsetY = 0

        for (i in 0 until itemCount) {
            val rect = Rect(0, offsetY, mItemWidth, offsetY + mItemHeight)
            mItemRects.put(i, rect)
            mHasAttachedItems.put(i, false)
            offsetY += mItemHeight
        }


        for (i in 0 until visibleCount) {
            val rect = mItemRects.get(i)
            val view = recycler.getViewForPosition(i)
            addView(view)
            //addView后一定要measure，先measure再layout
            measureChildWithMargins(view, 0, 0)
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom)
        }

        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        mTotalHeight = Math.max(offsetY, verticalSpace)
    }


    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        if (childCount <= 0) {
            return dy
        }

        var travel = dy
        //如果滑动到最顶部
        if (mSumDy + dy < 0) {
            travel = -mSumDy
        } else if (mSumDy + dy > mTotalHeight - verticalSpace) {
            //如果滑动到最底部
            travel = mTotalHeight - verticalSpace - mSumDy
        }

        mSumDy += travel

        val visibleRect = visibleArea

        //回收越界子View
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            val position = getPosition(child!!)
            val rect = mItemRects.get(position)

            if (!Rect.intersects(rect, visibleRect)) {
                removeAndRecycleView(child, recycler!!)
                mHasAttachedItems.put(position, false)
            } else {
                layoutDecoratedWithMargins(child, rect.left, rect.top - mSumDy, rect.right, rect.bottom - mSumDy)
                child.rotationY = child.rotationY + 1
                mHasAttachedItems.put(position, true)
            }
        }

        val lastView = getChildAt(childCount - 1)
        val firstView = getChildAt(0)
        if (travel >= 0) {
            val minPos = getPosition(firstView!!)
            for (i in minPos until itemCount) {
                insertView(i, visibleRect, recycler, false)
            }
        } else {
            val maxPos = getPosition(lastView!!)
            for (i in maxPos downTo 0) {
                insertView(i, visibleRect, recycler, true)
            }
        }
        return travel
    }

    private fun insertView(pos: Int, visibleRect: Rect, recycler: RecyclerView.Recycler?, firstPos: Boolean) {
        val rect = mItemRects.get(pos)
        if (Rect.intersects(visibleRect, rect) && !mHasAttachedItems.get(pos)) {
            val child = recycler!!.getViewForPosition(pos)
            if (firstPos) {
                addView(child, 0)
            } else {
                addView(child)
            }
            measureChildWithMargins(child, 0, 0)
            layoutDecoratedWithMargins(child, rect.left, rect.top - mSumDy, rect.right, rect.bottom - mSumDy)

            //在布局item后，修改每个item的旋转度数
            child.rotationY = child.rotationY + 1
            mHasAttachedItems.put(pos, true)
        }
    }

}
