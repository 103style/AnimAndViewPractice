package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 20:06
 * doc:https://blog.csdn.net/harvic880925/article/details/51264653
 * doc:https://developer.android.com/reference/android/graphics/PorterDuff.Mode.html
 */
class XfermodeGuaGuaKaView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val fillPaint: Paint
    private val strokePaint: Paint
    private val textPaint: Paint
    private var src: Bitmap? = null
    private var dst: Bitmap? = null
    private val mPath: Path
    private var region: Region? = null
    private var perX: Int = 0
    private var perY: Int = 0

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        strokePaint = Utils.initPaint(context, Paint.Style.STROKE)
        strokePaint.strokeWidth = Utils.doToPx(context, 16)
        strokePaint.color = Color.WHITE
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.textSize = Utils.doToPx(context, 20)
        mPath = Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        src = BitmapFactory.decodeResource(resources, R.drawable.guaguaka)
        val matrix = Matrix()
        val scale = width * 1.0f / src!!.width
        matrix.setScale(scale, scale)
        src = Bitmap.createBitmap(src!!, 0, 0, src!!.width, src!!.height, matrix, true)
        createDst(src!!)
        region = Region(0, 0, src!!.width, src!!.height)
    }

    private fun createDst(src: Bitmap) {
        dst = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val txt = "想中奖，就来关注公众号 103Tech ！"
        val canvas = Canvas(dst!!)
        canvas.drawText(
            txt,
            (dst!!.width - textPaint.measureText(txt)) / 2,
            (dst!!.height / 2).toFloat(),
            textPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (region!!.contains(x, y)) {
                    mPath.moveTo(x.toFloat(), y.toFloat())
                    perX = x
                    perY = y
                }
                if (y > src!!.height + Utils.doToPx(context, 8)) {
                    val group = parent as ViewGroup
                    group.removeView(this)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> if (region!!.contains(x, y)) {
                mPath.quadTo(perX.toFloat(), perY.toFloat(), x.toFloat(), y.toFloat())
                perX = x
                perY = y
            }
            else -> {
            }
        }

        if (region!!.contains(x, y)) {
            postInvalidate()
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.WHITE)
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————


        canvas.drawBitmap(dst!!, 0f, 0f, fillPaint)

        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)

        //使用 SRC_OUT 模式  去掉 src 和  mPath 重叠的部分
        canvas.drawPath(mPath, strokePaint)
        strokePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        //类似google示例中dst src 的完全重叠
        canvas.drawBitmap(src!!, 0f, 0f, strokePaint)
        //还原图层
        strokePaint.xfermode = null
        canvas.restoreToCount(layerId)
    }
}
