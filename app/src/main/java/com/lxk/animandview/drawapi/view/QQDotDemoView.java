package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lxk.animandview.utils.DensityUtils;

/**
 * @author https://github.com/103style
 * @date 2020/4/8 15:30
 * base qq Android 8.3.0.4480
 */
public class QQDotDemoView extends View {
    private static final String TAG = "QQDotDemoView";
    private final float hidePathScale = 0.15f;
    private Paint fillPaint;
    private Point circleCenterPoint;
    private int gapW;
    private float curX, curY;
    private Path mPath;
    private Region dotRegion;
    private int radius, changedR;
    private boolean touched;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public QQDotDemoView(Context context) {
        super(context);
        fillPaint = DensityUtils.initPaint(context, Paint.Style.FILL);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        gapW = w / 8;
        circleCenterPoint = new Point(gapW, gapW);
        radius = gapW / 2;
        changedR = gapW / 2;
        //构建接受的点击区域
        dotRegion = new Region(gapW / 2, gapW / 2, radius * 2 + gapW / 2, radius * 2 + gapW / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //触摸到圆所在的位置
                if (dotRegion.contains((int) x, (int) y)) {
                    touched = true;
                } else {
                    ViewGroup group = (ViewGroup) getParent();
                    group.removeView(this);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (touched) {
                    //起始圆的半径随拖动距离越大 越来越小
                    double dis = Math.sqrt(Math.pow(x - circleCenterPoint.x, 2) + Math.pow(y - circleCenterPoint.y, 2));
                    //计算放缩比例
                    double scale = radius * 2 / dis;
                    if (scale < hidePathScale) {
                        changedR = 0;
                    } else {
                        changedR = (int) (radius * (scale > 1 ? 1 : scale));
                    }
                    curX = x;
                    curY = y;
                    updatePath(x, y);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                //恢复到初始状态
                changedR = radius;
                touched = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updatePath(float x, float y) {
        //计算 两圆圆心连线与 x轴 构成的锐角
        double degree = Math.atan(Math.abs(y - circleCenterPoint.y) / Math.abs(x - circleCenterPoint.x));
        Log.e(TAG, "degree = " + degree);
        //原圆边上两点 x, y 的距离
        float rDx = (float) (Math.sin(degree) * changedR);
        float rDy = (float) (Math.cos(degree) * changedR);
        //手指所在圆边上两点 x, y 的距离
        float curDx = (float) (Math.sin(degree) * (radius - changedR));
        float curDy = (float) (Math.cos(degree) * (radius - changedR));

        mPath.reset();
        //原圆连线的右上点
        mPath.moveTo(circleCenterPoint.x + rDx, circleCenterPoint.y - rDy);
        //原圆连线的左下点
        mPath.lineTo(circleCenterPoint.x - rDx, circleCenterPoint.y + rDy);
        //协助点为两圆圆心连线的中点 终点为手指所在圆连线的左下点
        mPath.quadTo((circleCenterPoint.x + x) / 2, (circleCenterPoint.y + y) / 2, x - curDx, y + curDy);
        //手指所在圆连线的右上点
        mPath.lineTo(x + curDx, y - curDy);
        //协助点为两圆圆心连线的中点   终点为原圆连线的右上点
        mPath.quadTo((circleCenterPoint.x + x) / 2, (circleCenterPoint.y + y) / 2,
                circleCenterPoint.x + rDx, circleCenterPoint.y - rDy);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0));

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————

        //绘制原圆
        canvas.drawCircle(circleCenterPoint.x, circleCenterPoint.x, changedR, fillPaint);
        if (touched) {
            if (changedR != 0) {
                //绘制两圆的连接区域
                canvas.drawPath(mPath, fillPaint);
            }
            //绘制手指触摸的圆
            canvas.drawCircle(curX, curY, (radius - changedR), fillPaint);
        }

    }


}
