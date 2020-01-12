package com.lxk.animandcustomviewdemo.recyclerview

import android.content.Context
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.ViewConfiguration

import androidx.recyclerview.widget.RecyclerView

/**
 * @author https://github.com/103style
 * @date 2020/1/12 17:28
 */
class TestRecyclerView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr) {

    val testLayoutManager: TestLayout4Manager
        get() = layoutManager as TestLayout4Manager

    init {
        isChildrenDrawingOrderEnabled = true
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        //计算正在显示的所有Item的中间位置
        val center = testLayoutManager.centerPosition - testLayoutManager.firstVisiblePosition
        val order: Int

        if (i == center) {
            order = childCount - 1
        } else if (i > center) {
            order = center + childCount - 1 - i
        } else {
            order = i
        }
        return order
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        var flingX = (velocityX * 0.40f).toInt()
        val manger = testLayoutManager
        val distance = getSplineFlingDistance(flingX)
        val newDistance = manger.calculateDistance(velocityX, distance)
        val fixVelocityX = getVelocity(newDistance)
        if (velocityX > 0) {
            flingX = fixVelocityX
        } else {
            flingX = -fixVelocityX
        }
        return super.fling(flingX, velocityY)
    }


    //------------惯性滑动START-----------------------
    private val mFlingFriction = ViewConfiguration.getScrollFriction()
    private var mPhysicalCoeff = 0f

    // g (m/s^2)
    // inch/meter
    // look and feel tuning
    private val physicalCoeff: Float
        get() {
            if (mPhysicalCoeff == 0f) {
                val ppi = this.resources.displayMetrics.density * 160.0f
                mPhysicalCoeff = (SensorManager.GRAVITY_EARTH
                        * 39.37f
                        * ppi
                        * 0.84f)
            }
            return mPhysicalCoeff
        }

    /**
     * 根据松手后的滑动速度计算出fling的距离
     */
    private fun getSplineFlingDistance(velocity: Int): Double {
        val l = getSplineDeceleration(velocity)
        val decelMinusOne = DECELERATION_RATE - 1.0
        return mFlingFriction.toDouble() * physicalCoeff.toDouble() * Math.exp(DECELERATION_RATE / decelMinusOne * l)
    }

    /**
     * 根据距离计算出速度
     */
    private fun getVelocity(distance: Double): Int {
        val decelMinusOne = DECELERATION_RATE - 1.0
        val aecel = Math.log(distance / (mFlingFriction * mPhysicalCoeff)) * decelMinusOne / DECELERATION_RATE
        return Math.abs((Math.exp(aecel) * (mFlingFriction * mPhysicalCoeff) / INFLEXION).toInt())
    }

    private fun getSplineDeceleration(velocity: Int): Double {
        val ppi = this.resources.displayMetrics.density * 160.0f
        val mPhysicalCoeff = (SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * ppi
                * 0.84f) // look and feel tuning


        return Math.log((INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff)).toDouble())
    }

    companion object {
        private val INFLEXION = 0.35f // Tension lines cross at (INFLEXION, 1)
        private val DECELERATION_RATE = (Math.log(0.78) / Math.log(0.9)).toFloat()
    }
    //------------惯性滑动END-----------------------
}
