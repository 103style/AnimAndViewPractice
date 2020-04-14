package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 20:00
 */
class DrawTextAndPathDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val fillPaint: Paint
    private val strokePaint: Paint
    private val textPaint: Paint
    private val path: Path

    init {

        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        strokePaint = Utils.initPaint(context, Paint.Style.STROKE)

        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.color = Color.WHITE
        //设置阴影
        textPaint.setShadowLayer(10f, 15f, 15f, Color.BLACK)
        textPaint.textSize = Utils.doToPx(context, 14)

        path = Path()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        val gapW = width / 10
        val gapH = height / 10

        val text = "103style的测试文字"
        canvas.drawText(text, (gapW * 5).toFloat(), gapH.toFloat(), textPaint)
        textPaint.clearShadowLayer()

        //直线路径
        path.moveTo(gapW.toFloat(), gapH.toFloat())
        path.lineTo(gapW.toFloat(), (gapH * 2).toFloat())
        path.lineTo((gapW * 5).toFloat(), (gapH * 2).toFloat())
        path.close()
        canvas.drawPath(path, fillPaint)

        //矩形路径
        path.reset()
        val rectFCW = RectF(
            (gapW * 2).toFloat(),
            (gapH * 3).toFloat(),
            (gapW * 5).toFloat(),
            (gapH * 4).toFloat()
        )
        //顺时针方向
        path.addRect(rectFCW, Path.Direction.CW)
        //绘制路径
        canvas.drawPath(path, strokePaint)

        val offset = 0f
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint)

        path.reset()
        val rectFCCW = RectF(
            (gapW * 6).toFloat(),
            (gapH * 3).toFloat(),
            (gapW * 9).toFloat(),
            (gapH * 4).toFloat()
        )
        //逆时针方向
        path.addRect(rectFCCW, Path.Direction.CCW)
        //绘制路径
        canvas.drawPath(path, strokePaint)
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint)

        //圆角矩形路径
        path.reset()
        val rect = RectF(
            (gapW * 2).toFloat(),
            (gapH * 5).toFloat(),
            (gapW * 5).toFloat(),
            (gapH * 6).toFloat()
        )
        path.addRoundRect(rect, 10f, 10f, Path.Direction.CW)
        //绘制路径
        canvas.drawPath(path, strokePaint)
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint)


        //圆形路径
        path.reset()
        path.addCircle(
            (gapW * 7).toFloat(),
            (gapH * 5 + gapW).toFloat(),
            gapW.toFloat(),
            Path.Direction.CW
        )
        //绘制路径
        canvas.drawPath(path, strokePaint)
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint)


        //椭圆形路径
        path.reset()
        val ovalRectF = RectF(
            (gapW * 2).toFloat(),
            (gapH * 7).toFloat(),
            (gapW * 5).toFloat(),
            (gapH * 8).toFloat()
        )
        path.addOval(ovalRectF, Path.Direction.CW)
        //绘制路径
        canvas.drawPath(path, strokePaint)
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint)


        //椭圆形路径
        path.reset()
        val arcRectF = RectF(
            (gapW * 6).toFloat(),
            (gapH * 7).toFloat(),
            (gapW * 9).toFloat(),
            (gapH * 8).toFloat()
        )
        path.addArc(arcRectF, 0f, 120f)
        //绘制路径
        canvas.drawPath(path, strokePaint)
        //在路径上绘制文字
        canvas.drawTextOnPath(text, path, offset, offset, textPaint)

        //向左倾斜
        textPaint.textSkewX = 0.5f
        //设置下划线
        textPaint.isUnderlineText = true
        canvas.drawText("向左倾斜+下划线", gapW.toFloat(), (gapH * 17 / 2).toFloat(), textPaint)

        //设置带有删除线效果
        textPaint.isStrikeThruText = true
        //取消下划线
        textPaint.isUnderlineText = false
        ////向右倾斜
        textPaint.textSkewX = -0.5f
        canvas.drawText("向右倾斜+删除线", (width / 2).toFloat(), (gapH * 17 / 2).toFloat(), textPaint)

        //去掉删除线效果
        textPaint.isStrikeThruText = false
        textPaint.textScaleX = 2f
        textPaint.textSkewX = 0f
        canvas.drawText("横向拉伸2倍", gapW.toFloat(), (gapH * 9).toFloat(), textPaint)

    }

}
