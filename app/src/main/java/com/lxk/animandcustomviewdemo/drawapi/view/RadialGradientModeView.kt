package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 20:05
 */
class RadialGradientModeView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    private val fillPaint: Paint
    private val textPaint: Paint
    private var clampRadialGradient: RadialGradient? = null
    private var repeatRadialGradient: RadialGradient? = null
    private var mirrorRadialGradient: RadialGradient? = null
    private var rectH: Int = 0
    private var gap: Int = 0

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.color = Color.WHITE
        textPaint.textSize = Utils.doToPx(context, 20)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectH = h / 4
        gap = rectH / 4
        clampRadialGradient = RadialGradient(
            (w / 2).toFloat(), (rectH / 2).toFloat(), (rectH / 4).toFloat(),
            Color.RED, Color.GREEN, Shader.TileMode.CLAMP
        )
        repeatRadialGradient = RadialGradient(
            (w / 2).toFloat(), (rectH / 2).toFloat(), (rectH / 4).toFloat(),
            Color.RED, Color.GREEN, Shader.TileMode.REPEAT
        )
        mirrorRadialGradient = RadialGradient(
            (w / 2).toFloat(), (rectH / 2).toFloat(), (rectH / 4).toFloat(),
            Color.RED, Color.GREEN, Shader.TileMode.MIRROR
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        canvas.save()
        canvas.translate(0f, gap.toFloat())
        fillPaint.shader = clampRadialGradient
        canvas.drawCircle(
            (width / 2).toFloat(),
            (rectH / 2).toFloat(),
            (rectH / 2).toFloat(),
            fillPaint
        )
        var txt = "Shader.TileMode.CLAMP"
        canvas.drawText(
            txt,
            (measuredWidth - textPaint.measureText(txt)) / 2,
            (rectH / 2).toFloat(),
            textPaint
        )

        canvas.translate(0f, (gap + rectH).toFloat())
        fillPaint.shader = repeatRadialGradient
        canvas.drawCircle(
            (width / 2).toFloat(),
            (rectH / 2).toFloat(),
            (rectH / 2).toFloat(),
            fillPaint
        )
        txt = "Shader.TileMode.REPEAT"
        canvas.drawText(
            txt,
            (measuredWidth - textPaint.measureText(txt)) / 2,
            (rectH / 2).toFloat(),
            textPaint
        )

        canvas.translate(0f, (gap + rectH).toFloat())
        fillPaint.shader = mirrorRadialGradient
        canvas.drawCircle(
            (width / 2).toFloat(),
            (rectH / 2).toFloat(),
            (rectH / 2).toFloat(),
            fillPaint
        )
        txt = "Shader.TileMode.MIRROR"
        canvas.drawText(
            txt,
            (measuredWidth - textPaint.measureText(txt)) / 2,
            (rectH / 2).toFloat(),
            textPaint
        )

        canvas.restore()

    }


}
