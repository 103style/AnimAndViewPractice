package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;

import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/8 15:30
 * base qq Android 8.3.0.4480
 */
public class QQDotDemoView extends View {
    private static final String TAG = "QQDotDemoView";
    private Paint fillPaint;
    private int gapW;
    private float afterX, afterY;
    private Path mPath;
    private Region dotRegion;
    private float radius;
    private boolean touched;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public QQDotDemoView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        gapW = w / 8;
        dotRegion = new Region(gapW / 2, gapW / 2, gapW + gapW / 2, gapW + gapW / 2);
        radius = gapW / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (dotRegion.contains((int) x, (int) y)) {
                    touched = true;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (touched) {
                    updatePath(x, y);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updatePath(float x, float y) {
        afterX = x;
        afterY = y;
        double degree = Math.atan(Math.abs(y - gapW) / Math.abs(x - gapW));
        float dx = (float) (Math.sin(degree) * gapW / 2);
        float dy = (float) (Math.cos(degree) * gapW / 2);


        mPath.reset();
        mPath.moveTo(gapW + dx, gapW - dy);
        mPath.lineTo(gapW - dx, gapW + dy);
        mPath.lineTo(x - dx, y + dy);
        mPath.lineTo(x + dx, y - dy);
        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————

        canvas.drawCircle(gapW, gapW, radius, fillPaint);
        if (touched) {
            canvas.drawPath(mPath, fillPaint);
            canvas.drawCircle(afterX, afterY, gapW / 2, fillPaint);
        }

    }


}
