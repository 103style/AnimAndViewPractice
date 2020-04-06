package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * @author https://github.com/103style
 * @date 2020/4/6 16:51
 */
public class DrawBezierWaterView extends View {
    int x, y;
    private Paint fillPaint;
    private Path mPath = new Path();
    private int width, height;

    public DrawBezierWaterView(Context context) {
        super(context);
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔颜色
        fillPaint.setColor(Color.RED);
        //设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setStrokeWidth(6);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        x = 0;
        y = w / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));
        int r = width / 4;
        int halfWaveLen = r / 2;
        int dy = 50;
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        mPath.moveTo(-r, y);
        for (int i = -r; i <= getWidth() + r; i += r) {
            mPath.rQuadTo(halfWaveLen / 2, -dy, halfWaveLen, 0);
            mPath.rQuadTo(halfWaveLen / 2, dy, halfWaveLen, 0);
        }
//        mPath.lineTo(width, height);
//        mPath.lineTo(0, height);
//        mPath.close();
        canvas.drawPath(mPath, fillPaint);
//        y += 10;
//        if (y < height) {
//            mPath.reset();
//            invalidate();
//        }
    }

}
