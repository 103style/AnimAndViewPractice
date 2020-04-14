package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/13 20:28
 */
class DrawBezierPenView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    style: Int = 0
) : View(context, attributeSet, style) {

    private var strokePaint: Paint = Utils.initPaint(context, Paint.Style.STROKE)
    private var textPaint: Paint = Utils.initPaint(context, Paint.Style.FILL)
    private val mPath = Path()
    private var mPreX = 0f
    private var mPreY = 0f

    init {
        strokePaint.strokeWidth = Utils.doToPx(context, 1)
        textPaint.textSize = Utils.doToPx(context, 16)
        textPaint.color = Color.WHITE
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath.moveTo(event.x, event.y)
                mPreX = event.x
                mPreY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (mPreX + event.x) / 2
                val endY = (mPreY + event.y) / 2
                mPath.quadTo(mPreX, mPreY, endX, endY)
                mPreX = event.x
                mPreY = event.y
                postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (event.y < 200) {
                    if (event.x > width / 2) {
                        visibility = GONE
                    }
                    mPath.reset()
                    postInvalidate()
                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————

        val reset = "reset"
        canvas.drawText(reset, strokePaint.measureText(reset) / 2, 100F, textPaint)
        val hide = "hide"
        canvas.drawText(
            hide,
            measuredWidth / 2 + strokePaint.measureText(hide) / 2,
            100F,
            textPaint
        )
        canvas.drawPath(mPath, strokePaint)
    }
}