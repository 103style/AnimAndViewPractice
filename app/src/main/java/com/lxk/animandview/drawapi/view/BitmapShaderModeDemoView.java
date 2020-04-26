package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

import com.lxk.animandview.R;
import com.lxk.animandview.utils.DensityUtils;

/**
 * @author https://github.com/103style
 * @date 2020/4/8 20:10
 * doc: https://blog.csdn.net/harvic880925/article/details/52039081
 */
public class BitmapShaderModeDemoView extends View {

    private Bitmap bitmap;
    private Paint fillPaint, textPaint;
    private BitmapShader clamp, repeat, mirror;
    private int width, height;

    private int gap, end, rectH, top;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public BitmapShaderModeDemoView(Context context) {
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

        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.android11);
        Matrix matrix = new Matrix();
        float scale = rectH / 3 * 1.0f / temp.getWidth();
        matrix.setScale(scale, scale);
        bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
        Canvas canvas = new Canvas(bitmap);
        fillPaint.setColor(Color.RED);
        canvas.drawLine(0, 0, 0, bitmap.getHeight(), fillPaint);
        fillPaint.setColor(Color.GREEN);
        canvas.drawLine(0, 0, bitmap.getWidth(), 0, fillPaint);
        fillPaint.setColor(Color.YELLOW);
        canvas.drawLine(0, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight(), fillPaint);

        clamp = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        repeat = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mirror = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        // BitmapShader 是以canvas的左上角为起始点开始覆盖的
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

    private void drawTest(Canvas canvas, BitmapShader bitmapShader, String txt) {
        canvas.save();
        fillPaint.setShader(bitmapShader);
        canvas.translate(gap, top);
        canvas.drawRect(0, 0, end, rectH, fillPaint);
        canvas.drawText(txt, 0, 0, textPaint);
        canvas.restore();
    }
}
