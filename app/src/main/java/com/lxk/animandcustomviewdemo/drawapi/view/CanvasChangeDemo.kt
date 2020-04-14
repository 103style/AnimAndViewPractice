package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/13 20:18
 */
class CanvasChangeDemo @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private var fillPaint: Paint = Utils.initPaint(context, Paint.Style.FILL)
    private var strokePaint: Paint = Utils.initPaint(context, Paint.Style.STROKE)

    init {
        fillPaint.textSize = Utils.doToPx(context, 16)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        val gapW = width / 10
        val gapH = height / 10

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))
        //保存的画布大小为全屏幕大小
        canvas.save()
        canvas.clipRect(Rect(gapW, gapH, width - gapW, height - gapH))
        canvas.drawColor(Color.argb(255, 0, 0, 255))
        //将栈顶的画布状态取出来，作为当前画布，并画成黄色背景
        canvas.restore()

        canvas.save()
        canvas.clipRect(Rect(gapW * 2, gapH * 2, width - gapW * 2, height - gapH * 2))
        canvas.drawColor(Color.argb(255, 0, 64, 255))
        canvas.restore()

        canvas.save()
        canvas.clipRect(Rect(gapW * 3, gapH * 3, width - gapW * 3, height - gapH * 3))
        canvas.drawColor(Color.argb(255, 0, 128, 255))
        canvas.restore()

        canvas.save()
        canvas.clipRect(Rect(gapW * 4, gapH * 4, width - gapW * 4, height - gapH * 4))
        canvas.drawColor(Color.argb(255, 0, 192, 255))
        canvas.restore()


        //保存当前的画布到对应的栈中
        canvas.save()
        drawCircle(canvas, gapW)
        //画布原点向右向下平移gapH
        canvas.translate(gapH.toFloat(), gapH.toFloat())
        drawCircle(canvas, gapW)
        //画布原点向右向上平移gapH
        canvas.translate(gapH.toFloat(), -gapH.toFloat())
        drawCircle(canvas, gapW)
        //弹出之前保存的画布 继续在其上绘画，与save一起出现
        canvas.restore()

        canvas.save()
        fillPaint.color = Color.MAGENTA
        strokePaint.color = Color.MAGENTA
        val rect = Rect(gapW * 3, gapH * 3, gapW * 5, gapH * 4)
        canvas.drawRect(rect, strokePaint)
        canvas.drawText("旋转前", gapW * 3f, gapH * 3f, fillPaint)
        //旋转操作
        canvas.rotate(-30f)
        canvas.drawRect(rect, strokePaint)
        canvas.drawText("旋转-30°后", gapW * 3f, gapH * 3f, fillPaint)
        canvas.restore()


        canvas.save()
        fillPaint.color = Color.GREEN
        canvas.drawText("放缩前", gapW.toFloat(), gapH * 3f, fillPaint)
        //放缩操作
        canvas.scale(2f, 2f)
        canvas.drawText("x、y扩大2倍后", gapW.toFloat(), gapH * 3f, fillPaint)
        //放缩操作
        canvas.scale(0.8f, 0.8f)
        canvas.drawText("x、y缩小到2倍的80%后", gapW.toFloat(), gapH * 3f, fillPaint)
        canvas.restore()


        canvas.save()
        fillPaint.color = Color.WHITE
        canvas.drawText("扭曲操作前", gapW * 4f, gapH.toFloat(), fillPaint)
        //扭曲操作
        canvas.skew(2f, 2f)
        canvas.drawText("扭曲操作后", gapW * 4f, gapH.toFloat(), fillPaint)
        canvas.restore()
    }

    fun drawCircle(canvas: Canvas, w: Int) {
        canvas.drawCircle(w.toFloat(), w.toFloat(), w.toFloat(), fillPaint)
    }
}