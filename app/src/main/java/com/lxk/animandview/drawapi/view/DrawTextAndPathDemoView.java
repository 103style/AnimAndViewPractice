package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lxk.animandview.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/4 17:01
 */
public class DrawTextAndPathDemoView extends View {
    private Paint fillPaint, strokePaint, textPaint;
    private int width, height;
    private Path path;

    public DrawTextAndPathDemoView(Context context) {
        this(context, null);
    }

    public DrawTextAndPathDemoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawTextAndPathDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        strokePaint = Utils.initPaint(context, Paint.Style.STROKE);

        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        //设置阴影
        textPaint.setShadowLayer(10, 15, 15, Color.BLACK);
        textPaint.setTextSize(Utils.dpToPx(context, 14));

        path = new Path();
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

        String text = "103style的测试文字";
        canvas.drawText(text, gapW * 5, gapH, textPaint);
        textPaint.clearShadowLayer();

        //直线路径
        path.moveTo(gapW, gapH);
        path.lineTo(gapW, gapH * 2);
        path.lineTo(gapW * 5, gapH * 2);
        path.close();
        canvas.drawPath(path, fillPaint);

        //矩形路径
        path.reset();
        RectF rectFCW = new RectF(gapW * 2, gapH * 3, gapW * 5, gapH * 4);
        //顺时针方向
        path.addRect(rectFCW, Path.Direction.CW);
        //绘制路径
        canvas.drawPath(path, strokePaint);

        float offset = 0;
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint);

        path.reset();
        RectF rectFCCW = new RectF(gapW * 6, gapH * 3, gapW * 9, gapH * 4);
        //逆时针方向
        path.addRect(rectFCCW, Path.Direction.CCW);
        //绘制路径
        canvas.drawPath(path, strokePaint);
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint);

        //圆角矩形路径
        path.reset();
        RectF rect = new RectF(gapW * 2, gapH * 5, gapW * 5, gapH * 6);
        path.addRoundRect(rect, 10, 10, Path.Direction.CW);
        //绘制路径
        canvas.drawPath(path, strokePaint);
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint);


        //圆形路径
        path.reset();
        path.addCircle(gapW * 7, gapH * 5 + gapW, gapW, Path.Direction.CW);
        //绘制路径
        canvas.drawPath(path, strokePaint);
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint);


        //椭圆形路径
        path.reset();
        RectF ovalRectF = new RectF(gapW * 2, gapH * 7, gapW * 5, gapH * 8);
        path.addOval(ovalRectF, Path.Direction.CW);
        //绘制路径
        canvas.drawPath(path, strokePaint);
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint);


        //椭圆形路径
        path.reset();
        RectF arcRectF = new RectF(gapW * 6, gapH * 7, gapW * 9, gapH * 8);
        path.addArc(arcRectF, 0, 120);
        //绘制路径
        canvas.drawPath(path, strokePaint);
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint);

        //向左倾斜
        textPaint.setTextSkewX(0.5f);
        //设置下划线
        textPaint.setUnderlineText(true);
        canvas.drawText("向左倾斜+下划线", gapW, gapH * 17 / 2, textPaint);

        //设置带有删除线效果
        textPaint.setStrikeThruText(true);
        //取消下划线
        textPaint.setUnderlineText(false);
        ////向右倾斜
        textPaint.setTextSkewX(-0.5f);
        canvas.drawText("向右倾斜+删除线", width / 2, gapH * 17 / 2, textPaint);

        //去掉删除线效果
        textPaint.setStrikeThruText(false);
        textPaint.setTextScaleX(2);
        textPaint.setTextSkewX(0);
        canvas.drawText("横向拉伸2倍", gapW, gapH * 9, textPaint);

    }

}
