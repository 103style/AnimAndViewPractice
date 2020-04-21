package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.View;

import com.lxk.animandview.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/10 16:33
 */
public class RadialGradientModeView extends View {
    private Paint fillPaint, textPaint;
    private RadialGradient clampRadialGradient;
    private RadialGradient repeatRadialGradient;
    private RadialGradient mirrorRadialGradient;
    private int width, height, rectH, gap;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public RadialGradientModeView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(Utils.dpToPx(context, 20));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        rectH = h / 4;
        gap = rectH / 4;
        clampRadialGradient = new RadialGradient(w / 2, rectH / 2, rectH / 4,
                Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
        repeatRadialGradient = new RadialGradient(w / 2, rectH / 2, rectH / 4,
                Color.RED, Color.GREEN, Shader.TileMode.REPEAT);
        mirrorRadialGradient = new RadialGradient(w / 2, rectH / 2, rectH / 4,
                Color.RED, Color.GREEN, Shader.TileMode.MIRROR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        canvas.save();
        canvas.translate(0, gap);
        fillPaint.setShader(clampRadialGradient);
        canvas.drawCircle(width / 2, rectH / 2, rectH / 2, fillPaint);
        String txt = "Shader.TileMode.CLAMP";
        canvas.drawText(txt, (getMeasuredWidth() - textPaint.measureText(txt)) / 2, rectH / 2, textPaint);

        canvas.translate(0, gap + rectH);
        fillPaint.setShader(repeatRadialGradient);
        canvas.drawCircle(width / 2, rectH / 2, rectH / 2, fillPaint);
        txt = "Shader.TileMode.REPEAT";
        canvas.drawText(txt, (getMeasuredWidth() - textPaint.measureText(txt)) / 2, rectH / 2, textPaint);

        canvas.translate(0, gap + rectH);
        fillPaint.setShader(mirrorRadialGradient);
        canvas.drawCircle(width / 2, rectH / 2, rectH / 2, fillPaint);
        txt = "Shader.TileMode.MIRROR";
        canvas.drawText(txt, (getMeasuredWidth() - textPaint.measureText(txt)) / 2, rectH / 2, textPaint);

        canvas.restore();

    }


}
