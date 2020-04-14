package com.lxk.animandcustomviewdemo.drawapi.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/8 14:40
 */
class XfermodeSrcInDemoView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    internal var r: Int = 0
    internal var y: Int = 0
    internal var dx: Int = 0
    private val fillPaint: Paint
    private val mPath = Path()
    private var valueAnimator: ValueAnimator? = null
    //波的振幅
    private var amplitude = 32

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        fillPaint.textSize = Utils.doToPx(context, 40)
        amplitude = Utils.doToPx(context, amplitude).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        r = width / 2
        y = height / 4
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————

        //--------------------------------------------------------------------------------------------------
        //文字和水波纹效果

        //获取水波纹path
        update(y)

        val txt = "公众号 103Tech"
        fillPaint.color = Color.WHITE
        //保持文字可见
        canvas.drawText(txt, (width - fillPaint.measureText(txt)) / 2, y.toFloat(), fillPaint)


        //SRC_IN 保留 水波纹上  和 文字 相交 的地方
        var layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)

        canvas.drawText(txt, (width - fillPaint.measureText(txt)) / 2, y.toFloat(), fillPaint)

        //参考 DrawXfermodeDemoView 中的 18 种模式
        fillPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        fillPaint.color = Color.GREEN
        canvas.drawPath(mPath, fillPaint)

        fillPaint.xfermode = null
        canvas.restoreToCount(layerId)

        //--------------------------------------------------------------------------------------------------
        //圆形 和 水波纹效果

        //获取水波纹path
        update(y * 2)

        fillPaint.color = Color.WHITE
        canvas.drawCircle(
            (width / 2).toFloat(),
            (y * 2).toFloat(),
            (height / 5).toFloat(),
            fillPaint
        )

        //SRC_IN 保留 水波纹上  和 文字 相交 的地方
        layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)

        canvas.drawCircle(
            (width / 2).toFloat(),
            (y * 2).toFloat(),
            (height / 5).toFloat(),
            fillPaint
        )
        //参考 DrawXfermodeDemoView 中的 18 种模式
        fillPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        fillPaint.color = Color.GREEN
        canvas.drawPath(mPath, fillPaint)

        fillPaint.xfermode = null
        canvas.restoreToCount(layerId)

        startAnim()
    }


    private fun update(y: Int) {
        //重置path
        mPath.reset()

        //左右偏移的距离
        val gap = 2 * r
        //移动到坐标的偏移点
        mPath.moveTo((-gap + dx).toFloat(), y.toFloat())
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
    }


    private fun startAnim() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(0, 2 * r)
            valueAnimator!!.duration = 2000
            valueAnimator!!.repeatMode = ValueAnimator.RESTART
            valueAnimator!!.interpolator = LinearInterpolator()
            valueAnimator!!.addUpdateListener { animation ->
                dx = animation.animatedValue as Int
                postInvalidate()
            }
        }
        if (!valueAnimator!!.isRunning) {
            valueAnimator!!.start()
        }
    }

    private fun stopAnim() {
        if (valueAnimator != null) {
            valueAnimator!!.cancel()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnim()
        valueAnimator = null
    }
}
