package com.lxk.animandcustomviewdemo.drawapi.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/13 20:34
 */
class DrawBezierWaterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : View(context, attrs, style) {

    internal var r = 0
    internal var y = 0
    internal var dx = 0
    private val fillPaint: Paint
    private val mPath: Path = Path()
    private var valueAnimator: ValueAnimator? = null
    //波的振幅
    private var amplitude = 32

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        fillPaint.color = Color.LTGRAY
        amplitude = Utils.doToPx(context, amplitude).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        r = w / 2
        y = w / 4
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        //重置path
        mPath.reset()

        //左右偏移的距离
        val gap = 2F * r
        //移动到坐标的偏移点
        mPath.moveTo(-gap + dx, y.toFloat())
        var i = -gap
        while (i <= width + gap) {
            //在 gap 的前一个 r距离 中间上方添加一个控制点
            mPath.rQuadTo((r / 2).toFloat(), (-amplitude).toFloat(), r.toFloat(), 0f)
            //在 gap 的后一个 r距离 中间下方添加一个控制点
            mPath.rQuadTo((r / 2).toFloat(), amplitude.toFloat(), r.toFloat(), 0f)
            i += gap
        }
        //组成闭合区间
        mPath.lineTo(width.toFloat(), height.toFloat())
        mPath.lineTo(0f, height.toFloat())
        mPath.close()
        //绘制路径
        canvas.drawPath(mPath, fillPaint)
        //做Y轴的变化
        y += 2
        if (y > height + amplitude) {
            //结束动画并移除当前视图
            stopAnim()
            val group = parent as ViewGroup
            group.removeView(this)
        } else {
            //开始动画
            startAnim()
        }
    }

    private fun startAnim() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(0, 2 * r)
            valueAnimator!!.duration = 2000
            valueAnimator!!.repeatMode = ValueAnimator.RESTART
            valueAnimator!!.repeatCount = ValueAnimator.INFINITE
            valueAnimator!!.interpolator = LinearInterpolator()
            valueAnimator!!.addUpdateListener { animation ->
                dx = animation.animatedValue as Int
                postInvalidate()
            }
            if (!valueAnimator!!.isRunning) {
                valueAnimator!!.start()
            }
        }
    }

    private fun stopAnim() {
        valueAnimator?.cancel()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnim()
        valueAnimator = null
    }
}