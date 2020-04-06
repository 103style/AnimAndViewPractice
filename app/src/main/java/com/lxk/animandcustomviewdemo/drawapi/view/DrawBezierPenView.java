package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author https://github.com/103style
 * @date 2020/4/6 16:28
 */
public class DrawBezierPenView extends View {
    private Paint strokePaint;
    private Path mPath = new Path();
    private float mPreX, mPreY;

    public DrawBezierPenView(Context context) {
        this(context, null);
    }

    public DrawBezierPenView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawBezierPenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔颜色
        strokePaint.setColor(Color.RED);
        //设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setTextSize(64);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPath.moveTo(event.getX(), event.getY());
                mPreX = event.getX();
                mPreY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_MOVE:
                float endX = (mPreX + event.getX()) / 2;
                float endY = (mPreY + event.getY()) / 2;
                mPath.quadTo(mPreX, mPreY, endX, endY);
                mPreX = event.getX();
                mPreY = event.getY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getY() < 200) {
                    if (event.getX() > getMeasuredWidth() / 2) {
                        setVisibility(GONE);
                    }
                    mPath.reset();
                    postInvalidate();
                }
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
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————

        strokePaint.setColor(Color.WHITE);
        String reset = "reset";
        canvas.drawText(reset, strokePaint.measureText(reset) / 2, 100, strokePaint);
        String hide = "hide";
        canvas.drawText(hide, getMeasuredWidth() / 2 + strokePaint.measureText(hide) / 2, 100, strokePaint);
        strokePaint.setColor(Color.RED);
        canvas.drawPath(mPath, strokePaint);
    }

}
