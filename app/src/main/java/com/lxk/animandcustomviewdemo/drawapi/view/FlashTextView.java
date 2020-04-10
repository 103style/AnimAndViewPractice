package com.lxk.animandcustomviewdemo.drawapi.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/10 15:41
 */
public class FlashTextView extends View {
    private Paint textPaint;
    private LinearGradient clamp;
    private Matrix matrix;
    private int[] color = new int[]{Color.WHITE, Color.GREEN, Color.BLUE, Color.YELLOW};
    private float[] pos = new float[]{0, 0.34f, 0.67f, 1.0f};
    private ValueAnimator animator;
    private float dx;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public FlashTextView(Context context) {
        super(context);
        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(Utils.doToPx(context, 40));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));
        String txt = "103style---103Tech";
        float txtW = textPaint.measureText(txt);
        if (clamp == null) {
            clamp = new LinearGradient(0, 0, txtW, 0, color, pos, Shader.TileMode.CLAMP);
            matrix = new Matrix();
        }
        matrix.setTranslate(dx, 0);
        clamp.setLocalMatrix(matrix);
        textPaint.setShader(clamp);
        canvas.drawText(txt, (getMeasuredWidth() - txtW) / 2, getMeasuredHeight() / 2, textPaint);

        startAnim(txtW);

    }

    private void startAnim(float width) {
        if (animator == null) {
            animator = ValueAnimator.ofFloat(-width / 2, width / 2);
            animator.setDuration(3000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    dx = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
        }
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
    }
}
