package com.lxk.animandview.practice.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.lxk.animandview.utils.DensityUtils;

/**
 * @author https://github.com/103style
 * @date 2020/4/28 16:30
 * 用于绘制 {@link PathLayoutManager} 的路径
 */
public class LayoutManagerPathView extends View {
    Paint paint;
    Path path;

    public LayoutManagerPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtils.dpToPx(context, 4));
    }

    public void setPaintColor(@ColorInt int color) {
        this.paint.setColor(color);
    }

    public void setPath(Path path) {
        this.path = path;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (path != null) {
            canvas.drawPath(path, paint);
        }
    }
}
