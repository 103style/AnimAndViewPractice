package com.lxk.animandcustomviewdemo.drawapi

import android.content.Context
import android.graphics.Color
import android.graphics.Paint

/**
 * @author https://github.com/103style
 * @date 2020/4/7 13:48
 */
object Utils {
    @JvmStatic

    fun initPaint(context: Context, style: Paint.Style): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        //设置画笔颜色
        paint.color = Color.RED
        //设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        paint.style = style
        //设置画笔宽度
        paint.strokeWidth = doToPx(context, 6).toFloat()
        return paint
    }

    @JvmStatic
    fun doToPx(context: Context, value: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (value * scale + 0.5f).toInt()
    }
}