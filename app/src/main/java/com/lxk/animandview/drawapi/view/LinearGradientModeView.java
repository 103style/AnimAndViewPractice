package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

import com.lxk.animandview.utils.DensityUtils;

/**
 * @author https://github.com/103style
 * @date 2020/4/10 15:31
 */
public class LinearGradientModeView extends View {
    private Paint fillPaint, textPaint;
    private LinearGradient clamp, repeat, mirror;
    private int width, height;

    private int gap, end, rectH, top;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public LinearGradientModeView(Context context) {
        super(context);
        fillPaint = DensityUtils.initPaint(context, Paint.Style.FILL);
        textPaint = DensityUtils.initPaint(context, Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(DensityUtils.dpToPx(context, 14));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        gap = width / 10;
        top = gap;
        rectH = height / 4;
        end = width - gap * 2;
        int[] color = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
        clamp = new LinearGradient(0, 0, gap, gap, color, null, Shader.TileMode.CLAMP);
        repeat = new LinearGradient(0, 0, gap, gap, color, null, Shader.TileMode.REPEAT);
        mirror = new LinearGradient(0, 0, gap, gap, color, null, Shader.TileMode.MIRROR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        // Shader 是以canvas的左上角为起始点开始覆盖的
        // 无论你利用绘图函数绘多大一块，在哪绘制，与Shader无关。
        // 因为Shader总是在控件的左上角开始，而你绘制的部分只是显示出来的部分而已。
        // 没有绘制的部分虽然已经生成，但只是不会显示出来罢了。

        //x y 轴 在原图的基础上边界拉伸
        drawTest(canvas, clamp, "Shader.TileMode.CLAMP");

        top = top + rectH + gap;
        //x y 轴平移
        drawTest(canvas, repeat, "Shader.TileMode.REPEAT");

        top = top + rectH + gap;
        //x y 轴翻转
        drawTest(canvas, mirror, "Shader.TileMode.MIRROR");
    }

    private void drawTest(Canvas canvas, LinearGradient linearGradient, String txt) {
        canvas.save();
        fillPaint.setShader(linearGradient);
        canvas.translate(gap, top);
        canvas.drawRect(0, 0, end, rectH, fillPaint);
        canvas.drawText(txt, 0, 0, textPaint);
        canvas.restore();
    }
}
