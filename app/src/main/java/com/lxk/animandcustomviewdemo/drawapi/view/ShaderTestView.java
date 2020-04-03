package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author https://github.com/103style
 * @date 2020/3/31 11:36
 */
public class ShaderTestView extends View {
    Paint paint;

    public ShaderTestView(Context context) {
        this(context, null);
    }

    public ShaderTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);  //设置画笔颜色
        paint.setStyle(Paint.Style.FILL);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        paint.setStrokeWidth(5);//设置画笔宽度
        paint.setShadowLayer(100, 100, 100, Color.GREEN);//设置阴影
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(190, 200, 150, paint);

        canvas.drawLine(400, 100, 200, 200, paint);

        canvas.drawLines(new float[]{200, 300, 500, 300, 600, 500, 200, 500}, paint);


        canvas.drawPoint(300, 300, paint);


        RectF rect = new RectF(120, 10, 210, 100);
        canvas.drawRect(rect, paint);//使用RectF构造


        Rect rect2 =  new Rect(230, 10, 320, 100);
        canvas.drawRect(rect2, paint);//使用Rect构造
    }

}
