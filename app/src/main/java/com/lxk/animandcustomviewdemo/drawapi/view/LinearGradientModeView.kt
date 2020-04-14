package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/10 15:31
 */
class LinearGradientModeView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    private val fillPaint: Paint
    private val textPaint: Paint
    private var clamp: LinearGradient? = null
    private var repeat: LinearGradient? = null
    private var mirror: LinearGradient? = null

    private var gap: Int = 0
    private var end: Int = 0
    private var rectH: Int = 0
    private var mTop: Int = 0

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.color = Color.WHITE
        textPaint.textSize = Utils.doToPx(context, 14)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gap = width / 10
        mTop = gap
        rectH = height / 4
        end = width - gap * 2
        val color = intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        clamp =
            LinearGradient(0f, 0f, gap.toFloat(), gap.toFloat(), color, null, Shader.TileMode.CLAMP)
        repeat = LinearGradient(
            0f,
            0f,
            gap.toFloat(),
            gap.toFloat(),
            color,
            null,
            Shader.TileMode.REPEAT
        )
        mirror = LinearGradient(
            0f,
            0f,
            gap.toFloat(),
            gap.toFloat(),
            color,
            null,
            Shader.TileMode.MIRROR
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        // Shader 是以canvas的左上角为起始点开始覆盖的
        // 无论你利用绘图函数绘多大一块，在哪绘制，与Shader无关。
        // 因为Shader总是在控件的左上角开始，而你绘制的部分只是显示出来的部分而已。
        // 没有绘制的部分虽然已经生成，但只是不会显示出来罢了。

        //x y 轴 在原图的基础上边界拉伸
        drawTest(canvas, clamp, "Shader.TileMode.CLAMP")

        mTop = mTop + rectH + gap
        //x y 轴平移
        drawTest(canvas, repeat, "Shader.TileMode.REPEAT")

        mTop = mTop + rectH + gap
        //x y 轴翻转
        drawTest(canvas, mirror, "Shader.TileMode.MIRROR")
    }

    private fun drawTest(canvas: Canvas, linearGradient: LinearGradient?, txt: String) {
        canvas.save()
        fillPaint.shader = linearGradient
        canvas.translate(gap.toFloat(), mTop.toFloat())
        canvas.drawRect(0f, 0f, end.toFloat(), rectH.toFloat(), fillPaint)
        canvas.drawText(txt, 0f, 0f, textPaint)
        canvas.restore()
    }
}
