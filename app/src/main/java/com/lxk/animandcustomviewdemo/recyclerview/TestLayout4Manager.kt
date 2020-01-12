package com.lxk.animandcustomviewdemo.recyclerview

import android.graphics.Rect
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams

/**
 * @author https://github.com/103style
 * @date 2019/11/14 17:00
 */
class TestLayout4Manager : RecyclerView.LayoutManager() {
    private val TAG = TestLayout4Manager::class.java.name
    /**
     * 滑动过的距离
     */
    private var mScrollWidth = 0
    /**
     * recylerView的高度 和 内容高度 之间的较大值
     */
    private var mTotalWidth = 0
    /**
     * 相同宽高item的宽高
     */
    private var mItemWidth: Int = 0
    private var mItemHeight: Int = 0
    /**
     * 记录每个item的位置
     */
    private val mItemRects = SparseArray<Rect>()
    /**
     * 记录每个item是否出现在屏幕上并且还没回收。 true表示出现在屏幕上，并且还没被回收
     */
    private val mHasAttachedItems = SparseBooleanArray()
    /**
     * item之间的偏移值
     */
    private var offsetX = -1
    /**
     * recyclerview内容的偏移值
     */
    private var startX = 0

    /**
     * 当前高度最多可以显示多少个item
     */
    private var visibleCount: Int = 0
    /**
     * item之间的角度
     */
    private val M_MAX_ROTATION_Y = 30.0f
    private var mRecycler: RecyclerView.Recycler? = null
    private var mState: RecyclerView.State? = null

    /**
     * 获取recyclerview用于内容显示的区域高度
     */
    private val verticalSpace: Int
        get() = width - paddingRight - paddingLeft

    /**
     * 获取滑动的最大值
     */
    private val maxOffset: Int
        get() = (itemCount - 1) * offsetX

    /**
     * 获取可见的item区域Rect
     */
    private val visibleArea: Rect
        get() = Rect(paddingLeft + mScrollWidth,
                paddingTop,
                width - paddingRight + mScrollWidth,
                height - paddingBottom)

    /**
     * 获取屏幕上中间的item的位置
     */
    internal val centerPosition: Int
        get() {
            var pos = mScrollWidth / offsetX
            val more = mScrollWidth % offsetX
            if (more > offsetX * 0.5f) {
                pos++
            }
            return pos
        }

    /**
     * 获取屏幕上第一个item对应的位置
     */
    internal val firstVisiblePosition: Int
        get() {
            if (childCount <= 0) {
                return 0
            }
            val view = getChildAt(0) ?: return 0
            return getPosition(view)
        }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        mRecycler = recycler
        mState = state
        //清空缓存数据
        mHasAttachedItems.clear()
        mItemRects.clear()
        //把屏幕内显示的item拿下来
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

        //配置item的偏移值
        offsetX = mItemWidth / 2
        //获取第一个item显示的位置
        startX = (width - mItemWidth) / 2


        //获取一屏可以显示的item个数
        visibleCount = verticalSpace / offsetX
        //注意内容不够 recyclerview高度时
        visibleCount = Math.min(visibleCount, itemCount)

        //定义竖直方向的偏移量
        var offsetWidth = 0
        //保存所有item的宽高,  这里只是针对宽高一致的item
        for (i in 0 until itemCount) {
            val rect = Rect(startX + offsetWidth, 0, startX + offsetWidth + mItemWidth, mItemHeight)
            mItemRects.put(i, rect)
            mHasAttachedItems.put(i, false)
            offsetWidth += offsetX
        }

        //添加够一屏显示的item
        for (i in 0 until visibleCount) {
            val rect = mItemRects.get(i)
            val view = recycler.getViewForPosition(i)
            addView(view)
            //addView后一定要先measure再layout
            measureChildWithMargins(view, 0, 0)
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom)

