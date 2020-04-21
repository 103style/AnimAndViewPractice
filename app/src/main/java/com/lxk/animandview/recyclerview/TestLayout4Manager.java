package com.lxk.animandview.recyclerview;

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
    /**
     * 滑动过的距离
     */
    private int mScrollWidth = 0;
    /**
     * recylerView的高度 和 内容高度 之间的较大值
     */
    private int mTotalWidth = 0;
    /**
     * 相同宽高item的宽高
     */
    private int mItemWidth, mItemHeight;
    /**
     * 记录每个item的位置
     */
    private SparseArray<Rect> mItemRects = new SparseArray<>();
    /**
     * 记录每个item是否出现在屏幕上并且还没回收。 true表示出现在屏幕上，并且还没被回收
     */
    private SparseBooleanArray mHasAttachedItems = new SparseBooleanArray();
    /**
     * item之间的偏移值
     */
    private int offsetX = -1;
    /**
     * recyclerview内容的偏移值
     */
    private int startX = 0;

    /**
     * 当前高度最多可以显示多少个item
     */
    private int visibleCount;
    /**
     * item之间的角度
     */
    private float M_MAX_ROTATION_Y = 30.0f;
    private RecyclerView.Recycler mRecycler;
    private RecyclerView.State mState;

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        mRecycler = recycler;
        mState = state;
        //清空缓存数据
        mHasAttachedItems.clear();
        mItemRects.clear();
        //把屏幕内显示的item拿下来
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

        //配置item的偏移值
        offsetX = mItemWidth / 2;
        //获取第一个item显示的位置
        startX = (getWidth() - mItemWidth) / 2;


        //获取一屏可以显示的item个数
        visibleCount = getVerticalSpace() / offsetX;
        //注意内容不够 recyclerview高度时
        visibleCount = Math.min(visibleCount, getItemCount());

        //定义竖直方向的偏移量
        int offsetWidth = 0;
        //保存所有item的宽高,  这里只是针对宽高一致的item
        for (int i = 0; i < getItemCount(); i++) {
            Rect rect = new Rect(startX + offsetWidth, 0, startX + offsetWidth + mItemWidth, mItemHeight);
            mItemRects.put(i, rect);
            mHasAttachedItems.put(i, false);
            offsetWidth += offsetX;
        }

        //添加够一屏显示的item
        for (int i = 0; i < visibleCount; i++) {
            Rect rect = mItemRects.get(i);
            View view = recycler.getViewForPosition(i);
            addView(view);
            //addView后一定要先measure再layout
            measureChildWithMargins(view, 0, 0);
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom);

            //处理当前view所在位置的放缩和旋转
            handleChildView(view, rect.left - offsetX - mScrollWidth);
        }

        //计算recyclerview 的高度 和 内容高度 的较大值
        mTotalWidth = Math.max(offsetWidth, getVerticalSpace());
    }

    /**
     * 获取recyclerview用于内容显示的区域高度
     */
    private int getVerticalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    @Override
    public boolean canScrollHorizontally() {
        //设置可以水平滑动
        return true;
    }

    /**
     * 获取滑动的最大值
     */
    private int getMaxOffset() {
        return (getItemCount() - 1) * offsetX;
    }


    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mRecycler = recycler;
        mState = state;
        if (getChildCount() <= 0) {
            return dx;
        }

        int travel = dx;
        if (mScrollWidth + dx < 0) {
            //如果滑动到最顶部
            travel = -mScrollWidth;
        } else if (mScrollWidth + dx > getMaxOffset()) {
            //如果滑动到最底部
            travel = getMaxOffset() - mScrollWidth;
        }

        //计算滑动之后滑动的总距离
        mScrollWidth += travel;

        //获取滑动之后状态下屏幕可见区域对应的所有item的位置
        Rect visibleRect = getVisibleArea();

        //回收滑动之后越界的子View
        for (int i = getChildCount() - 1; i >= 0; i--) {
            //获取当前屏幕内的view
            View child = getChildAt(i);
            //获取view在recyclerview中的位置
            int position = getPosition(child);
            Rect rect = mItemRects.get(position);

            if (!Rect.intersects(rect, visibleRect)) {
                //回收不在范围内的item
                removeAndRecycleView(child, recycler);
                mHasAttachedItems.put(position, false);
            } else {
                //布局还在范围内的item
                layoutDecoratedWithMargins(child, rect.left - mScrollWidth, rect.top, rect.right - mScrollWidth, rect.bottom);
                //处理变换等操作
                handleChildView(child, rect.left - offsetX - mScrollWidth);
                mHasAttachedItems.put(position, true);
            }
        }
        //获取回收之后当前显示区域中首尾的item
        View lastView = getChildAt(getChildCount() - 1);
        View firstView = getChildAt(0);
        if (travel >= 0) {
            //向上滑动
            int minPos = getPosition(firstView);
            for (int i = minPos; i < getItemCount(); i++) {
                //依次添加item
                insertView(i, visibleRect, recycler, false);
            }
        } else {
            //向下滑动
            int maxPos = getPosition(lastView);
            for (int i = maxPos; i >= 0; i--) {
                //依次添加item
                insertView(i, visibleRect, recycler, true);
            }
        }
        return travel;
    }

    /**
     * 添加满足条件的item到屏幕内
     */
    private void insertView(int pos, Rect visibleRect, RecyclerView.Recycler recycler, boolean firstPos) {
        Rect rect = mItemRects.get(pos);
        if (Rect.intersects(visibleRect, rect) && !mHasAttachedItems.get(pos)) {
            //在显示区域 并且没有添加的item
            View child = recycler.getViewForPosition(pos);
            if (firstPos) {
                addView(child, 0);
            } else {
                addView(child);
            }
            //先测量再布局
            measureChildWithMargins(child, 0, 0);
            layoutDecoratedWithMargins(child, rect.left - mScrollWidth, rect.top, rect.right - mScrollWidth, rect.bottom);
            handleChildView(child, rect.left - mScrollWidth - startX);
            mHasAttachedItems.put(pos, true);
        }
    }

    /**
     * 获取可见的item区域Rect
     */
    private Rect getVisibleArea() {
        return new Rect(getPaddingLeft() + mScrollWidth,
                getPaddingTop(),
                getWidth() - getPaddingRight() + mScrollWidth,
                getHeight() - getPaddingBottom());
    }


    /**
     * 处理view的各种变换
     */
    private void handleChildView(View child, int moveX) {
        //设置缩放
        float radio = computeScale(moveX);
        child.setScaleX(radio);
        child.setScaleY(radio);
        //设置旋转
        float rotation = computeRotationY(moveX);
        child.setRotationY(rotation);
    }

    /**
     * 计算当前偏移距离对应放缩的倍数
     *
     * @param x 偏移值
     */
    private float computeScale(int x) {
        float scale = 1 - Math.abs(x * 1.0f / (4f * offsetX));
        if (scale < 0) {
            scale = 0;
        } else if (scale > 1) {
            scale = 1;
        }
        return scale;
    }

    /**
     * 计算当前偏移距离对应旋转的角度
     *
     * @param x 偏移值
     */
    private float computeRotationY(int x) {
        float rotationY;
        rotationY = -M_MAX_ROTATION_Y * x / offsetX;
        if (Math.abs(rotationY) > M_MAX_ROTATION_Y) {
            if (rotationY > 0) {
                rotationY = M_MAX_ROTATION_Y;
            } else {
                rotationY = -M_MAX_ROTATION_Y;
            }
        }
        return rotationY;
    }

    /**
     * 获取屏幕上中间的item的位置
     */
    int getCenterPosition() {
        int pos = mScrollWidth / offsetX;
        int more = mScrollWidth % offsetX;
        if (more > offsetX * 0.5f) {
            pos++;
        }
        return pos;
    }

    /**
     * 获取屏幕上第一个item对应的位置
     */
    int getFirstVisiblePosition() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View view = getChildAt(0);
        if (view == null) {
            return 0;
        }
        return getPosition(view);
    }

    /**
     * 计算需要滑动的距离
     */
    double calculateDistance(int velocityX, double distance) {
        int extra = mScrollWidth % offsetX;
        double realDistance;
        if (velocityX > 0) {
            if (distance < offsetX) {
                realDistance = offsetX - extra;
            } else {
                realDistance = distance - distance % offsetX - extra;
            }
        } else {
            if (distance < offsetX) {
                realDistance = extra;
            } else {
                realDistance = distance - distance % offsetX + extra;
            }
        }
        return realDistance;
    }
}
