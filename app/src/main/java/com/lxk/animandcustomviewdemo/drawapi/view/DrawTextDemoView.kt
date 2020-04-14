package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 20:00
 */
class DrawTextDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val textPaint: Paint

    init {
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.strokeWidth = Utils.doToPx(context, 1)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        val gapW = width / 10
        val gapH = height / 10

        textPaint.textSize = Utils.doToPx(context, 30)

        val baseX = 0
        val baseY = gapH * 5
        val txt = "What a funny demo!"
        val txtWidth = textPaint.measureText(txt)
        //居中绘制
        canvas.drawText(txt, (width - txtWidth) / 2, baseY.toFloat(), textPaint)
        //基准线
        textPaint.color = Color.WHITE
        canvas.drawLine(
            baseX.toFloat(),
            baseY.toFloat(),
            width.toFloat(),
            baseY.toFloat(),
            textPaint
        )

        var fontMetrics: Paint.FontMetrics = textPaint.fontMetrics
        textPaint.color = Color.RED
        //可绘制的最上线
        val top = baseY + fontMetrics.top
        canvas.drawLine(baseX.toFloat(), top, width.toFloat(), top, textPaint)
        textPaint.color = Color.YELLOW
        //可绘制的最下线
        val bottom = baseY + fontMetrics.bottom
        canvas.drawLine(baseX.toFloat(), bottom, width.toFloat(), bottom, textPaint)
        textPaint.color = Color.BLUE
        //系统建议的最上线
        val ascent = baseY + fontMetrics.ascent
        canvas.drawLine(baseX.toFloat(), ascent, width.toFloat(), ascent, textPaint)
        textPaint.color = Color.GREEN
        //系统建议的最下线
        val descent = baseY + fontMetrics.descent
        canvas.drawLine(baseX.toFloat(), descent, (width / 2).toFloat(), descent, textPaint)
        //中心线
        textPaint.color = Color.DKGRAY
        val centerY = top + (bottom - top) / 2
        canvas.drawLine(baseX.toFloat(), centerY, width.toFloat(), centerY, textPaint)

        textPaint.textSize = Utils.doToPx(context, 14)
        textPaint.color = Color.WHITE
        fontMetrics = textPaint.fontMetrics
        canvas.drawText(
            "fontMetrics.top = " + fontMetrics.top,
            baseX.toFloat(),
            gapH.toFloat(),
            textPaint
        )
        canvas.drawText(
            "fontMetrics.ascent = " + fontMetrics.ascent,
            baseX.toFloat(),
            (gapH * 3 / 2).toFloat(),
            textPaint
        )
        canvas.drawText(
            "fontMetrics.descent = " + fontMetrics.descent,
            baseX.toFloat(),
            (gapH * 2).toFloat(),
            textPaint
        )
        canvas.drawText(
            "fontMetrics.bottom = " + fontMetrics.bottom,
            baseX.toFloat(),
            (gapH * 5 / 2).toFloat(),
            textPaint
        )
        val dy = Math.abs(fontMetrics.top + fontMetrics.bottom) / 2
        val topS = "top"
        canvas.drawText(topS, baseX.toFloat(), top - dy, textPaint)
        val bottomS = "bottom"
        canvas.drawText(bottomS, baseX.toFloat(), bottom + dy, textPaint)
        val ascentS = "ascent"
        canvas.drawText(ascentS, width - textPaint.measureText(ascentS), ascent + dy, textPaint)
        val descentS = "descent"
        canvas.drawText(descentS, width - textPaint.measureText(descentS), descent + dy, textPaint)

        val centerS = "center"
        canvas.drawText(centerS, gapW.toFloat(), centerY, textPaint)

    }

}
