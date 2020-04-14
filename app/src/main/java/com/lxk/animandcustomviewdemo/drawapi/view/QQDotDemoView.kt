package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 20:04
 *  base qq Android 8.3.0.4480
 */
class QQDotDemoView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    private val hidePathScale = 0.15f
    private val fillPaint: Paint
    private var circleCenterPoint: Point? = null
    private var gapW: Int = 0
    private var curX: Float = 0.toFloat()
    private var curY: Float = 0.toFloat()
    private val mPath: Path
    private var dotRegion: Region? = null
    private var radius: Int = 0
    private var changedR: Int = 0
    private var touched: Boolean = false

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        mPath = Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gapW = w / 8
        circleCenterPoint = Point(gapW, gapW)
        radius = gapW / 2
        changedR = gapW / 2
        //构建接受的点击区域
        dotRegion = Region(gapW / 2, gapW / 2, radius * 2 + gapW / 2, radius * 2 + gapW / 2)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //触摸到圆所在的位置
                if (dotRegion!!.contains(x.toInt(), y.toInt())) {
                    touched = true
                } else {
                    val group = parent as ViewGroup
                    group.removeView(this)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> if (touched) {
                //起始圆的半径随拖动距离越大 越来越小
                val dis = Math.sqrt(
                    Math.pow(
                        (x - circleCenterPoint!!.x).toDouble(),
                        2.0
                    ) + Math.pow((y - circleCenterPoint!!.y).toDouble(), 2.0)
                )
                //计算放缩比例
                val scale = radius * 2 / dis
                if (scale < hidePathScale) {
                    changedR = 0
                } else {
                    val t = if (scale > 1) 1F else scale.toFloat()
                    changedR = (radius * t).toInt()
                }
                curX = x
                curY = y
                updatePath(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                //恢复到初始状态
                changedR = radius
                touched = false
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun updatePath(x: Float, y: Float) {
        //计算 两圆圆心连线与 x轴 构成的锐角
        val degree =
            Math.atan((Math.abs(y - circleCenterPoint!!.y) / Math.abs(x - circleCenterPoint!!.x)).toDouble())
        Log.e(TAG, "degree = $degree")
        //原圆边上两点 x, y 的距离
        val rDx = (Math.sin(degree) * changedR).toFloat()
        val rDy = (Math.cos(degree) * changedR).toFloat()
        //手指所在圆边上两点 x, y 的距离
        val curDx = (Math.sin(degree) * (radius - changedR)).toFloat()
        val curDy = (Math.cos(degree) * (radius - changedR)).toFloat()

        mPath.reset()
        //原圆连线的右上点
        mPath.moveTo(circleCenterPoint!!.x + rDx, circleCenterPoint!!.y - rDy)
        //原圆连线的左下点
        mPath.lineTo(circleCenterPoint!!.x - rDx, circleCenterPoint!!.y + rDy)
        //协助点为两圆圆心连线的中点 终点为手指所在圆连线的左下点
        mPath.quadTo(
            (circleCenterPoint!!.x + x) / 2,
            (circleCenterPoint!!.y + y) / 2,
            x - curDx,
            y + curDy
        )
        //手指所在圆连线的右上点
        mPath.lineTo(x + curDx, y - curDy)
        //协助点为两圆圆心连线的中点   终点为原圆连线的右上点
        mPath.quadTo(
            (circleCenterPoint!!.x + x) / 2, (circleCenterPoint!!.y + y) / 2,
            circleCenterPoint!!.x + rDx, circleCenterPoint!!.y - rDy
        )

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————

        //绘制原圆
        canvas.drawCircle(
            circleCenterPoint!!.x.toFloat(),
            circleCenterPoint!!.x.toFloat(),
            changedR.toFloat(),
            fillPaint
        )
        if (touched) {
            if (changedR != 0) {
                //绘制两圆的连接区域
                canvas.drawPath(mPath, fillPaint)
            }
            //绘制手指触摸的圆
            canvas.drawCircle(curX, curY, (radius - changedR).toFloat(), fillPaint)
        }

    }

    companion object {
        private val TAG = "QQDotDemoView"
    }


}
