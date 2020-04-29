package com.lxk.animandview.practice.path;

import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2020/4/27 20:19
 * 参考doc: https://blog.csdn.net/u011387817/article/details/81875021
 */
public class PathLayoutManager extends RecyclerView.LayoutManager {

    /**
     * 路径数据
     */
    private PathKeyFrame pathKeyFrame;
    /**
     * item的间距
     */
    private int itemOffset;
    /**
     * 是否是竖直滑动
     */
    private boolean vertical;

    /**
     * 最多可见的item总数
     */
    private int visibleItemCount;
    /**
     * 目前滑动的距离
     */
    private int dxCount, dyCount;
    /**
     * 记录当前第一个和最后一个视图的宽高
     */
    private int firstW, firstH, lastW, lastH;

    private int mFirstVisibleItem;
    /**
     * 允许滑动溢出
     */
    private boolean overflowMode;
    /**
     * 循环滑动
     */
    private boolean circulateMode;

    public PathLayoutManager(@NonNull Path mPath, int itemOffset) {
        this(mPath, itemOffset, RecyclerView.VERTICAL);
    }

    public PathLayoutManager(@NonNull Path mPath, int itemOffset,
                             @RecyclerView.Orientation int orientation) {
        if (itemOffset < 0) {
            throw new IllegalArgumentException("itemOffset must be > 0 !");
        }
        this.itemOffset = itemOffset;
        vertical = orientation == RecyclerView.VERTICAL;
        updatePath(mPath);
    }

    /**
     * 更新路径
     */
    public void updatePath(@NonNull Path mPath) {
        pathKeyFrame = new PathKeyFrame(mPath);
        visibleItemCount = (int) (pathKeyFrame.getPathLength() / itemOffset) + 1;
        requestLayout();
    }

    /**
     * 更新item间距
     */
    public void setItemOffset(int itemOffset) {
        if (this.itemOffset != itemOffset && itemOffset > 0) {
            this.itemOffset = itemOffset;
            if (pathKeyFrame != null) {
                visibleItemCount = (int) (pathKeyFrame.getPathLength() / itemOffset) + 1;
                requestLayout();
            }
        }
    }

    /**
     * 是否开启循环滑动
     */
    public boolean isCirculateMode() {
        return circulateMode;
    }

    /**
     * 设置循环滑动
     */
    public void setCirculateMode(boolean circulateMode) {
        this.circulateMode = circulateMode;
    }

    /**
     * 是否允许item被完全划出
     */
    public boolean isOverflowMode() {
        return overflowMode;
    }

