package com.lxk.animandview.practice.path;

import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2020/4/27 20:19
 */
public class PathLayoutManager extends RecyclerView.LayoutManager {

    /**
     * 路径
     */
    private Path mPath;
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
     * 可见的总数
     */
    private int visibleCount;

    private int dxCount, dyCount;

    private int mFirstVisibleItem;

    public PathLayoutManager(@NonNull Path mPath,
                             @IntRange(from = 1, to = Integer.MAX_VALUE) int itemOffset) {
        this(mPath, itemOffset, RecyclerView.VERTICAL);
    }

    public PathLayoutManager(@NonNull Path mPath,
                             @IntRange(from = 1, to = Integer.MAX_VALUE) int itemOffset,
                             @RecyclerView.Orientation int orientation) {
        this.mPath = mPath;
        this.itemOffset = itemOffset;
        vertical = orientation == RecyclerView.VERTICAL;
        updatePath(mPath);
    }

    /**
     * 更新路径
     */
    public void updatePath(@NonNull Path mPath) {
        this.mPath = mPath;
        pathKeyFrame = new PathKeyFrame(mPath);
        visibleCount = (int) (pathKeyFrame.getPathLength() / itemOffset) + 1;
//        requestLayout();
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
        List<PathPoint> needItems = getNeedLayoutItems(state.getItemCount());
        if (needItems.isEmpty()) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        onLayout(recycler, needItems);
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
        for (PathPoint pathPoint : needItems) {
            item = recycler.getViewForPosition(pathPoint.index);
            addView(item);
            measureChild(item, 0, 0);
            //获取item所占宽高
            cFW = getDecoratedMeasuredWidth(item);
            cFH = getDecoratedMeasuredHeight(item);
            //计算item的左上点坐标
            left = (int) pathPoint.x - cFW / 2;
            top = (int) pathPoint.y - cFH / 2;
            //布局
            layoutDecorated(item, left, top, left + cFW, top + cFH);
            //根据路径的角度旋转
            item.setRotation(pathPoint.getChildAngle() - (vertical ? 90f : 0));
        }
    }

    private void relayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        List<PathPoint> items = getNeedLayoutItems(state.getItemCount());
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
        if (getChildCount() == 0 || delta == 0) {
            return 0;
        }
        //剥离屏幕上显示的item
        detachAndScrapAttachedViews(recycler);
        //获取之前滑动的距离
        int before = vertical ? dyCount : dxCount;
        //计算滑动之后的滑动距离
        int after = updateOffset(before, delta);
        relayoutChildren(recycler, state);
        //返回消耗的滑动距离
        return after == before ? 0 : delta;
    }

    /**
     * 更新滑动距离
     */
    private int updateOffset(int count, int delta) {
        //更新滚动的距离
        count += delta;
        //获取路径的总长度
        float pathLength = pathKeyFrame.getPathLength();
        //获取item的最大长度  最后一个item的高度的一半(因为path的点在item的中心) + item的总间距
        int itemLength = getItemOffsetCount();
        //item总长度相对于路径总长度多出来的部分
        float overflowLength = itemLength - pathLength;
        if (count < 0) {
            //滑动到顶部 避免第一个item脱离顶部
            count = 0;
        } else if (count > overflowLength) {
            //滑动到底部
            //如果列表能滚动的话，则直接设置为可滑动的最大距离，避免最后一个item向上移
            if (itemLength > pathLength) {
                //内容长度大于路径长度  则直接滑动到最底部
                count = (int) overflowLength;
            } else {
                //内容长度小于路径长度  不滑动
                count -= delta;
            }
        }
        if (vertical) {
            dyCount = count;
        } else {
            dxCount = count;
        }
        return count;
    }

    /**
     * 获取item的总间距
     */
    private int getItemOffsetCount() {
        return (getItemCount() - 1) * itemOffset;
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
