package com.lxk.animandcustomviewdemo.drawapi.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/10 16:59
 */
class RadialGradientDemoView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    private var DEFAULT_RADIUS = 0
    private val fillPaint: Paint
    private val textPaint: Paint
    private var clampRadialGradient: RadialGradient? = null
    private var rectH: Int = 0
    private var radius: Int = 0
    private var x: Int = 0
    private var y: Int = 0
    private var valueAnimator: ValueAnimator? = null

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.color = Color.WHITE
        textPaint.textSize = Utils.doToPx(context, 20)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectH = h / 4
        DEFAULT_RADIUS = width / 8
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        x = event.x.toInt()
        y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                radius = DEFAULT_RADIUS
                cancelAnim()
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                radius = DEFAULT_RADIUS
                invalidate()
            }
            MotionEvent.ACTION_UP -> startAnim()
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))
        if (radius > 0) {
            clampRadialGradient = RadialGradient(
                x.toFloat(), y.toFloat(), radius.toFloat(),
                0x00FFFFFF, -0xa70554, Shader.TileMode.CLAMP
            )
            fillPaint.shader = clampRadialGradient
            canvas.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat(), fillPaint)
        }
    }

    private fun startAnim() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(DEFAULT_RADIUS, height)
            valueAnimator!!.duration = 1000
            valueAnimator!!.interpolator = LinearInterpolator()
            valueAnimator!!.addUpdateListener { animation ->
                radius = animation.animatedValue as Int
                postInvalidate()
            }
            valueAnimator!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    radius = 0
                    postInvalidate()
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
        }
        if (!valueAnimator!!.isRunning) {
            valueAnimator!!.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnim()
    }

    internal fun cancelAnim() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
        }
    }
}