    /**
     * 设置是否允许item被完全划出
     */
    public void setOverflowMode(boolean overflowMode) {
        this.overflowMode = overflowMode;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            /**
             * 没有Item可布局，就回收全部临时缓存
             * 参考 {@link androidx.recyclerview.widget.LinearLayoutManager#onLayoutChildren(RecyclerView.Recycler, RecyclerView.State)}
             * 这里的没有Item，是指Adapter里面的数据集，
             * 可能临时被清空了，但不确定何时还会继续添加回来
             */
            removeAndRecycleAllViews(recycler);
            return;
        }
        //取下当前在屏幕上显示的item
        detachAndScrapAttachedViews(recycler);
        //获取接下来要显示的item
        List<PathPoint> needItems = isCirculateScroll() ? getNeedLayoutCirculateItems() : getNeedLayoutItems(state.getItemCount());
        if (needItems.isEmpty()) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        onLayout(recycler, needItems);
    }

    /**
     * 获取循环滑动状态下当前需要显示的item
     */
    private List<PathPoint> getNeedLayoutCirculateItems() {
        List<PathPoint> result = new ArrayList<>();

        int overflowCount = getOverflowCount();

        //得出第一个可见的item索引
        mFirstVisibleItem = overflowCount - 1 - visibleItemCount;

        float curDis, fraction;
        PathPoint pathPoint;
        int pos;
        for (int i = mFirstVisibleItem; i < overflowCount; i++) {
            pos = i % getItemCount();
            if (pos < 0) {
                pos += getItemCount();
            }
            curDis = (i + getItemCount()) * itemOffset - getScrollOffset();
            fraction = curDis / pathKeyFrame.getPathLength();
            pathPoint = pathKeyFrame.getValue(fraction);
            if (pathPoint == null) {
                continue;
            }
            result.add(new PathPoint(pathPoint, pos, fraction));
        }
        return result;
    }

    /**
     * 获取溢出的个数
     */
    private int getOverflowCount() {
        // 屏幕上面 / 屏幕 / 屏幕下面
        //                 0 _ 1 _ 2 _/ 3 _ 4 _ 5 _ 0 _ 1 /_ 2 _ 3 _ 4
        // 0 _ 1 _ 2 _ 3 _ 4 _ 5 _ 0 _/ 1 _ 2 _ 3 _ 4 _ 5 /_ 0 _ 1

        //获取内容的长度 6 个item的长度
        int contentLength = getItemContentLength();

        //获取路径的长度 即屏幕内可见个数的长度  5 个item的长度
        int pathLength = (int) pathKeyFrame.getPathLength();

        //第一个item 到 path 终点的偏移量  即（0 - 第二个 0   = 3 + 5 = 8 or  0- 第二个5  = 7 + 5 = 12）个item的长度
        int firstItemOffset = getScrollOffset() + pathLength;

        //firstItemOffset 对应的最后一个item 相对于path终点的偏移量 (8 - 5 = 3   12 - 5 = 7)
        int lastItemOffset = firstItemOffset - contentLength;

        //总长度 6 + 5 = 11
        int lengthOffset = contentLength + pathLength;

        /**
         * 根据 {@link #updateOffsetInCirculateMode(int, int)} 中更新滑动距离的逻辑
         * scroll 的范围为  -pathLength 到 contentLength
         *
         * 当前屏幕下方外的第一个对应的数 8 < 11  =  0    12 - 11 = 1
         */
        int lastItemOverflowOffset = firstItemOffset > lengthOffset ? firstItemOffset - lengthOffset : 0;

        //空缺的距离 3 + 0 = 3    7 % 6 + 1 = 2
        int vacantDistance = lastItemOffset % contentLength + lastItemOverflowOffset;

        //空缺的距离 / item之间的距离 = 需补上的item个数
        return vacantDistance / itemOffset;
    }


    /**
     * 获取当前要显示的item集合
     */
    private List<PathPoint> getNeedLayoutItems(int itemCount) {
        List<PathPoint> result = new ArrayList<>();
        float curDis;
        //找到第一个显示的位置
        for (int i = 0; i < itemCount; i++) {
            curDis = i * itemOffset - getScrollOffset();
            if (curDis >= 0) {
                mFirstVisibleItem = i;
                break;
            }
        }
        //获取最后一个显示的下标
        int endIndex = mFirstVisibleItem + itemCount;
        if (endIndex + 1 > getItemCount()) {
            endIndex = getItemCount() - 1;
        }

        float fraction;
        PathPoint pathPoint;
        //组装每个item所在path的位置
        for (int i = mFirstVisibleItem; i <= endIndex; i++) {
            curDis = i * itemOffset - getScrollOffset();
            fraction = curDis / pathKeyFrame.getPathLength();
            pathPoint = pathKeyFrame.getValue(fraction);
            if (pathPoint == null) {
                continue;
            }
            result.add(new PathPoint(pathPoint, i, fraction));
        }
        return result;
    }

    /**
     * 获取当前的滑动偏移量
     */
    private int getScrollOffset() {
        return vertical ? dyCount : dxCount;
    }

    /**
     * 布局子view
     */
    private void onLayout(RecyclerView.Recycler recycler, List<PathPoint> needItems) {
        View item;
        int left, top, cFW, cFH;
        for (int i = 0; i < needItems.size(); i++) {
            PathPoint pathPoint = needItems.get(i);
            item = recycler.getViewForPosition(pathPoint.index);
            addView(item);
            measureChild(item, 0, 0);
            //获取item所占宽高
            cFW = getDecoratedMeasuredWidth(item);
            cFH = getDecoratedMeasuredHeight(item);
            updateFirstAndLast(pathPoint.index, cFW, cFH);
            //计算item的左上点坐标
            left = (int) pathPoint.x - cFW / 2;
            top = (int) pathPoint.y - cFH / 2;
            //布局
            layoutDecorated(item, left, top, left + cFW, top + cFH);
            //根据路径的角度旋转
            item.setRotation(pathPoint.getChildAngle() - (vertical ? 90f : 0));
        }
    }

    /**
     * 更新 第一个和最后一个视图的高度
     */
    private void updateFirstAndLast(int index, int cFW, int cFH) {
        if (index == 0) {
            firstW = cFW;
            firstH = cFW;
        } else if (index + 1 == getItemCount()) {
            lastW = cFW;
            lastH = cFH;
        }
    }

    private void relayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        List<PathPoint> items = isCirculateMode() ? getNeedLayoutCirculateItems() : getNeedLayoutItems(state.getItemCount());
        if (items.isEmpty()) {
            return;
        }
        onLayout(recycler, items);
        recycleChildren(recycler);
    }

    @Override
    public boolean canScrollHorizontally() {
        return !vertical;
    }

    @Override
    public boolean canScrollVertically() {
        return vertical;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return scrollBy(dx, recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return scrollBy(dy, recycler, state);
    }

    /**
     * 返回消耗额滑动距离
     */
    private int scrollBy(int delta, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0 || delta == 0) {
            return 0;
        }
        //剥离屏幕上显示的item
        detachAndScrapAttachedViews(recycler);
        //获取之前滑动的距离
        int before = vertical ? dyCount : dxCount;
        //计算滑动之后的滑动距离
        int after;
        if (isCirculateScroll()) {
            after = updateOffsetInCirculateMode(before, delta);
        } else if (isOverflowMode()) {
            after = updateOffsetInOverflowMode(before, delta);
        } else {
            after = updateOffsetInNormal(before, delta);
        }
        if (vertical) {
            dyCount = after;
        } else {
            dxCount = after;
        }
        relayoutChildren(recycler, state);
        //返回消耗的滑动距离
        return after == before ? 0 : delta;
    }

    /**
     * 是否达到循环滑动的条件
     * <p>
     * 开启循环滑动 并且 内容长度 大于 路径长度和间距之和
     */
    private boolean isCirculateScroll() {
        return isCirculateMode() && getItemContentLength() - pathKeyFrame.getPathLength() > itemOffset;
    }

    /**
     * 循环模式下更新滑动距离
     */
    private int updateOffsetInCirculateMode(int scrollCount, int delta) {
        //更新滚动的距离
        scrollCount += delta;
        //获取路径的总长度
        float pathLength = pathKeyFrame.getPathLength();
        //获取item的最大长度
        int itemLength = getItemContentLength();
        if (scrollCount > itemLength) {
            scrollCount %= itemLength;
            //因为是向前偏移了一个Item的距离
            scrollCount -= itemOffset;
        } else if (scrollCount <= -pathLength) {
            scrollCount += itemLength;
            scrollCount += itemOffset;
        }
        return scrollCount;
    }

    /**
     * 允许所有item超过屏幕模式下更新滑动距离
     */
    private int updateOffsetInOverflowMode(int scrollCount, int delta) {
        //更新滚动的距离
        scrollCount += delta;
        //获取路径的总长度
        float pathLength = pathKeyFrame.getPathLength();
        //获取item的最大长度  最后一个item的高度的一半(因为path的点在item的中心) + item的总间距
        int itemLength = getItemContentLength();
        //第一个视图的宽或高
        int wh = vertical ? firstH : firstW;
        if (scrollCount < -pathLength - wh) {
            //向下全部滑出 则不能再滑动
            scrollCount = (int) -pathLength - wh;
        } else if (scrollCount > itemLength) {
            //向上全部滑出 则不能再滑动
            scrollCount = itemLength;
        }
        return scrollCount;
    }

    /**
     * 正常模式下更新滑动距离
     */
    private int updateOffsetInNormal(int scrollCount, int delta) {
        //更新滚动的距离
        scrollCount += delta;
        //获取路径的总长度
        float pathLength = pathKeyFrame.getPathLength();
        //获取item的最大长度  最后一个item的高度的一半(因为path的点在item的中心) + item的总间距
        int itemLength = getItemContentLength();
        //item总长度相对于路径总长度多出来的部分
        float overflowLength = itemLength - pathLength;

        if (scrollCount < 0) {
            //滑动到顶部 避免第一个item脱离顶部
            scrollCount = 0;
        } else if (scrollCount > overflowLength) {
            //滑动到底部
            //如果列表能滚动的话，则直接设置为可滑动的最大距离，避免最后一个item向上移
            if (itemLength > pathLength) {
                //内容长度大于路径长度  则直接滑动到最底部
                scrollCount = (int) overflowLength;
            } else {
                //内容长度小于路径长度  不滑动
                scrollCount -= delta;
            }
        }
        return scrollCount;
    }

    /**
     * 获取item内容的总长度  间距+ 第一个或者最后一个 的 宽度或高度的一半
     */
    private int getItemContentLength() {
        int childDelta;
        if (getScrollOffset() < 0) {
            //下滑 first
            childDelta = vertical ? firstH : firstW;
        } else {
            //上划 last
            childDelta = vertical ? lastH : lastW;
        }
        return (getItemCount() - 1) * itemOffset + childDelta / 2;
    }

    /**
     * 回收屏幕外需回收的Item
     */
    private void recycleChildren(RecyclerView.Recycler recycler) {
        List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
        for (int i = 0; i < scrapList.size(); i++) {
            RecyclerView.ViewHolder holder = scrapList.get(i);
            removeView(holder.itemView);
            recycler.recycleView(holder.itemView);
        }
    }

}
