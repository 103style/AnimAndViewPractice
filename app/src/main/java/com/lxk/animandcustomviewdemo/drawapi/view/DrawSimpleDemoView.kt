package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 20:01
 */
class DrawSimpleDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val fillPaint: Paint
    private val strokePaint: Paint

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        strokePaint = Utils.initPaint(context, Paint.Style.STROKE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        //—————————— 以下为示例  开发时不要在 onDraw中创建对象 ——————————

        val gapW = width / 10
        val gapH = height / 10

        //画线
        canvas.drawLine(0f, 0f, gapW.toFloat(), gapH.toFloat(), fillPaint)

        canvas.drawLines(
            floatArrayOf(
                (gapW * 2).toFloat(),
                gapH.toFloat(),
                (gapW * 3).toFloat(),
                0f,
                (gapW * 3).toFloat(),
                0f,
                (gapW * 4).toFloat(),
                gapH.toFloat(),
                (gapW * 4).toFloat(),
                gapH.toFloat(),
                (gapW * 5).toFloat(),
                0f
            ), strokePaint
        )

        //画点
        val y = gapH * 3 / 2
        canvas.drawPoint(gapW.toFloat(), y.toFloat(), fillPaint)
        canvas.drawPoints(
            floatArrayOf(
                (gapW * 2).toFloat(),
                (y / 3).toFloat(),
                (gapW * 3).toFloat(),
                y.toFloat(),
                (gapW * 4).toFloat(),
                y.toFloat(),
                (gapW * 5).toFloat(),
                y.toFloat(),
                (gapW * 6).toFloat(),
                y.toFloat()
            ), strokePaint
        )

        //画圆
        canvas.drawCircle((gapW * 3).toFloat(), (gapH * 3).toFloat(), gapW.toFloat(), fillPaint)

        canvas.drawCircle((gapW * 7).toFloat(), (gapH * 3).toFloat(), gapW.toFloat(), strokePaint)


        //使用RectF构造矩形
        val rect = RectF(
            gapW.toFloat(),
            (gapH * 4).toFloat(),
            (width * 2 / 10).toFloat(),
            (gapH * 5).toFloat()
        )
        canvas.drawRect(rect, fillPaint)
        //使用Rect构造矩形
        val rect2 = Rect(gapW * 3, gapH * 4, gapW * 5, gapH * 5)
        canvas.drawRect(rect2, strokePaint)

        //使用Rect构造圆角矩形
        val rect3 = RectF(
            (gapW * 6).toFloat(),
            (gapH * 4).toFloat(),
            (width * 8 / 10).toFloat(),
            (gapH * 5).toFloat()
        )
        canvas.drawRoundRect(rect3, 20f, 10f, fillPaint)

        //圆弧
        val rect4 = RectF(
            gapW.toFloat(),
            (gapH * 5).toFloat(),
            (width * 4 / 10).toFloat(),
            (gapH * 6).toFloat()
        )
        canvas.drawArc(rect4, 0f, 90f, true, fillPaint)
        val rect5 = RectF(
            (gapW * 6).toFloat(),
            (gapH * 5).toFloat(),
            (width * 9 / 10).toFloat(),
            (gapH * 6).toFloat()
        )
        canvas.drawArc(rect5, 0f, 90f, false, strokePaint)

        //椭圆
        val rect6 =
            RectF(gapW.toFloat(), (gapH * 7).toFloat(), (gapW * 4).toFloat(), (gapH * 8).toFloat())
        canvas.drawOval(rect6, fillPaint)
        //椭圆
        val rect7 = RectF(
            (gapW * 6).toFloat(),
            (gapH * 7).toFloat(),
            (gapW * 9).toFloat(),
            (gapH * 8).toFloat()
        )
        canvas.drawOval(rect7, strokePaint)
    }

}
