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
 * @date 2020/3/31 11:41
 */
public class DrawApiBaseView extends View {
    Paint paint;

    public DrawApiBaseView(Context context) {
        this(context, null);
    }

    public DrawApiBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawApiBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔颜色
        paint.setColor(Color.RED);
        //设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        paint.setStyle(Paint.Style.FILL);
        //设置画笔宽度
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.argb(128, 0, 0, 0));


//—————————— 以下为示例  开发时不要在 onDraw中创建对象 ——————————


        //画线
        canvas.drawLine(0, 0, 100, 100, paint);
        canvas.drawLines(new float[]{50, 50, 100, 50, 100, 50, 200, 50}, paint);

        //画点
        canvas.drawPoint(200, 200, paint);
        canvas.drawPoints(new float[]{240, 200, 260, 200, 280, 200, 300, 200}, paint);

        //画圆
        canvas.drawCircle(350, 200, 40, paint);


        //使用RectF构造矩形
        RectF rect = new RectF(10, 350, 110, 450);
        canvas.drawRect(rect, paint);

        //使用Rect构造矩形
        Rect rect2 = new Rect(210, 350, 310, 450);
        canvas.drawRect(rect2, paint);

        //使用Rect构造圆角矩形
        RectF rect3 = new RectF(410, 350, 510, 450);
        canvas.drawRoundRect(rect3, 20, 10, paint);

        //圆弧
        RectF rect4 = new RectF(10, 500, 110, 600);
        canvas.drawArc(rect4, 0, 90, true, paint);
        RectF rect5 = new RectF(210, 500, 310, 600);
        canvas.drawArc(rect5, 0, 90, false, paint);

        //椭圆
        RectF rect6 = new RectF(410, 500, 510, 600);
        canvas.drawOval(rect6, paint);


    }

}
