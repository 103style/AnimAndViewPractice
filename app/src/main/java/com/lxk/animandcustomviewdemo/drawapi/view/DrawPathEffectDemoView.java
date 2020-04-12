package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.SumPathEffect;
import android.view.View;

import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/7 14:04
 */
public class DrawPathEffectDemoView extends View {
    private Paint strokePaint, textPaint;
    private Path path;
    private int width, height;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public DrawPathEffectDemoView(Context context) {
        super(context);
        strokePaint = Utils.initPaint(context, Paint.Style.STROKE);
        strokePaint.setStrokeWidth(Utils.doToPx(context, 2));
        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setStrokeWidth(1);
        textPaint.setTextSize(Utils.doToPx(context, 12));
        textPaint.setColor(Color.WHITE);
        path = new Path();
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
        path.moveTo(gapW, gapH);
        path.lineTo(gapW * 4, gapH / 2);
        path.lineTo(gapW * 7, gapH);
        canvas.drawText("CornerPathEffect：圆角效果", gapW, gapH / 2, textPaint);
        canvas.drawPath(path, strokePaint);

        // CornerPathEffect 在转角处 转化为配置半径对应的圆角
        strokePaint.setColor(Color.BLUE);
        strokePaint.setPathEffect(new CornerPathEffect(Utils.doToPx(getContext(), 32)));
        canvas.drawPath(path, strokePaint);
        strokePaint.setColor(Color.YELLOW);
        strokePaint.setPathEffect(new CornerPathEffect(Utils.doToPx(getContext(), 64)));
        canvas.drawPath(path, strokePaint);


        //DashPathEffect  虚线效果
        //float intervals[] : 实线虚线依次的长度(数量为2的倍数 一实一虚)   长度要>=2
        //float phase : 开始绘制的偏移值
        strokePaint.setPathEffect(new DashPathEffect(new float[]{50, 10, 30, 20}, 10));
        strokePaint.setColor(Color.CYAN);
        path = getPath(gapH * 3 / 2, gapW, gapH);
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("DashPathEffect：虚线效果", path, 0, 0, textPaint);


        //DiscretePathEffect 离散路径
        // float segmentLength : 切割成多长的线段
        // float deviation : 每条线段的偏移值
        strokePaint.setPathEffect(new DiscretePathEffect(20, 10));
        strokePaint.setColor(Color.RED);
        path = getPath(gapH * 3, gapW, gapH);
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("DiscretePathEffect：离散路径效果", path, 0, 0, textPaint);


        //PathDashPathEffect——印章路径效果
        // Path shape ： 印章
        // float advance ： 两个印章之间的距离
        // float phase ： 印章的偏移
        // Style style ：印章转角处的样式
        canvas.drawText("PathDashPathEffect：印章路径效果", gapW, gapH * 9 / 2, textPaint);
        //构建印章路径
        Path shape = new Path();
        shape.moveTo(0, 20);
        shape.lineTo(10, 0);
        shape.lineTo(20, 20);
        shape.close();
        shape.addCircle(0, 0, 3, Path.Direction.CCW);

        //Style.TRANSLATE
        strokePaint.setPathEffect(new PathDashPathEffect(shape, 35, 0, PathDashPathEffect.Style.TRANSLATE));
        strokePaint.setColor(Color.RED);
        path = getPath(gapH * 9 / 2, gapW, gapH);
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("Style.TRANSLATE", path, 0, 0, textPaint);

        //保存平移前的状态
        canvas.save();
        int gap = gapH / 2;
        //图层向下平移gap距离
        canvas.translate(0, gap);
        //Style.ROTATE
        strokePaint.setPathEffect(new PathDashPathEffect(shape, 35, 0, PathDashPathEffect.Style.ROTATE));
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("Style.ROTATE", path, 0, 0, textPaint);

        //图层向下平移gap距离
        canvas.translate(0, gap);
        //Style.MORPH
        strokePaint.setPathEffect(new PathDashPathEffect(shape, 35, 0, PathDashPathEffect.Style.MORPH));
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("Style.MORPH", path, 0, 0, textPaint);
        //恢复到平移前的状态
        canvas.restore();


        path = getPath(gapH * 7, gapW, gapH);

        //仅应用圆角特效的路径
        CornerPathEffect cornerPathEffect = new CornerPathEffect(100);
        strokePaint.setPathEffect(cornerPathEffect);
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("CornerPathEffect", path, 0, 0, textPaint);

        //仅应用虚线特效的路径
        canvas.translate(0, gap);
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{2, 5, 10, 10}, 0);
        strokePaint.setPathEffect(dashPathEffect);
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("DashPathEffect", path, 0, 0, textPaint);

        //ComposePathEffect 合并两个特效，有先后顺序的   交集
        //利用ComposePathEffect先应用圆角特效,再应用虚线特效
        canvas.translate(0, gap);
        ComposePathEffect composePathEffect = new ComposePathEffect(dashPathEffect, cornerPathEffect);
        strokePaint.setPathEffect(composePathEffect);
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("ComposePathEffect 交集", path, 0, 0, textPaint);

        // SumPathEffect 是分别对原始路径分别作用第一个特效和第二个特效，然后合并改变之后的路径   并集
        //利用SumPathEffect,分别将圆角特效应用于原始路径,然后将生成的两条特效路径合并
        canvas.translate(0, gap);
        SumPathEffect sumPathEffect = new SumPathEffect(cornerPathEffect, dashPathEffect);
        strokePaint.setPathEffect(sumPathEffect);
        canvas.drawPath(path, strokePaint);
        canvas.drawTextOnPath("SumPathEffect 并集", path, 0, 0, textPaint);
    }


    private Path getPath(int startY, int gapW, int gapH) {
        Path path = new Path();
        path.moveTo(0, startY);
        path.lineTo(gapW, startY + gapH);
        path.lineTo(gapW * 3, startY);
        path.lineTo(gapW * 5, startY + gapH);
        path.lineTo(gapW * 7, startY);
        path.lineTo(gapW * 9, startY + gapH);
        path.lineTo(width, startY);
        return path;
    }
}
