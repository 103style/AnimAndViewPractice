package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.lxk.animandview.R;
import com.lxk.animandview.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/21 18:53
 */
public class ShadeLayerDemoView extends View {
    private Paint fillPaint, textPaint;
    private Bitmap iconBitmap;
    private int gap;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public ShadeLayerDemoView(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setTextSize(Utils.dpToPx(getContext(), 12));
        iconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        gap = w / 8;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.WHITE);

        canvas.drawText("w 为 屏幕宽度", gap, gap / 2, textPaint);

        int bW = iconBitmap.getWidth();
        int bH = iconBitmap.getHeight();

        canvas.drawBitmap(iconBitmap, 0, gap, fillPaint);
        canvas.drawText("NoShaowLayer", gap + bW, gap + bH / 2, textPaint);

        canvas.translate(0, gap + bH);

        fillPaint.setShadowLayer(gap, gap / 2, 0, Color.RED);
        canvas.drawBitmap(iconBitmap, 0, gap, fillPaint);
        canvas.drawText("ShaowLayer raduis=w/8, dx=w/16, dy=0", gap + bW, gap + bH / 2, textPaint);


        canvas.translate(0, gap + bH);

        fillPaint.setShadowLayer(gap * 2, gap, gap, Color.RED);
        canvas.drawBitmap(iconBitmap, 0, gap, fillPaint);
        canvas.drawText("ShaowLayer raduis=w/4, dx=w/8, dy=w/16", gap + bW, gap + bH / 2, textPaint);


        canvas.translate(0, gap * 2 + bH);
        textPaint.setShadowLayer(Utils.dpToPx(getContext(), 2), gap / 2, gap / 2, Color.DKGRAY);
        textPaint.setTextSize(Utils.dpToPx(getContext(), 16));
        canvas.drawText("这是文字阴影测试！半径2dp, x y偏移 w/16", 0, gap, textPaint);
    }
}