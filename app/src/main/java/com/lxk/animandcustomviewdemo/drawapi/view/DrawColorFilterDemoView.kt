package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 19:47
 */
class DrawColorFilterDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : View(context, attrs, style) {
    private val fillPaint: Paint
    private val textPaint: Paint
    private val bitmap: Bitmap

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        fillPaint.strokeWidth = Utils.doToPx(context, 2)
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.textSize = Utils.doToPx(context, 12)
        textPaint.color = Color.WHITE
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
    }


    fun draw(canvas: Canvas, rect: Rect, gapH: Float, type: String, txt: String) {
        canvas.drawRect(rect, fillPaint)
        canvas.drawBitmap(bitmap, 0F, gapH, fillPaint)
        canvas.drawText(type, 0F, gapH / 4, textPaint)
        canvas.drawText(txt, 0F, gapH * 3 / 4, textPaint)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(255, 255, 255, 255))
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        val gapW = width / 10F
        val gapH = height / 10F

        fillPaint.color = Color.argb(255, 103, 58, 183)
        val rect = Rect(0, 0, 2 * gapW.toInt(), gapH.toInt())
        draw(canvas, rect, gapH.toFloat(), "", "原色")

        val translateX = gapW * 5F / 2
        val translateY = gapH * 2F

        canvas.save()

        //ColorMatrixColorFilter、LightingColorFilter、PorterDuffColorFilter

//--------------------------------------------------------------------------------------------------

        /**
         * ColorMatrixColorFilter  颜色矩阵
         * 参考{@link DrawColorMatrixDemoView}
         */

//--------------------------------------------------------------------------------------------------
        //LightingColorFilter 光照颜色过滤器
        canvas.translate(translateX, 0F)
        fillPaint.colorFilter = LightingColorFilter(0xffffff, 0x0000f0)
        draw(canvas, rect, gapH, "LightingColor", "蓝色增强F0")

        canvas.translate(translateX, 0F)
        fillPaint.colorFilter = LightingColorFilter(0xffffff, 0x00f000)
        draw(canvas, rect, gapH, "LightingColor", "绿色增强F0")

        canvas.translate(translateX, 0F)
        fillPaint.colorFilter = LightingColorFilter(0xffffff, 0xf00000)
        draw(canvas, rect, gapH, "LightingColor", "红色增强F0")

        canvas.restore()


//--------------------------------------------------------------------------------------------------

        //PorterDuffColorFilter 颜色滤镜，也叫图形混合滤镜
        //Mode.ADD(饱和度相加)，Mode.DARKEN（变暗），Mode.LIGHTEN（变亮），
        //Mode.MULTIPLY（正片叠底），Mode.OVERLAY（叠加），Mode.SCREEN（滤色）
        textPaint.color = Color.BLACK

        canvas.save()
        canvas.translate(0F, translateY)

        val colorFilter = Color.RED
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.ADD)
        draw(canvas, rect, gapH, "PorterDuff", "ADD")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DARKEN)
        draw(canvas, rect, gapH, "PorterDuff", "DARKEN")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.LIGHTEN)
        draw(canvas, rect, gapH, "PorterDuff", "LIGHTEN")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY)
        draw(canvas, rect, gapH, "PorterDuff", "MULTIPLY")

        canvas.restore()

        canvas.save()

        canvas.translate(0f, translateY * 2)

        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.OVERLAY)
        draw(canvas, rect, gapH, "PorterDuff", "OVERLAY")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SCREEN)
        draw(canvas, rect, gapH, "PorterDuff", "SCREEN")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.XOR)
        draw(canvas, rect, gapH, "PorterDuff", "XOR")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.CLEAR)
        draw(canvas, rect, gapH, "PorterDuff", "CLEAR")


        canvas.restore()

        canvas.save()

        canvas.translate(0f, translateY * 3)

        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SRC)
        draw(canvas, rect, gapH, "PorterDuff", "SRC")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SRC_ATOP)
        draw(canvas, rect, gapH, "PorterDuff", "SRC_ATOP")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DST)
        draw(canvas, rect, gapH, "PorterDuff", "DST")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DST_ATOP)
        draw(canvas, rect, gapH, "PorterDuff", "DST_ATOP")


        canvas.restore()
        canvas.save()

        canvas.translate(0f, translateY * 4)

        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DST_IN)
        draw(canvas, rect, gapH, "PorterDuff", "DST_IN")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SRC_IN)
        draw(canvas, rect, gapH, "PorterDuff", "SRC_IN")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.DST_OUT)
        draw(canvas, rect, gapH, "PorterDuff", "DST_OUT")

        canvas.translate(translateX, 0f)
        fillPaint.colorFilter = PorterDuffColorFilter(colorFilter, PorterDuff.Mode.SRC_OUT)
        draw(canvas, rect, gapH, "PorterDuff", "SRC_OUT")
    }
}