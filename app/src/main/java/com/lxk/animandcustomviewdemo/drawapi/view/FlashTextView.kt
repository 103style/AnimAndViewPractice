package com.lxk.animandcustomviewdemo.drawapi.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 20:00
 */
class FlashTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val textPaint: Paint
    private var clamp: LinearGradient? = null

    private var mMatrix: Matrix? = null

    private val color = intArrayOf(
        Color.WHITE,
        Color.parseColor("#008577"),
        Color.parseColor("#D81B60"),
        Color.parseColor("#00574B"),
        Color.WHITE
    )
    private val pos = floatArrayOf(0f, 0.1f, 0.5f, 0.9f, 1.0f)
    private var animator: ValueAnimator? = null
    private var dx: Float = 0.toFloat()

    init {
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.color = Color.WHITE
        textPaint.textSize = Utils.doToPx(context, 40)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))
        val txt = "103style---103Tech"
        val txtW = textPaint.measureText(txt)
        if (clamp == null) {
            clamp = LinearGradient(0f, 0f, txtW, 0f, color, pos, Shader.TileMode.CLAMP)
            mMatrix = Matrix()
        }
        mMatrix!!.setTranslate(dx, 0f)
        clamp!!.setLocalMatrix(mMatrix)
        textPaint.shader = clamp
        canvas.drawText(txt, (measuredWidth - txtW) / 2, (measuredHeight / 2).toFloat(), textPaint)

        startAnim(txtW)

    }

    private fun startAnim(width: Float) {
        if (animator == null) {
            animator = ValueAnimator.ofFloat(-width, width)
            animator!!.duration = 3000
            animator!!.repeatCount = ValueAnimator.INFINITE
            animator!!.repeatMode = ValueAnimator.RESTART
            animator!!.interpolator = LinearInterpolator()
            animator!!.addUpdateListener { animation ->
                dx = animation.animatedValue as Float
                postInvalidate()
            }
        }
        if (!animator!!.isRunning) {
            animator!!.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (animator != null) {
            animator!!.cancel()
        }
    }
}
