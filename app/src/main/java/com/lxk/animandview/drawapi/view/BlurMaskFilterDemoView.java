package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.lxk.animandview.utils.DensityUtils;

/**
 * @author https://github.com/103style
 * @date 2020/4/21 19:41
 */
public class BlurMaskFilterDemoView extends View {
    private Paint fillPaint, textPaint;
    private int gap;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public BlurMaskFilterDemoView(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        fillPaint = DensityUtils.initPaint(context, Paint.Style.FILL);
        textPaint = DensityUtils.initPaint(context, Paint.Style.FILL);
        textPaint.setTextSize(DensityUtils.dpToPx(getContext(), 14));
        textPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        gap = w / 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        canvas.translate(0, gap);

        canvas.drawText("w 为 屏幕宽度, BlurMaskFilter的半径为 w/10 ", gap, gap / 2, textPaint);

        canvas.translate(0, gap);

        fillPaint.setMaskFilter(new BlurMaskFilter(gap, BlurMaskFilter.Blur.NORMAL));
        canvas.drawCircle(gap * 2, gap, gap, fillPaint);
        canvas.drawText("Blur.NORMAL:内外发光", gap * 3, gap / 2, textPaint);


        canvas.translate(0, gap * 3);
        fillPaint.setMaskFilter(new BlurMaskFilter(gap, BlurMaskFilter.Blur.INNER));
        canvas.drawCircle(gap * 2, gap, gap, fillPaint);
        canvas.drawText("Blur.INNER:内发光", gap * 3, gap / 2, textPaint);


        canvas.translate(0, gap * 3);
        fillPaint.setMaskFilter(new BlurMaskFilter(gap, BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(gap * 2, gap, gap, fillPaint);
        canvas.drawText("Blur.SOLID:外发光", gap * 3, gap / 2, textPaint);

        canvas.translate(0, gap * 3);
        fillPaint.setMaskFilter(new BlurMaskFilter(gap, BlurMaskFilter.Blur.OUTER));
        canvas.drawCircle(gap * 2, gap, gap, fillPaint);
        canvas.drawText("Blur.OUTER:仅发光部分可见", gap * 3, gap / 2, textPaint);


    }
}