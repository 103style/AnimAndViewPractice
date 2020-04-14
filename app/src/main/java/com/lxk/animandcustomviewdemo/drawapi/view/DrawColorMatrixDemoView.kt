package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 19:55
 *  学习文档：https://blog.csdn.net/harvic880925/article/details/51187277
 */
class DrawColorMatrixDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : View(context, attrs, style) {
    private val fillPaint: Paint
    private val textPaint: Paint
    private val path: Path
    private val bitmap: Bitmap

    init {
        fillPaint = Utils.initPaint(context, Paint.Style.FILL)
        fillPaint.strokeWidth = Utils.doToPx(context, 2)
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.textSize = Utils.doToPx(context, 12)
        textPaint.color = Color.WHITE
        path = Path()
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(255, 255, 255, 255))
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        val gapW = width / 10
        val gapH = height / 10

        fillPaint.color = Color.argb(255, 103, 58, 183)
        val rect = Rect(0, 0, 2 * gapW, gapH)
        draw(canvas, rect, gapH, "原色")

        canvas.save()
        val translateX = gapW * 5 / 2
        val translateY = gapH * 2
        //只保留透明度和蓝色通道
        fillPaint.colorFilter = ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    0f, 0f, 0f, 0f, 0f, //R
                    0f, 0f, 0f, 0f, 0f, //G
                    0f, 0f, 1f, 0f, 0f, //B
                    0f, 0f, 0f, 1f, 0f
                )//A
            )
        )
        canvas.translate(translateX.toFloat(), 0f)
        draw(canvas, rect, gapH, "蓝色通道")

        //只保留透明度和绿色通道
        fillPaint.colorFilter = ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    0f, 0f, 0f, 0f, 0f, //R
                    0f, 1f, 0f, 0f, 0f, //G
                    0f, 0f, 0f, 0f, 0f, //B
                    0f, 0f, 0f, 1f, 0f
                )//A
            )
        )
        canvas.translate(translateX.toFloat(), 0f)
        draw(canvas, rect, gapH, "绿色通道")


        //只保留透明度和红色通道
        fillPaint.colorFilter = ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f, //R
                    0f, 0f, 0f, 0f, 0f, //G
                    0f, 0f, 0f, 0f, 0f, //B
                    0f, 0f, 0f, 1f, 0f
                )//A
            )
        )
        canvas.translate(translateX.toFloat(), 0f)
        draw(canvas, rect, gapH, "红色通道")

        //恢复平移前的状态
        canvas.restore()


        canvas.save()

        //色彩增强1.2倍
        fillPaint.colorFilter = ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0f, 0f, 0f, //R
                    0f, 1.2f, 0f, 0f, 0f, //G
                    0f, 0f, 1.2f, 0f, 0f, //B
                    0f, 0f, 0f, 1f, 0f
                )//A
            )
        )
        canvas.translate(0f, translateY.toFloat())
        draw(canvas, rect, gapH, "RGB * 1.2")


        //色彩增强1.2倍
        fillPaint.colorFilter = ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    0.8f, 0f, 0f, 0f, 0f, //R
                    0f, 0.8f, 0f, 0f, 0f, //G
                    0f, 0f, 0.8f, 0f, 0f, //B
                    0f, 0f, 0f, 1f, 0f
                )//A
            )
        )
        canvas.translate(translateX.toFloat(), 0f)
        draw(canvas, rect, gapH, "RGB * 0.8")


        //黑白
        fillPaint.colorFilter = ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    0.213f, 0.715f, 0.072f, 0f, 0f, //R
                    0.213f, 0.715f, 0.072f, 0f, 0f, //G
                    0.213f, 0.715f, 0.072f, 0f, 0f, //B
                    0f, 0f, 0f, 1f, 0f
                )//A
            )
        )
        canvas.translate(translateX.toFloat(), 0f)
        draw(canvas, rect, gapH, "黑白")


        //绿红反色
        fillPaint.colorFilter = ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    0f, 1f, 0f, 0f, 0f, //R
                    1f, 0f, 0f, 0f, 0f, //G
                    0f, 0f, 1f, 0f, 0f, //B
                    0f, 0f, 0f, 1f, 0f
                )//A
            )
        )
        canvas.translate(translateX.toFloat(), 0f)
        draw(canvas, rect, gapH, "绿红反色")

        //恢复平移前的状态
        canvas.restore()

        //饱和度测试
        colorSaturationChange(canvas, rect, gapH, translateX, translateY)

        //色彩旋转
        colorRotateChange(canvas, rect, gapH, translateX, translateY)
    }


    private fun draw(canvas: Canvas, rect: Rect, gapH: Int, txt: String) {
        canvas.drawRect(rect, fillPaint)
        canvas.drawBitmap(bitmap, 0f, gapH.toFloat(), fillPaint)
        canvas.drawText(txt, 0f, (gapH / 2).toFloat(), textPaint)
    }


    /**
     * 饱和度测试
     */
    private fun colorSaturationChange(
        canvas: Canvas,
        rect: Rect,
        gapH: Int,
        translateX: Int,
        translateY: Int
    ) {
        canvas.save()
        canvas.translate(0f, (translateY * 2).toFloat())
        val mSaturationMatrix = ColorMatrix()

        //设置饱和度为 0.5
        mSaturationMatrix.setSaturation(0.5f)
        fillPaint.colorFilter = ColorMatrixColorFilter(mSaturationMatrix)
        draw(canvas, rect, gapH, "饱和度设为0.5")


        canvas.translate(translateX.toFloat(), 0f)
        //设置饱和度为 1
        mSaturationMatrix.setSaturation(1f)
        fillPaint.colorFilter = ColorMatrixColorFilter(mSaturationMatrix)
        canvas.drawRect(rect, fillPaint)
        canvas.drawBitmap(bitmap, 0f, gapH.toFloat(), fillPaint)
        draw(canvas, rect, gapH, "饱和度设为1")

        canvas.translate(translateX.toFloat(), 0f)
        //设置饱和度为 2
        mSaturationMatrix.setSaturation(2f)
        fillPaint.colorFilter = ColorMatrixColorFilter(mSaturationMatrix)
        draw(canvas, rect, gapH, "饱和度设为2")


        canvas.translate(translateX.toFloat(), 0f)
        //设置饱和度为 5
        mSaturationMatrix.setSaturation(5f)
        fillPaint.colorFilter = ColorMatrixColorFilter(mSaturationMatrix)
        draw(canvas, rect, gapH, "饱和度设为5")
        canvas.restore()
    }


    /**
     * 色彩旋转测试
     */
    private fun colorRotateChange(
        canvas: Canvas,
        rect: Rect,
        gapH: Int,
        translateX: Int,
        translateY: Int
    ) {
        canvas.save()
        canvas.translate(0f, (translateY * 3).toFloat())
        val rotateMatrix = ColorMatrix()

        //颜色旋转-30°
        rotateMatrix.setRotate(0, -30f)
        fillPaint.colorFilter = ColorMatrixColorFilter(rotateMatrix)
        draw(canvas, rect, gapH, "颜色旋转-30°")


        canvas.translate(translateX.toFloat(), 0f)
        //颜色旋转30°
        rotateMatrix.setRotate(0, 30f)
        fillPaint.colorFilter = ColorMatrixColorFilter(rotateMatrix)
        draw(canvas, rect, gapH, "颜色旋转30°")

        canvas.translate(translateX.toFloat(), 0f)
        //颜色旋转60°
        rotateMatrix.setRotate(0, 60f)
        fillPaint.colorFilter = ColorMatrixColorFilter(rotateMatrix)
        draw(canvas, rect, gapH, "颜色旋转60°")


        canvas.translate(translateX.toFloat(), 0f)
        //颜色旋转120°
        rotateMatrix.setRotate(0, 120f)
        fillPaint.colorFilter = ColorMatrixColorFilter(rotateMatrix)
        draw(canvas, rect, gapH, "颜色旋转120°")
        canvas.restore()
    }
}
