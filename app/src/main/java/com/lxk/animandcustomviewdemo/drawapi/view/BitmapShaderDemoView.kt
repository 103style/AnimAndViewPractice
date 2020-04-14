package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/12 22:24
 */
class BitmapShaderDemoView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    style: Int = 0
) : View(context, attr, style) {
    private var bitmap: Bitmap? = null
    private var fillPaint: Paint = Utils.initPaint(context, Paint.Style.FILL)
    private var clamp: BitmapShader? = null

    private var x = 0
    private var y = 0
    private var radius = Utils.doToPx(context, 96)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val temp = BitmapFactory.decodeResource(resources, R.drawable.mm)
        val matrix = Matrix()
        //扩大到铺满屏幕
        val scaleX = width * 1.0f / temp.width
        matrix.setScale(scaleX, scaleX)
        bitmap = Bitmap.createBitmap(temp, 0, 0, temp.width, temp.height, matrix, true)
        clamp = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        fillPaint.shader = clamp
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_MOVE -> {
                x = event.x.toInt()
                y = event.y.toInt()
            }
            MotionEvent.ACTION_UP -> {
                x = 0
                y = 0
            }
            else -> {
            }
        }
        invalidate()
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        if (x != 0 && y != 0) {
            canvas.drawCircle(x.toFloat(), y.toFloat(), radius.toFloat(), fillPaint)
        }
    }
}