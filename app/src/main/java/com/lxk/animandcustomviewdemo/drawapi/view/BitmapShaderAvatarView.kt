package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/12 20:57
 */
class BitmapShaderAvatarView constructor(
        context: Context,
        attr: AttributeSet? = null,
        style: Int = 0) : View(context, attr, style) {
    private var bitmap: Bitmap? = null
    private var fillPaint: Paint = Utils.initPaint(context, Paint.Style.FILL)
    private var clamp: BitmapShader? = null
    private var path: Path = Path()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val temp = BitmapFactory.decodeResource(resources, R.drawable.android11)
        val matrix = Matrix()
        //扩大到铺满屏幕
        val scaleX = w / 5 * 1.0f / temp.width
        matrix.setScale(scaleX, scaleX)
        bitmap = Bitmap.createBitmap(temp, 0, 0, temp.width, temp.height, matrix, true)
        clamp = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        fillPaint.shader = clamp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        val width = measuredWidth.toFloat() / 5
        val translate = width * 2
        val gap = translate / 4
        canvas.save()
        canvas.translate(gap, gap)
        canvas.drawCircle(width / 2, width / 2, width / 2, fillPaint)

        canvas.translate(translate - gap, 0f)
        canvas.drawRoundRect(0f, 0f, width, width, width / 4, width / 4, fillPaint)

        canvas.translate(translate - gap, 0f)
        canvas.drawRoundRect(0f, 0f, width, width, width / 8, width / 8, fillPaint)

        canvas.restore()


        canvas.save()
        canvas.translate(gap, translate + gap)

        canvas.drawRect(0f, 0f, width, width, fillPaint)

        canvas.translate(translate - gap, 0f)
        path.reset()
        path.moveTo(width / 2, 0f)
        path.lineTo(0f, width / 2)
        path.lineTo(width / 2, width)
        path.lineTo(width, width / 2)
        path.close()
        canvas.drawPath(path, fillPaint)

        canvas.translate(translate - gap, 0f)
        //五角星画法
        //https://jingyan.baidu.com/article/e4d08ffda964730fd2f60dbd.html
        path.reset()
        //五角星把圆分成五份  每份圆弧角度为 360 / 5 = 72
        val cos72 = (width / 2.0f * Math.cos(Math.PI / 180 * 72)).toInt()
        val sin72 = (width / 2.0f * Math.sin(Math.PI / 180 * 72)).toInt()
        //54 = 72 - (90-72)
        val cos54 = (width / 2.0f * Math.cos(Math.PI / 180 * 54)).toInt()
        val sin54 = (width / 2.0f * Math.sin(Math.PI / 180 * 54)).toInt()
        path.moveTo(width / 2, 0f)
        path.lineTo(width / 2 - cos54, width / 2 + sin54)
        path.lineTo(width / 2 + sin72, width / 2 - cos72)
        path.lineTo(width / 2 - sin72, width / 2 - cos72)
        path.lineTo(width / 2 + cos54, width / 2 + sin54)
        path.close()
        canvas.drawPath(path, fillPaint)
        canvas.restore()


        canvas.save()
        canvas.translate(gap, translate * 2 + gap)

        //爱心
        //http://www.360doc.com/content/17/0819/14/44182525_680393802.shtml
        //第 05点  例子中的 2 表示爱心上部宽度额一半    -2.14 表示 爱心下部分的高度
        path.reset()
        //爱心上部的公式
        path.moveTo(0f, width / 4)
        var dx = 0f
        for (i in 0 until width.toInt()) {
            //width/2 → 0 → width/2
            dx = if (i > width / 2f) (i - width / 2f) else (width / 2f - i)
            path.lineTo(i.toFloat(), (width / 4 - Math.sqrt(width / 2.toDouble() * dx - dx * dx)).toFloat())

        }
        path.lineTo(width, width / 4)
        path.lineTo(0F, width / 4)
        var ty = 0f
        //爱心下部的公式
        val dy = (width * 3 / 4 / Math.sqrt(Math.sqrt(width / 2.toDouble()))).toFloat()
        for (i in 0 until width.toInt()) {
            //width/2 → 0 → width/2
            dx = if (i > width / 2f) (i - width / 2f) else (width / 2f - i)
            ty = width / 4f + (dy * Math.sqrt(Math.sqrt(width / 2.toDouble()) - Math.sqrt(dx.toDouble())).toFloat())
            path.lineTo(i.toFloat(), ty)

        }
        path.lineTo(width, width / 4)
        canvas.drawPath(path, fillPaint)
        canvas.restore()
    }

}