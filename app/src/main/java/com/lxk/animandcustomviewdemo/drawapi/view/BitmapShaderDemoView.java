package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/9 19:20
 */
public class BitmapShaderDemoView extends View {

    private Bitmap bitmap;
    private Paint fillPaint;
    private BitmapShader clamp;
    private int width, height;

    private int x, y, radius;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public BitmapShaderDemoView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        radius = Utils.doToPx(context, 96);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.mm);
        Matrix matrix = new Matrix();
        //扩大到铺满屏幕
        float scaleX = width * 1.0f / temp.getWidth();
        matrix.setScale(scaleX, scaleX);
        bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
        clamp = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        fillPaint.setShader(clamp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x = y = 0;
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));


        if (x != 0 && y != 0) {
            canvas.drawCircle(x, y, radius, fillPaint);
        }
    }

}
