package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/3/31 11:41
 */
public class DrawSimpleDemoView extends View {
    private Paint fillPaint, strokePaint;
    private int width, height;

    public DrawSimpleDemoView(Context context) {
        this(context, null);
    }

    public DrawSimpleDemoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawSimpleDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
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

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        //—————————— 以下为示例  开发时不要在 onDraw中创建对象 ——————————

        int gapW = width / 10;
        int gapH = height / 10;

        //画线
        canvas.drawLine(0, 0, gapW, gapH, fillPaint);

        canvas.drawLines(new float[]{gapW * 2, gapH, gapW * 3, 0,
                gapW * 3, 0, gapW * 4, gapH,
                gapW * 4, gapH, gapW * 5, 0}, strokePaint);

        //画点
        int y = gapH * 3 / 2;
        canvas.drawPoint(gapW, y, fillPaint);
        canvas.drawPoints(new float[]{gapW * 2, y / 3, gapW * 3, y, gapW * 4, y, gapW * 5, y, gapW * 6, y}, strokePaint);

        //画圆
        canvas.drawCircle(gapW * 3, gapH * 3, gapW, fillPaint);

        canvas.drawCircle(gapW * 7, gapH * 3, gapW, strokePaint);


        //使用RectF构造矩形
        RectF rect = new RectF(gapW, gapH * 4, width * 2 / 10, gapH * 5);
        canvas.drawRect(rect, fillPaint);
        //使用Rect构造矩形
        Rect rect2 = new Rect(gapW * 3, gapH * 4, gapW * 5, gapH * 5);
        canvas.drawRect(rect2, strokePaint);

        //使用Rect构造圆角矩形
        RectF rect3 = new RectF(gapW * 6, gapH * 4, width * 8 / 10, gapH * 5);
        canvas.drawRoundRect(rect3, 20, 10, fillPaint);

        //圆弧
        RectF rect4 = new RectF(gapW, gapH * 5, width * 4 / 10, gapH * 6);
        canvas.drawArc(rect4, 0, 90, true, fillPaint);
        RectF rect5 = new RectF(gapW * 6, gapH * 5, width * 9 / 10, gapH * 6);
        canvas.drawArc(rect5, 0, 90, false, strokePaint);

        //椭圆
        RectF rect6 = new RectF(gapW, gapH * 7, gapW * 4, gapH * 8);
        canvas.drawOval(rect6, fillPaint);
        //椭圆
        RectF rect7 = new RectF(gapW * 6, gapH * 7, gapW * 9, gapH * 8);
        canvas.drawOval(rect7, strokePaint);
    }

}
