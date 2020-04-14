package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/7 20:19
 * doc:https://blog.csdn.net/harvic880925/article/details/51264653
 * doc:https://developer.android.com/reference/android/graphics/PorterDuff.Mode.html
 */
class DrawXfermodeDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val fillPaint: Paint
    private val textPaint: Paint
    private var src: Bitmap? = null
    private var dst: Bitmap? = null

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.textSize = Utils.doToPx(context, 12)
        textPaint.color = Color.parseColor("#FFF44336")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        src = makeSrc(w / 5)
        dst = makeDst(w / 5)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.WHITE)
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        val gapW = width / 10
        val gapH = height / 10

        val translateX = gapW * 5 / 2
        val translateY = gapW * 3

        xfermodeDraw(canvas, PorterDuff.Mode.CLEAR, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.SRC, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.DST, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.SRC_OVER, gapW)

        canvas.restore()

        canvas.save()

        canvas.translate(0f, translateY.toFloat())
        xfermodeDraw(canvas, PorterDuff.Mode.DST_OVER, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.SRC_IN, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.DST_IN, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.SRC_OUT, gapW)

        canvas.restore()

        canvas.save()

        canvas.translate(0f, (translateY * 2).toFloat())
        xfermodeDraw(canvas, PorterDuff.Mode.DST_OUT, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.SRC_ATOP, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.DST_ATOP, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.XOR, gapW)

        canvas.restore()

        canvas.save()

        canvas.translate(0f, (translateY * 3).toFloat())
        xfermodeDraw(canvas, PorterDuff.Mode.DARKEN, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.LIGHTEN, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.MULTIPLY, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.SCREEN, gapW)


        canvas.restore()

        canvas.save()

        canvas.translate(0f, (translateY * 4).toFloat())
        xfermodeDraw(canvas, PorterDuff.Mode.ADD, gapW)
        canvas.translate(translateX.toFloat(), 0f)
        xfermodeDraw(canvas, PorterDuff.Mode.OVERLAY, gapW)
    }

    private fun xfermodeDraw(canvas: Canvas, mode: PorterDuff.Mode, gapW: Int) {
        val l = gapW * 5 / 2
        val layerId = canvas.saveLayer(0f, 0f, l.toFloat(), l.toFloat(), fillPaint)
        canvas.drawBitmap(dst!!, 0f, 0f, fillPaint)
        fillPaint.xfermode = PorterDuffXfermode(mode)
        //类似google示例中dst src 的完全重叠
        canvas.drawBitmap(src!!, 0f, 0f, fillPaint)
        //还原图层
        fillPaint.xfermode = null
        canvas.restoreToCount(layerId)

        val txt = mode.toString()
        val fontMetrics = fillPaint.fontMetrics
        val dy = Math.abs(fontMetrics.top)
        canvas.drawText(txt, (gapW * 2 - textPaint.measureText(txt)) / 2, l / 2 - dy, textPaint)
    }


    private fun makeDst(w: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = -0x995501
        c.drawCircle((w / 2).toFloat(), (w / 2).toFloat(), (w / 4).toFloat(), p)
        return bm
    }

    private fun makeSrc(w: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = -0x33bc
        c.drawRect((w / 2).toFloat(), (w / 2).toFloat(), w.toFloat(), w.toFloat(), p)
        return bm
    }

}
