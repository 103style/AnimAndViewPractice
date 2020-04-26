package com.lxk.animandview.practice.arcview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * @author https://github.com/103style
 * @date 2020/4/26 10:05
 * <p>
 * 实现 Item之间有一定的角度偏移的布局
 * <p>
 * 参考：https://blog.csdn.net/u011387817/article/details/80788704
 */
public class ArcLayoutView extends ViewGroup implements ArcSlidingHelper.OnSlidingListener {
    /**
     * 计算旋转的辅助类
     */
    private ArcSlidingHelper arcSlidingHelper;

    /**
     * 滑动的最短距离
     */
    private int touchSlop;
    /**
     * 记录按下的点坐标  用户处理是否拦截事件
     */
    private float mPreX, mPreY;
    /**
     * 是否拦截事件
     */
    private boolean interceptTouchEvent;

    public ArcLayoutView(Context context) {
        this(context, null);
    }

    public ArcLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.argb(128, 0, 0, 0));
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int angle = 360 / count;
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int layoutHeight = child.getMeasuredHeight() / 2;
            child.layout(centerX, centerY - layoutHeight,
                    centerX + child.getMeasuredWidth(), centerY + layoutHeight);
            //更新旋转的中心点
            child.setPivotX(0);
            child.setPivotY(layoutHeight);
            //配置选装角度
            child.setRotation(angle * i);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (arcSlidingHelper == null) {
            arcSlidingHelper = ArcSlidingHelper.create(this, this);
            arcSlidingHelper.enableInertialSliding(true);
        } else {
            arcSlidingHelper.updatePivotX(w / 2);
            arcSlidingHelper.updatePivotY(h / 2);
        }
    }

    @Override
    public void onSliding(float angele) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setRotation(child.getRotation() + angele);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (arcSlidingHelper != null) {
            arcSlidingHelper.release();
            arcSlidingHelper = null;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE && interceptTouchEvent) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        float x = ev.getX();
        float y = ev.getY();
        //拦截move事件
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //当手指按下时，停止惯性滚动
                arcSlidingHelper.abortAnimation();
                //记录按下的坐标
                mPreX = x;
                mPreY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - mPreX) > touchSlop
                        || Math.abs(y - mPreY) > touchSlop) {
                    interceptTouchEvent = true;
                }
                //更新上一个点的坐标
                arcSlidingHelper.updateMovement(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                interceptTouchEvent = false;
                break;
        }
        return interceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        arcSlidingHelper.handleMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                interceptTouchEvent = false;
                break;
        }
        return true;
    }
}
