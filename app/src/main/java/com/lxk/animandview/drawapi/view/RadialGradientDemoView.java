package com.lxk.animandview.drawapi.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lxk.animandview.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/10 16:59
 */
public class RadialGradientDemoView extends View {
    private int DEFAULT_RADIUS = 0;
    private Paint fillPaint, textPaint;
    private RadialGradient clampRadialGradient;
    private int width, height, rectH, radius, x, y;
    private ValueAnimator valueAnimator;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public RadialGradientDemoView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(Utils.dpToPx(context, 20));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        rectH = h / 4;
        DEFAULT_RADIUS = width / 8;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = (int) event.getX();
        y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                radius = DEFAULT_RADIUS;
                cancelAnim();
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                startAnim();
                break;
            default:
                break;

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));
        if (radius > 0) {
            clampRadialGradient = new RadialGradient(x, y, radius,
                    0x00FFFFFF, 0xFF58FAAC, Shader.TileMode.CLAMP);
            fillPaint.setShader(clampRadialGradient);
            canvas.drawCircle(x, y, radius, fillPaint);
        }
    }

    private void startAnim() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(DEFAULT_RADIUS, height);
            valueAnimator.setDuration(1000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    radius = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    radius = 0;
                    postInvalidate();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnim();
    }

    void cancelAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }
}
