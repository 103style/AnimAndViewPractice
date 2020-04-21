package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.view.View;

/**
 * @author https://github.com/103style
 * @date 2020/4/5 16:47
 */
public class RangeDemo extends View {
    private Paint fillPaint, strokePaint;
    private int width, height;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public RangeDemo(Context context) {
        super(context);
        fillPaint = initPaint(Paint.Style.FILL);
        float scale = getResources().getDisplayMetrics().density;
        int size = (int) (14 * scale + 0.5f);
        fillPaint.setTextSize(size);
        strokePaint = initPaint(Paint.Style.STROKE);
    }

    private Paint initPaint(Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔颜色
        paint.setColor(Color.RED);
        //设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        paint.setStyle(style);
        //设置画笔宽度
        paint.setStrokeWidth(6);
        return paint;
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

        Region region = new Region(gapW, gapH, gapW * 2, gapH * 2);
        drawRegion(canvas, region, fillPaint);
        //绘制一个新的区域
        region.set(gapW * 3, gapH, gapW * 4, gapH * 2);
        drawRegion(canvas, region, strokePaint);


        //构造一个椭圆路径
        Path ovalPath = new Path();
        RectF rect = new RectF(gapW * 5, gapH, gapW * 8, gapH * 2);
        ovalPath.addOval(rect, Path.Direction.CCW);
        //SetPath时,传入一个比椭圆区域小的矩形区域,让其取交集
        Region rgn = new Region();
        rgn.setPath(ovalPath, new Region(gapW * 5, gapH, gapW * 7, gapH * 2));
        //画出路径
        drawRegion(canvas, rgn, fillPaint);


        //region 的合并交叉等操作
        Region thirdOneHor = new Region(gapW / 2, gapH * 3, gapW * 3, gapH * 7 / 2);
        Region thirdOneVer = new Region(gapW * 3 / 2, gapH * 5 / 2, gapW * 2, gapH * 4);
        regionOp(canvas, thirdOneHor, thirdOneVer, Region.Op.INTERSECT);
        canvas.drawText("OP.INTERSECT", gapW / 2, gapH * 9 / 2, fillPaint);

        Region thirdTwoHor = new Region(gapW * 7 / 2, gapH * 3, gapW * 6, gapH * 7 / 2);
        Region thirdTwoVer = new Region(gapW * 9 / 2, gapH * 5 / 2, gapW * 5, gapH * 4);
        regionOp(canvas, thirdTwoHor, thirdTwoVer, Region.Op.DIFFERENCE);
        canvas.drawText("OP.DIFFERENCE", gapW * 7 / 2, gapH * 9 / 2, fillPaint);

        Region thirdThreeHor = new Region(gapW * 13 / 2, gapH * 3, gapW * 9, gapH * 7 / 2);
        Region thirdThreeVer = new Region(gapW * 15 / 2, gapH * 5 / 2, gapW * 8, gapH * 4);
        regionOp(canvas, thirdThreeHor, thirdThreeVer, Region.Op.UNION);
        canvas.drawText("OP.UNION", gapW * 13 / 2, gapH * 9 / 2, fillPaint);


        //region 的合并交叉等操作
        Region forthOneHor = new Region(gapW / 2, gapH * 6, gapW * 3, gapH * 13 / 2);
        Region forthOneVer = new Region(gapW * 3 / 2, gapH * 11 / 2, gapW * 2, gapH * 7);
        regionOp(canvas, forthOneHor, forthOneVer, Region.Op.XOR);
        canvas.drawText("OP.XOR", gapW / 2, gapH * 15 / 2, fillPaint);

        Region forthTwoHor = new Region(gapW * 7 / 2, gapH * 6, gapW * 6, gapH * 13 / 2);
        Region forthTwoVer = new Region(gapW * 9 / 2, gapH * 11 / 2, gapW * 5, gapH * 7);
        regionOp(canvas, forthTwoHor, forthTwoVer, Region.Op.REPLACE);
        canvas.drawText("OP.REPLACE", gapW * 7 / 2, gapH * 15 / 2, fillPaint);

        Region forthThreeHor = new Region(gapW * 13 / 2, gapH * 6, gapW * 9, gapH * 13 / 2);
        Region forthThreeVer = new Region(gapW * 15 / 2, gapH * 11 / 2, gapW * 8, gapH * 7);
        regionOp(canvas, forthThreeHor, forthThreeVer, Region.Op.REVERSE_DIFFERENCE);
        canvas.drawText("OP.REVERSE_DIFFERENCE", gapW * 13 / 2, gapH * 15 / 2, fillPaint);

    }

    private void regionOp(Canvas canvas, Region a, Region b, Region.Op op) {
        drawRegion(canvas, a, strokePaint);
        drawRegion(canvas, b, strokePaint);
        a.op(b, op);
        drawRegion(canvas, a, fillPaint);
    }

    private void drawRegion(Canvas canvas, Region region, Paint paint) {
        //矩形集枚举区域
        RegionIterator ri = new RegionIterator(region);
        Rect r = new Rect();
        while (ri.next(r)) {
            canvas.drawRect(r, paint);
        }
    }

}
