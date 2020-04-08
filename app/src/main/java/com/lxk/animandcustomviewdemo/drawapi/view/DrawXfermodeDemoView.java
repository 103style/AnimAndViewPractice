package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/7 20:19
 * doc:https://blog.csdn.net/harvic880925/article/details/51264653
 * doc:https://developer.android.com/reference/android/graphics/PorterDuff.Mode.html
 */
public class DrawXfermodeDemoView extends View {

    private Paint fillPaint, textPaint;
    private int width, height;
    private Bitmap src, dst;

    public DrawXfermodeDemoView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setTextSize(Utils.doToPx(context, 12));
        textPaint.setColor(Color.parseColor("#FFF44336"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        src = makeSrc(w / 5);
        dst = makeDst(w / 5);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.WHITE);
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        int gapW = width / 10;
        int gapH = height / 10;

        int translateX = gapW * 5 / 2;
        int translateY = gapW * 3;

        xfermodeDraw(canvas, PorterDuff.Mode.CLEAR, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.SRC, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.DST, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.SRC_OVER, gapW);

        canvas.restore();

        canvas.save();

        canvas.translate(0, translateY);
        xfermodeDraw(canvas, PorterDuff.Mode.DST_OVER, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.SRC_IN, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.DST_IN, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.SRC_OUT, gapW);

        canvas.restore();

        canvas.save();

        canvas.translate(0, translateY * 2);
        xfermodeDraw(canvas, PorterDuff.Mode.DST_OUT, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.SRC_ATOP, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.DST_ATOP, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.XOR, gapW);

        canvas.restore();

        canvas.save();

        canvas.translate(0, translateY * 3);
        xfermodeDraw(canvas, PorterDuff.Mode.DARKEN, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.LIGHTEN, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.MULTIPLY, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.SCREEN, gapW);


        canvas.restore();

        canvas.save();

        canvas.translate(0, translateY * 4);
        xfermodeDraw(canvas, PorterDuff.Mode.ADD, gapW);
        canvas.translate(translateX, 0);
        xfermodeDraw(canvas, PorterDuff.Mode.OVERLAY, gapW);
    }

    private void xfermodeDraw(Canvas canvas, PorterDuff.Mode mode, int gapW) {
        int l = gapW * 5 / 2;
        int layerId = canvas.saveLayer(0, 0, l, l, fillPaint);
        canvas.drawBitmap(dst, 0, 0, fillPaint);
        fillPaint.setXfermode(new PorterDuffXfermode(mode));
        //类似google示例中dst src 的完全重叠
        canvas.drawBitmap(src, 0, 0, fillPaint);
        //还原图层
        fillPaint.setXfermode(null);
        canvas.restoreToCount(layerId);

        String txt = mode.toString();
        Paint.FontMetrics fontMetrics = fillPaint.getFontMetrics();
        float dy = Math.abs(fontMetrics.top);
        canvas.drawText(txt, (gapW * 2 - textPaint.measureText(txt)) / 2, l / 2 - dy, textPaint);
    }


    private Bitmap makeDst(int w) {
        Bitmap bm = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF66AAFF);
        c.drawCircle(w / 2, w / 2, w / 4, p);
        return bm;
    }

    private Bitmap makeSrc(int w) {
        Bitmap bm = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        c.drawRect(w / 2, w / 2, w, w, p);
        return bm;
    }

}
