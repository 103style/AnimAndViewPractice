package com.lxk.customviewlearndemo.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author xiaoke.luo@tcl.com 2019/11/15 18:00
 */
public class TestRecyclerView extends RecyclerView {
    public TestRecyclerView(Context context) {
        this(context, null);
    }

    public TestRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setChildrenDrawingOrderEnabled(true);
    }

    public TestLayout4Manager getCoverFlowLayout() {
        return ((TestLayout4Manager) getLayoutManager());
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        //计算正在显示的所有Item的中间位置
        int center = getCoverFlowLayout().getCenterPosition() - getCoverFlowLayout().getFirstVisiblePosition();
        int order;

        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        int flingX = (int) (velocityX * 0.40f);
        return super.fling(flingX, velocityY);
    }
}
