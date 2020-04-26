package com.lxk.animandview.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author https://github.com/103style
 * @date 2020/4/7 13:48
 */
public class DensityUtils {

    public static Paint initPaint(Context context, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔颜色
        paint.setColor(Color.RED);
        //设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        paint.setStyle(style);
        //设置画笔宽度
        paint.setStrokeWidth(dpToPx(context, 6));
        return paint;
    }

    public static int dpToPx(Context context, int value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }
}
