package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author https://github.com/103style
 * @date 2020/4/6 15:43
 */
public class DrawTextDemoView extends View {
    private Paint textPaint;
    private int width, height;

    public DrawTextDemoView(Context context) {
        this(context, null);
    }

    public DrawTextDemoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawTextDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔颜色
        textPaint.setColor(Color.RED);
        //设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        textPaint.setStyle(Paint.Style.FILL);
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

        textPaint.setTextSize(64);

        int baseX = 0, baseY = gapH * 5;
        String txt = "What a funny demo!";
        float txtWidth = textPaint.measureText(txt);
        //居中绘制
        canvas.drawText(txt, (width - txtWidth) / 2, baseY, textPaint);
        //基准线
        textPaint.setColor(Color.WHITE);
        canvas.drawLine(baseX, baseY, width, baseY, textPaint);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textPaint.setColor(Color.RED);
        //可绘制的最上线
        float top = baseY + fontMetrics.top;
        canvas.drawLine(baseX, top, width, top, textPaint);
        textPaint.setColor(Color.YELLOW);
        //可绘制的最下线
        float bottom = baseY + fontMetrics.bottom;
        canvas.drawLine(baseX, bottom, width, bottom, textPaint);
        textPaint.setColor(Color.BLUE);
        //系统建议的最上线
        float ascent = baseY + fontMetrics.ascent;
        canvas.drawLine(baseX, ascent, width, ascent, textPaint);
        textPaint.setColor(Color.GREEN);
        //系统建议的最下线
        float descent = baseY + fontMetrics.descent;
        canvas.drawLine(baseX, descent, width / 2, descent, textPaint);
        //中心线
        textPaint.setColor(Color.DKGRAY);
        float centerY = top + (bottom - top) / 2;
        canvas.drawLine(baseX, centerY, width, centerY, textPaint);

        textPaint.setTextSize(32);
        textPaint.setColor(Color.WHITE);
        fontMetrics = textPaint.getFontMetrics();
        canvas.drawText("fontMetrics.top = " + fontMetrics.top, baseX, gapH, textPaint);
        canvas.drawText("fontMetrics.ascent = " + fontMetrics.ascent, baseX, gapH * 3 / 2, textPaint);
        canvas.drawText("fontMetrics.descent = " + fontMetrics.descent, baseX, gapH * 2, textPaint);
        canvas.drawText("fontMetrics.bottom = " + fontMetrics.bottom, baseX, gapH * 5 / 2, textPaint);
        float dy = Math.abs(fontMetrics.top + fontMetrics.bottom) / 2;
        String topS = "top";
        canvas.drawText(topS, baseX, top - dy, textPaint);
        String bottomS = "bottom";
        canvas.drawText(bottomS, baseX, bottom + dy, textPaint);
        String ascentS = "ascent";
        canvas.drawText(ascentS, width - textPaint.measureText(ascentS), ascent + dy, textPaint);
        String descentS = "descent";
        canvas.drawText(descentS, width - textPaint.measureText(descentS), descent + dy, textPaint);

        String centerS = "center";
        canvas.drawText(centerS, gapW, centerY, textPaint);

    }

}
