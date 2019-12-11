package com.lxk.animandcustomviewdemo.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        Log.e(TAG, "onDraw: ");
        c.save();
        int cCount = parent.getChildCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        for (int i = 0; i < cCount; i++) {
            View child = parent.getChildAt(i);
            int left = layoutManager.getLeftDecorationWidth(child);
            int x = left / 2;
            int y = child.getTop() + child.getHeight() / 2;
            int radius = Math.min(left, child.getHeight()) / 2;
            c.drawCircle(x, y, radius, mPaint);
            Path p = new Path();
            p.moveTo(child.getLeft(), child.getTop());
            p.lineTo(child.getRight(), child.getTop());
            p.lineTo(child.getRight(), child.getBottom());
            p.lineTo(child.getLeft(), child.getBottom());
            p.close();
            c.drawPath(p, mPaint);
        }
        c.restore();
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        Log.e(TAG, "onDrawOver: ");
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        Log.e(TAG, "getItemOffsets: ");
        outRect.bottom = 10;
        outRect.top = 10;
        outRect.right = 10;
        outRect.left = 10;
    }
}
