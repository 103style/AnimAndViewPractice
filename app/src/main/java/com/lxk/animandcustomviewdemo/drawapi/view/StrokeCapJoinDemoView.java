package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/7 11:41
 */
public class StrokeCapJoinDemoView extends View {
    private Paint fillPaint, strokePaint;
    private int width, height;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public StrokeCapJoinDemoView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        strokePaint = Utils.initPaint(context, Paint.Style.STROKE);
        int size = Utils.doToPx(context, 12);
        fillPaint.setStrokeWidth(size);
        fillPaint.setTextSize(size);
        strokePaint.setStrokeWidth(size);
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

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        int gapW = width / 10;
        int gapH = height / 10;

        //线冒 Paint.Cap.BUTT（默认）  Paint.Cap.ROUND（圆形）  Paint.Cap.SQUARE（方形）

        //默认线冒
        canvas.drawLine(gapW, gapH, gapW * 6, gapH, fillPaint);
        canvas.drawText("Cap Default", gapW * 7, gapH, fillPaint);

        //默认线冒
        fillPaint.setStrokeCap(Paint.Cap.BUTT);
        canvas.drawLine(gapW, gapH * 2, gapW * 6, gapH * 2, fillPaint);
        canvas.drawText("Cap.BUTT", gapW * 7, gapH * 2, fillPaint);

        //圆形线帽，两端比默认多一个线冒的长度
        fillPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(gapW, gapH * 3, gapW * 6, gapH * 3, fillPaint);
        canvas.drawText("Cap.ROUND", gapW * 7, gapH * 3, fillPaint);

        //方形线冒，两端比默认多一个线冒的长度
        fillPaint.setStrokeCap(Paint.Cap.SQUARE);
        canvas.drawLine(gapW, gapH * 4, gapW * 6, gapH * 4, fillPaint);
        canvas.drawText("Cap.SQUARE", gapW * 7, gapH * 4, fillPaint);


        //连接处样式 Join.MITER（默认 ） Join.ROUND（圆角） Join.BEVEL（直线）

        Path path = new Path();
        path.moveTo(gapW, gapH * 5);
        path.lineTo(gapW * 2, gapH * 5);
        path.lineTo(gapW * 2, gapH * 6);
        canvas.drawPath(path, strokePaint);
        canvas.drawText("Join Default", gapW, gapH * 13 / 2, fillPaint);

        strokePaint.setStrokeJoin(Paint.Join.MITER);
        path.reset();
        path.moveTo(gapW * 3, gapH * 5);
        path.lineTo(gapW * 4, gapH * 5);
        path.lineTo(gapW * 4, gapH * 6);
        canvas.drawPath(path, strokePaint);
        canvas.drawText("Join.MITER", gapW * 3, gapH * 13 / 2, fillPaint);

        strokePaint.setStrokeJoin(Paint.Join.ROUND);
        path.reset();
        path.moveTo(gapW * 5, gapH * 5);
        path.lineTo(gapW * 6, gapH * 5);
        path.lineTo(gapW * 6, gapH * 6);
        canvas.drawPath(path, strokePaint);
        canvas.drawText("Join.ROUND", gapW * 5, gapH * 13 / 2, fillPaint);


        strokePaint.setStrokeJoin(Paint.Join.BEVEL);
        path.reset();
        path.moveTo(gapW * 7, gapH * 5);
        path.lineTo(gapW * 8, gapH * 5);
        path.lineTo(gapW * 8, gapH * 6);
        canvas.drawPath(path, strokePaint);
        canvas.drawText("Join.BEVEL", gapW * 7, gapH * 13 / 2, fillPaint);


    }
}
