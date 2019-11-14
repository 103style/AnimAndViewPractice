package com.lxk.customviewlearndemo.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 16:47
 */
public class TestItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "TestItemDecoration";

    private Paint mPaint;

    public TestItemDecoration() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Log.e(TAG, "onDraw: ");
        int cCount = parent.getChildCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        for (int i = 0; i < cCount; i++) {
            View child = parent.getChildAt(i);
            int left = layoutManager.getLeftDecorationWidth(child);
            int cy = child.getTop() + child.getHeight() / 2;
            c.drawCircle(left / 2, cy, left / 2, mPaint);
            c.drawLine(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom(), mPaint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        Log.e(TAG, "onDrawOver: ");
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        Log.e(TAG, "getItemOffsets: ");
        outRect.bottom = 10;
        outRect.left = 50;
        outRect.right = 50;
    }
}
