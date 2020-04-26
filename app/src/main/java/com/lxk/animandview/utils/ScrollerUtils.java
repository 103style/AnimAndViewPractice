package com.lxk.animandview.utils;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.ViewConfiguration;

/**
 * @author https://github.com/103style
 * @date 2020/4/26 11:07
 * 参考 OverScroller 获取速度对应的滑动距离
 */
public class ScrollerUtils {
    public static int getSplineFlingDistance(Context context, float velocity) {
        return (int) SplineOverScroller.getInstance(context).getSplineFlingDistance(velocity);
    }


    static class SplineOverScroller {
        // Constant gravity value, used in the deceleration phase.
        private static final float GRAVITY = 2000.0f;
        private static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
        private volatile static SplineOverScroller instance;
        private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));
        // Fling friction
        private float mFlingFriction = ViewConfiguration.getScrollFriction();
        // A context-specific coefficient adjusted to physical values.
        private float mPhysicalCoeff;


        SplineOverScroller(Context context) {
            final float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
            mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
                    * 39.37f // inch/meter
                    * ppi
                    * 0.84f; // look and feel tuning
        }

        public static SplineOverScroller getInstance(Context context) {
            if (instance == null) {
                synchronized (SplineOverScroller.class) {
                    if (instance == null) {
                        instance = new SplineOverScroller(context);
                    }
                }
            }
            return instance;
        }

        /*
         * Get a signed deceleration that will reduce the velocity.
         */
        private static float getDeceleration(int velocity) {
            return velocity > 0 ? -GRAVITY : GRAVITY;
        }

        public void setFriction(float friction) {
            mFlingFriction = friction;
        }


        private double getSplineDeceleration(float velocity) {
            return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
        }

        public double getSplineFlingDistance(float velocity) {
            final double l = getSplineDeceleration(velocity);
            final double decelMinusOne = DECELERATION_RATE - 1.0;
            return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
        }

        /* Returns the duration, expressed in milliseconds */
        public int getSplineFlingDuration(int velocity) {
            final double l = getSplineDeceleration(velocity);
            final double decelMinusOne = DECELERATION_RATE - 1.0;
            return (int) (1000.0 * Math.exp(l / decelMinusOne));
        }
    }
}
