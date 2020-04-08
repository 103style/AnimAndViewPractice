package com.lxk.animandcustomviewdemo.drawapi.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/8 14:40
 */
public class XfermodeSrcInDemoView extends View {
    int r, y, dx;
    private Paint fillPaint;
    private Path mPath = new Path();
    private int width, height;
    private ValueAnimator valueAnimator;
    //波的振幅
    private int amplitude = 32;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public XfermodeSrcInDemoView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        fillPaint.setTextSize(Utils.doToPx(context, 40));
        amplitude = Utils.doToPx(context, amplitude);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        r = width / 2;
        y = height / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————

//--------------------------------------------------------------------------------------------------
        //文字和水波纹效果

        //获取水波纹path
        update(y);

        String txt = "公众号 103Tech";
        fillPaint.setColor(Color.WHITE);
        //保持文字可见
        canvas.drawText(txt, (width - fillPaint.measureText(txt)) / 2, y, fillPaint);


        //SRC_IN 保留 水波纹上  和 文字 相交 的地方
        int layerId = canvas.saveLayer(0, 0, width, height, fillPaint);

        canvas.drawText(txt, (width - fillPaint.measureText(txt)) / 2, y, fillPaint);

        //参考 DrawXfermodeDemoView 中的 18 种模式
        fillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        fillPaint.setColor(Color.GREEN);
        canvas.drawPath(mPath, fillPaint);

        fillPaint.setXfermode(null);
        canvas.restoreToCount(layerId);

//--------------------------------------------------------------------------------------------------
        //圆形 和 水波纹效果

        //获取水波纹path
        update(y * 2);

        fillPaint.setColor(Color.WHITE);
        canvas.drawCircle(width / 2, y * 2, height / 5, fillPaint);

        //SRC_IN 保留 水波纹上  和 文字 相交 的地方
        layerId = canvas.saveLayer(0, 0, width, height, fillPaint);

        canvas.drawCircle(width / 2, y * 2, height / 5, fillPaint);
        //参考 DrawXfermodeDemoView 中的 18 种模式
        fillPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        fillPaint.setColor(Color.GREEN);
        canvas.drawPath(mPath, fillPaint);

        fillPaint.setXfermode(null);
        canvas.restoreToCount(layerId);

        startAnim();
    }


    private void update(int y) {
        //重置path
        mPath.reset();

        //左右偏移的距离
        int gap = 2 * r;
        //移动到坐标的偏移点
        mPath.moveTo(-gap + dx, y);
        for (int i = -gap; i <= getWidth() + gap; i += gap) {
            //在 gap 的前一个 r距离 中间上方添加一个控制点
            mPath.rQuadTo(r / 2, -amplitude, r, 0);
            //在 gap 的后一个 r距离 中间下方添加一个控制点
            mPath.rQuadTo(r / 2, amplitude, r, 0);
        }
        //组成闭合区间
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
    }


    private void startAnim() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(0, 2 * r);
            valueAnimator.setDuration(2000);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    dx = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
        }
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    private void stopAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
        valueAnimator = null;
    }
}
