package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/13 20:04
 */
class BitmapShaderModeDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : View(context, attrs, style) {
    private var bitmap: Bitmap? = null
    private var fillPaint: Paint = Utils.initPaint(context, Paint.Style.FILL)
    private var textPaint: Paint = Utils.initPaint(context, Paint.Style.FILL)

    init {
        textPaint.color = Color.WHITE
        textPaint.textSize = Utils.doToPx(context, 14)
    }

    private var clamp: BitmapShader? = null
    private var repeat: BitmapShader? = null
    private var mirror: BitmapShader? = null
    private var gap: Float = 0F
    private var end: Float = 0F
    private var rectH: Float = 0F
    private var mTop: Float = 0F


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        gap = width / 10F
        mTop = gap
        rectH = height / 4F
        end = width - gap * 2

        val temp = BitmapFactory.decodeResource(resources, R.drawable.android11)
        val matrix = Matrix()
        val scale = rectH / 3F / temp.width
        matrix.setScale(scale, scale)
        bitmap = Bitmap.createBitmap(temp, 0, 0, temp.width, temp.height, matrix, true)
        val canvas = Canvas(bitmap!!)
        fillPaint.color = Color.RED
        canvas.drawLine(0F, 0F, 0F, bitmap!!.height.toFloat(), fillPaint)
        fillPaint.color = Color.GREEN
        canvas.drawLine(0F, 0F, bitmap!!.width.toFloat(), 0F, fillPaint)
        fillPaint.color = Color.YELLOW
        canvas.drawLine(
            0F, bitmap!!.height.toFloat(),
            bitmap!!.width.toFloat(), bitmap!!.height.toFloat(),
            fillPaint
        )

        clamp = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        repeat = BitmapShader(bitmap!!, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        mirror = BitmapShader(bitmap!!, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        // BitmapShader 是以canvas的左上角为起始点开始覆盖的
        // 无论你利用绘图函数绘多大一块，在哪绘制，与Shader无关。
        // 因为Shader总是在控件的左上角开始，而你绘制的部分只是显示出来的部分而已。
        // 没有绘制的部分虽然已经生成，但只是不会显示出来罢了。

        //x y 轴 在原图的基础上边界拉伸
        drawTest(canvas, clamp!!, "Shader.TileMode.CLAMP")

        mTop = mTop + rectH + gap
        //x y 轴平移
        drawTest(canvas, repeat!!, "Shader.TileMode.REPEAT")

        mTop = mTop + rectH + gap
        //x y 轴翻转
        drawTest(canvas, mirror!!, "Shader.TileMode.MIRROR")
    }

    fun drawTest(canvas: Canvas, bitmapShader: BitmapShader, txt: String) {
        canvas.save()
        fillPaint.shader = bitmapShader
        canvas.translate(gap, mTop)
        canvas.drawRect(0F, 0F, end, rectH, fillPaint)
        canvas.drawText(txt, 0F, 0F, textPaint)
        canvas.restore()
    }
}