package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.lxk.animandview.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/6 12:35
 */
public class CanvasChangeDemo extends View {
    private Paint fillPaint, strokePaint;
    private int width, height;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public CanvasChangeDemo(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        fillPaint.setTextSize(Utils.dpToPx(context, 16));
        strokePaint = Utils.initPaint(context, Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        int gapW = width / 10;
        int gapH = height / 10;

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));
        //保存的画布大小为全屏幕大小
        canvas.save();
        canvas.clipRect(new Rect(gapW, gapH, width - gapW, height - gapH));
        canvas.drawColor(Color.argb(255, 0, 0, 255));
        //将栈顶的画布状态取出来，作为当前画布，并画成黄色背景
        canvas.restore();

        canvas.save();
        canvas.clipRect(new Rect(gapW * 2, gapH * 2, width - gapW * 2, height - gapH * 2));
        canvas.drawColor(Color.argb(255, 0, 64, 255));
        canvas.restore();

        canvas.save();
        canvas.clipRect(new Rect(gapW * 3, gapH * 3, width - gapW * 3, height - gapH * 3));
        canvas.drawColor(Color.argb(255, 0, 128, 255));
        canvas.restore();

        canvas.save();
        canvas.clipRect(new Rect(gapW * 4, gapH * 4, width - gapW * 4, height - gapH * 4));
        canvas.drawColor(Color.argb(255, 0, 192, 255));
        canvas.restore();


        //保存当前的画布到对应的栈中
        canvas.save();
        drawCircle(canvas, gapW);
        //画布原点向右向下平移gapH
        canvas.translate(gapH, gapH);
        drawCircle(canvas, gapW);
        //画布原点向右向上平移gapH
        canvas.translate(gapH, -gapH);
        drawCircle(canvas, gapW);
        //弹出之前保存的画布 继续在其上绘画，与save一起出现
        canvas.restore();

        canvas.save();
        fillPaint.setColor(Color.MAGENTA);
        strokePaint.setColor(Color.MAGENTA);
        Rect rect = new Rect(gapW * 3, gapH * 3, gapW * 5, gapH * 4);
        canvas.drawRect(rect, strokePaint);
        canvas.drawText("旋转前", gapW * 3, gapH * 3, fillPaint);
        //旋转操作
        canvas.rotate(-30);
        canvas.drawRect(rect, strokePaint);
        canvas.drawText("旋转-30°后", gapW * 3, gapH * 3, fillPaint);
        canvas.restore();


        canvas.save();
        fillPaint.setColor(Color.GREEN);
        canvas.drawText("放缩前", gapW, gapH * 3, fillPaint);
        //放缩操作
        canvas.scale(2, 2);
        canvas.drawText("x、y扩大2倍后", gapW, gapH * 3, fillPaint);
        //放缩操作
        canvas.scale(0.8f, 0.8f);
        canvas.drawText("x、y缩小到2倍的80%后", gapW, gapH * 3, fillPaint);
        canvas.restore();


        canvas.save();
        fillPaint.setColor(Color.WHITE);
        canvas.drawText("扭曲操作前", gapW * 4, gapH, fillPaint);
        //扭曲操作
        canvas.skew(2, 2);
        canvas.drawText("扭曲操作后", gapW * 4, gapH, fillPaint);
        canvas.restore();
    }

    private void drawCircle(Canvas canvas, int w) {
        canvas.drawCircle(w, w, w, fillPaint);
    }
}
