package com.lxk.customviewlearndemo.recyclerview;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author xiaoke.luo@tcl.com 2019/11/15 18:00
 */
public class TestRecyclerView extends RecyclerView {
    public TestRecyclerView(Context context) {
        this(context, null);
    }

    public TestRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setChildrenDrawingOrderEnabled(true);
    }

    public TestLayout4Manager getTestLayoutManager() {
        return ((TestLayout4Manager) getLayoutManager());
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        //计算正在显示的所有Item的中间位置
        int center = getTestLayoutManager().getCenterPosition() - getTestLayoutManager().getFirstVisiblePosition();
        int order;

        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        int flingX = (int) (velocityX * 0.40f);
        TestLayout4Manager manger = getTestLayoutManager();
        double distance = getSplineFlingDistance(flingX);
        double newDistance = manger.calculateDistance(velocityX,distance);
        int fixVelocityX = getVelocity(newDistance);
        if (velocityX > 0) {
            flingX = fixVelocityX;
        } else {
            flingX = -fixVelocityX;
        }
        return super.fling(flingX, velocityY);
    }


    /**
     * 根据松手后的滑动速度计算出fling的距离
     */
    private double getSplineFlingDistance(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return mFlingFriction * getPhysicalCoeff() * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }

    /**
     * 根据距离计算出速度
     */
    private int getVelocity(double distance) {
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        double aecel = Math.log(distance / (mFlingFriction * mPhysicalCoeff)) * decelMinusOne / DECELERATION_RATE;
        return Math.abs((int) (Math.exp(aecel) * (mFlingFriction * mPhysicalCoeff) / INFLEXION));
    }

    /**
     * --------------fling辅助类---------------
     */
    private static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
    private float mFlingFriction = ViewConfiguration.getScrollFriction();
    private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
    private float mPhysicalCoeff = 0;

    private double getSplineDeceleration(int velocity) {
        final float ppi = this.getResources().getDisplayMetrics().density * 160.0f;
        float mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * ppi
                * 0.84f; // look and feel tuning


        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    private float getPhysicalCoeff() {
        if (mPhysicalCoeff == 0) {
            final float ppi = this.getResources().getDisplayMetrics().density * 160.0f;
            mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
                    * 39.37f // inch/meter
                    * ppi
                    * 0.84f; // look and feel tuning
        }
        return mPhysicalCoeff;
    }
}
