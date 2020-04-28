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
            item.setRotation(pathPoint.getChildAngle());
        }
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
        dxCount += dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        dyCount += dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

}
