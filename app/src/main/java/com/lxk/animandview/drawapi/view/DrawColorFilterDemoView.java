package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.view.View;

import com.lxk.animandview.R;
import com.lxk.animandview.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/7 17:36
 */
public class DrawColorFilterDemoView extends View {
    private Paint fillPaint, textPaint;
    private int width, height;
    private Bitmap bitmap;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public DrawColorFilterDemoView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        fillPaint.setStrokeWidth(Utils.dpToPx(context, 2));
        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setTextSize(Utils.dpToPx(context, 12));
        textPaint.setColor(Color.WHITE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }


    private void draw(Canvas canvas, Rect rect, int gapH, String type, String txt) {
        canvas.drawRect(rect, fillPaint);
        canvas.drawBitmap(bitmap, 0, gapH, fillPaint);
        canvas.drawText(type, 0, gapH / 4, textPaint);
        canvas.drawText(txt, 0, gapH * 3 / 4, textPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(255, 255, 255, 255));
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        int gapW = width / 10;
        int gapH = height / 10;

        fillPaint.setColor(Color.argb(255, 103, 58, 183));
        Rect rect = new Rect(0, 0, 2 * gapW, gapH);
        draw(canvas, rect, gapH, "", "原色");

        int translateX = gapW * 5 / 2;
        int translateY = gapH * 2;

        canvas.save();

        //ColorMatrixColorFilter、LightingColorFilter、PorterDuffColorFilter

//--------------------------------------------------------------------------------------------------

        /**
         * ColorMatrixColorFilter  颜色矩阵
         * 参考{@link DrawColorMatrixDemoView}
         */

//--------------------------------------------------------------------------------------------------
        //LightingColorFilter 光照颜色过滤器
        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new LightingColorFilter(0xffffff, 0x0000f0));
        draw(canvas, rect, gapH, "LightingColor", "蓝色增强F0");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new LightingColorFilter(0xffffff, 0x00f000));
        draw(canvas, rect, gapH, "LightingColor", "绿色增强F0");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new LightingColorFilter(0xffffff, 0xf00000));
        draw(canvas, rect, gapH, "LightingColor", "红色增强F0");

        canvas.restore();


//--------------------------------------------------------------------------------------------------

        //PorterDuffColorFilter 颜色滤镜，也叫图形混合滤镜
        //Mode.ADD(饱和度相加)，Mode.DARKEN（变暗），Mode.LIGHTEN（变亮），
        //Mode.MULTIPLY（正片叠底），Mode.OVERLAY（叠加），Mode.SCREEN（滤色）
        textPaint.setColor(Color.BLACK);

        canvas.save();
        canvas.translate(0, translateY);

        int colorFilter = Color.RED;
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.ADD));
        draw(canvas, rect, gapH, "PorterDuff", "ADD");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DARKEN));
        draw(canvas, rect, gapH, "PorterDuff", "DARKEN");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.LIGHTEN));
        draw(canvas, rect, gapH, "PorterDuff", "LIGHTEN");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY));
        draw(canvas, rect, gapH, "PorterDuff", "MULTIPLY");

        canvas.restore();

        canvas.save();

        canvas.translate(0, translateY * 2);

        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.OVERLAY));
        draw(canvas, rect, gapH, "PorterDuff", "OVERLAY");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SCREEN));
        draw(canvas, rect, gapH, "PorterDuff", "SCREEN");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.XOR));
        draw(canvas, rect, gapH, "PorterDuff", "XOR");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.CLEAR));
        draw(canvas, rect, gapH, "PorterDuff", "CLEAR");


        canvas.restore();

        canvas.save();

        canvas.translate(0, translateY * 3);

        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SRC));
        draw(canvas, rect, gapH, "PorterDuff", "SRC");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SRC_ATOP));
        draw(canvas, rect, gapH, "PorterDuff", "SRC_ATOP");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DST));
        draw(canvas, rect, gapH, "PorterDuff", "DST");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DST_ATOP));
        draw(canvas, rect, gapH, "PorterDuff", "DST_ATOP");


        canvas.restore();
        canvas.save();

        canvas.translate(0, translateY * 4);

        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DST_IN));
        draw(canvas, rect, gapH, "PorterDuff", "DST_IN");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SRC_IN));
        draw(canvas, rect, gapH, "PorterDuff", "SRC_IN");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DST_OUT));
        draw(canvas, rect, gapH, "PorterDuff", "DST_OUT");

        canvas.translate(translateX, 0);
        fillPaint.setColorFilter(new PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SRC_OUT));
        draw(canvas, rect, gapH, "PorterDuff", "SRC_OUT");
    }


}
