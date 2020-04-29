package com.lxk.animandview.practice.qqzone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.lxk.animandview.utils.DensityUtils;

/**
 * @author https://github.com/103style
 * @date 2020/4/29 15:35
 * 参考doc: https://blog.csdn.net/u011387817/article/details/84136291
 * <p>
 * 方qq空间头部广告拖动动画
 */
public class RandomDragLayout extends FrameLayout {
    /**
     * 最短滑动距离
     */
    private int touchSlop;

    private Paint paint;

    private MotionEvent event;

    public RandomDragLayout(Context context) {
        this(context, null);
    }

    public RandomDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //回调ondraw方法
        setWillNotDraw(false);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtils.dpToPx(context, 1));
        paint.setTextSize(DensityUtils.dpToPx(context, 14));
        paint.setColor(Color.RED);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                this.event = event;
                break;
            default:
                this.event = null;
                break;
        }
        invalidate();
        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.argb(128, 0, 0, 0));
        //画坐标轴
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paint);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);

        if (this.event != null) {
            canvas.drawLine(getWidth() / 2, getHeight() / 2, event.getX(), event.getY(), paint);
            float angel = getRotationAngle(getWidth() / 2, getHeight() / 2, event.getX(), event.getY());
            canvas.drawText("顺时针角度 = " + angel, getWidth() / 8, getWidth() / 8, paint);
        }
    }

    /**
     * 获取顺势正的旋转角度
     */
    private float getRotationAngle(float startX, float startY, float endX, float endY) {
        //获取所在象限需要增加的角度
        int appendAngle = calculateAppendAngle(startX, startY, endX, endY);
        //
        //  P (sX,eY)___ E(eX,eY)           S(sX,sY)                 E(eX,eY)           S(sX,sY)____P (eX,sY)
        //          |  /                       /|                        |\                     \  |
        //   象限1  | /               象限3   / |                象限2   | \              象限4   \ |
        //    270  |/                  90   /__|                  180   |__\               0     \|
        //        S(sX,sY)          E(eX,eY)    P (sX,eY)      P (eX,sY)  S(sX,sY)             E(eX,eY)
        //
        //  所以我们需要的锐角就是  角 PSE
        //              __________
        //根据公式 SE = √ PS² + PE² 计算出对角线的长度
        float PS, PE, SE;
        if (appendAngle == 90 || appendAngle == 270) {
            //第一和第三象限
            PS = Math.abs(startY - endY);
            PE = Math.abs(endX - startX);
            SE = (float) Math.sqrt(PS * PS + PE * PE);
        } else {
            //第二和第四象限
            PE = Math.abs(startY - endY);
            PS = Math.abs(endX - startX);
            SE = (float) Math.sqrt(PS * PS + PE * PE);
        }
        double cosPSE = PS / SE;
        float angele = (float) Math.toDegrees(Math.acos(cosPSE));
        return angele + appendAngle;
    }


    /**
     * 获取线方向所在象限 应该增加的顺势正角度
     * 1 ： 270
     * 2 ： 180
     * 3 ： 90
     * 4 ： 0
     */
    private int calculateAppendAngle(float startX, float startY, float endX, float endY) {
        if (endX > startX) {
            // 1、4 象限  endY > startY 说明在下方
            return endY > startY ? 0 : 270;
        } else {
            // 2、3 象限   endY > startY 说明在下方
            return endY > startY ? 90 : 180;
        }
    }
}
