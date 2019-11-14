package com.lxk.customviewlearndemo.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 17:00
 */
public class TestLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }
}