            //处理当前view所在位置的放缩和旋转
            handleChildView(view, rect.left - offsetX - mScrollWidth)
        }

        //计算recyclerview 的高度 和 内容高度 的较大值
        mTotalWidth = Math.max(offsetWidth, verticalSpace)
    }

    override fun canScrollHorizontally(): Boolean {
        //设置可以水平滑动
        return true
    }


    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        mRecycler = recycler
        mState = state
        if (childCount <= 0) {
            return dx
        }

        var travel = dx
        if (mScrollWidth + dx < 0) {
            //如果滑动到最顶部
            travel = -mScrollWidth
        } else if (mScrollWidth + dx > maxOffset) {
            //如果滑动到最底部
            travel = maxOffset - mScrollWidth
        }

        //计算滑动之后滑动的总距离
        mScrollWidth += travel

        //获取滑动之后状态下屏幕可见区域对应的所有item的位置
        val visibleRect = visibleArea

        //回收滑动之后越界的子View
        for (i in childCount - 1 downTo 0) {
            //获取当前屏幕内的view
            val child = getChildAt(i)
            //获取view在recyclerview中的位置
            val position = getPosition(child!!)
            val rect = mItemRects.get(position)

            if (!Rect.intersects(rect, visibleRect)) {
                //回收不在范围内的item
                removeAndRecycleView(child, recycler!!)
                mHasAttachedItems.put(position, false)
            } else {
                //布局还在范围内的item
                layoutDecoratedWithMargins(child, rect.left - mScrollWidth, rect.top, rect.right - mScrollWidth, rect.bottom)
                //处理变换等操作
                handleChildView(child, rect.left - offsetX - mScrollWidth)
                mHasAttachedItems.put(position, true)
            }
        }
        //获取回收之后当前显示区域中首尾的item
        val lastView = getChildAt(childCount - 1)
        val firstView = getChildAt(0)
        if (travel >= 0) {
            //向上滑动
            val minPos = getPosition(firstView!!)
            for (i in minPos until itemCount) {
                //依次添加item
                insertView(i, visibleRect, recycler, false)
            }
        } else {
            //向下滑动
            val maxPos = getPosition(lastView!!)
            for (i in maxPos downTo 0) {
                //依次添加item
                insertView(i, visibleRect, recycler, true)
            }
        }
        return travel
    }

    /**
     * 添加满足条件的item到屏幕内
     */
    private fun insertView(pos: Int, visibleRect: Rect, recycler: RecyclerView.Recycler?, firstPos: Boolean) {
        val rect = mItemRects.get(pos)
        if (Rect.intersects(visibleRect, rect) && !mHasAttachedItems.get(pos)) {
            //在显示区域 并且没有添加的item
            val child = recycler!!.getViewForPosition(pos)
            if (firstPos) {
                addView(child, 0)
            } else {
                addView(child)
            }
            //先测量再布局
            measureChildWithMargins(child, 0, 0)
            layoutDecoratedWithMargins(child, rect.left - mScrollWidth, rect.top, rect.right - mScrollWidth, rect.bottom)
            handleChildView(child, rect.left - mScrollWidth - startX)
            mHasAttachedItems.put(pos, true)
        }
    }


    /**
     * 处理view的各种变换
     */
    private fun handleChildView(child: View, moveX: Int) {
        //设置缩放
        val radio = computeScale(moveX)
        child.scaleX = radio
        child.scaleY = radio
        //设置旋转
        val rotation = computeRotationY(moveX)
        child.rotationY = rotation
    }

    /**
     * 计算当前偏移距离对应放缩的倍数
     *
     * @param x 偏移值
     */
    private fun computeScale(x: Int): Float {
        var scale = 1 - Math.abs(x * 1.0f / (4f * offsetX))
        if (scale < 0) {
            scale = 0f
        } else if (scale > 1) {
            scale = 1f
        }
        return scale
    }

    /**
     * 计算当前偏移距离对应旋转的角度
     *
     * @param x 偏移值
     */
    private fun computeRotationY(x: Int): Float {
        var rotationY: Float
        rotationY = -M_MAX_ROTATION_Y * x / offsetX
        if (Math.abs(rotationY) > M_MAX_ROTATION_Y) {
            if (rotationY > 0) {
                rotationY = M_MAX_ROTATION_Y
            } else {
                rotationY = -M_MAX_ROTATION_Y
            }
        }
        return rotationY
    }

    /**
     * 计算需要滑动的距离
     */
    internal fun calculateDistance(velocityX: Int, distance: Double): Double {
        val extra = mScrollWidth % offsetX
        val realDistance: Double
        if (velocityX > 0) {
            if (distance < offsetX) {
                realDistance = (offsetX - extra).toDouble()
            } else {
                realDistance = distance - distance % offsetX - extra.toDouble()
            }
        } else {
            if (distance < offsetX) {
                realDistance = extra.toDouble()
            } else {
                realDistance = distance - distance % offsetX + extra
            }
        }
        return realDistance
    }

}
