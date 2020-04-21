package com.lxk.animandview.drawapi.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.lxk.animandview.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/6 16:51
 */
public class DrawBezierWaterView extends View {
    int r, y, dx;
    private Paint fillPaint;
    private Path mPath = new Path();
    private int width, height;
    private ValueAnimator valueAnimator;
    //波的振幅
    private int amplitude = 32;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public DrawBezierWaterView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        //设置画笔颜色
        fillPaint.setColor(Color.LTGRAY);
        amplitude = Utils.dpToPx(context, amplitude);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        r = w / 2;
        y = w / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
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
        //绘制路径
        canvas.drawPath(mPath, fillPaint);
        //做Y轴的变化
        y += 2;
        if (y > height + amplitude) {
            //结束动画并移除当前视图
            stopAnim();
            ViewGroup group = (ViewGroup) getParent();
            group.removeView(this);
        } else {
            //开始动画
            startAnim();
        }
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
