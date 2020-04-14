package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lxk.animandcustomviewdemo.drawapi.Utils

/**
 * @author https://github.com/103style
 * @date 2020/4/14 19:56
 */
class DrawPathEffectDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : View(context, attrs, style) {
    private val strokePaint: Paint
    private val textPaint: Paint
    private var path: Path? = null

    init {
        strokePaint = Utils.initPaint(context, Paint.Style.STROKE)
        strokePaint.strokeWidth = Utils.doToPx(context, 2)
        textPaint = Utils.initPaint(context, Paint.Style.FILL)
        textPaint.strokeWidth = 1f
        textPaint.textSize = Utils.doToPx(context, 12)
        textPaint.color = Color.WHITE
        path = Path()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        val gapW = width / 10
        val gapH = height / 10
        path!!.moveTo(gapW.toFloat(), gapH.toFloat())
        path!!.lineTo((gapW * 4).toFloat(), (gapH / 2).toFloat())
        path!!.lineTo((gapW * 7).toFloat(), gapH.toFloat())
        canvas.drawText("CornerPathEffect：圆角效果", gapW.toFloat(), (gapH / 2).toFloat(), textPaint)
        canvas.drawPath(path!!, strokePaint)

        // CornerPathEffect 在转角处 转化为配置半径对应的圆角
        strokePaint.color = Color.BLUE
        strokePaint.pathEffect = CornerPathEffect(Utils.doToPx(context, 32))
        canvas.drawPath(path!!, strokePaint)
        strokePaint.color = Color.YELLOW
        strokePaint.pathEffect = CornerPathEffect(Utils.doToPx(context, 64))
        canvas.drawPath(path!!, strokePaint)


        //DashPathEffect  虚线效果
        //float intervals[] : 实线虚线依次的长度(数量为2的倍数 一实一虚)   长度要>=2
        //float phase : 开始绘制的偏移值
        strokePaint.pathEffect = DashPathEffect(floatArrayOf(50f, 10f, 30f, 20f), 10f)
        strokePaint.color = Color.CYAN
        path = getPath(gapH * 3 / 2, gapW, gapH)
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("DashPathEffect：虚线效果", path!!, 0f, 0f, textPaint)


        //DiscretePathEffect 离散路径
        // float segmentLength : 切割成多长的线段
        // float deviation : 每条线段的偏移值
        strokePaint.pathEffect = DiscretePathEffect(20f, 10f)
        strokePaint.color = Color.RED
        path = getPath(gapH * 3, gapW, gapH)
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("DiscretePathEffect：离散路径效果", path!!, 0f, 0f, textPaint)


        //PathDashPathEffect——印章路径效果
        // Path shape ： 印章
        // float advance ： 两个印章之间的距离
        // float phase ： 印章的偏移
        // Style style ：印章转角处的样式
        canvas.drawText(
            "PathDashPathEffect：印章路径效果",
            gapW.toFloat(),
            (gapH * 9 / 2).toFloat(),
            textPaint
        )
        //构建印章路径
        val shape = Path()
        shape.moveTo(0f, 20f)
        shape.lineTo(10f, 0f)
        shape.lineTo(20f, 20f)
        shape.close()
        shape.addCircle(0f, 0f, 3f, Path.Direction.CCW)

        //Style.TRANSLATE
        strokePaint.pathEffect =
            PathDashPathEffect(shape, 35f, 0f, PathDashPathEffect.Style.TRANSLATE)
        strokePaint.color = Color.RED
        path = getPath(gapH * 9 / 2, gapW, gapH)
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("Style.TRANSLATE", path!!, 0f, 0f, textPaint)

        //保存平移前的状态
        canvas.save()
        val gap = gapH / 2
        //图层向下平移gap距离
        canvas.translate(0f, gap.toFloat())
        //Style.ROTATE
        strokePaint.pathEffect = PathDashPathEffect(shape, 35f, 0f, PathDashPathEffect.Style.ROTATE)
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("Style.ROTATE", path!!, 0f, 0f, textPaint)

        //图层向下平移gap距离
        canvas.translate(0f, gap.toFloat())
        //Style.MORPH
        strokePaint.pathEffect = PathDashPathEffect(shape, 35f, 0f, PathDashPathEffect.Style.MORPH)
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("Style.MORPH", path!!, 0f, 0f, textPaint)
        //恢复到平移前的状态
        canvas.restore()


        path = getPath(gapH * 7, gapW, gapH)

        //仅应用圆角特效的路径
        val cornerPathEffect = CornerPathEffect(100f)
        strokePaint.pathEffect = cornerPathEffect
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("CornerPathEffect", path!!, 0f, 0f, textPaint)

        //仅应用虚线特效的路径
        canvas.translate(0f, gap.toFloat())
        val dashPathEffect = DashPathEffect(floatArrayOf(2f, 5f, 10f, 10f), 0f)
        strokePaint.pathEffect = dashPathEffect
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("DashPathEffect", path!!, 0f, 0f, textPaint)

        //ComposePathEffect 合并两个特效，有先后顺序的   交集
        //利用ComposePathEffect先应用圆角特效,再应用虚线特效
        canvas.translate(0f, gap.toFloat())
        val composePathEffect = ComposePathEffect(dashPathEffect, cornerPathEffect)
        strokePaint.pathEffect = composePathEffect
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("ComposePathEffect 交集", path!!, 0f, 0f, textPaint)

        // SumPathEffect 是分别对原始路径分别作用第一个特效和第二个特效，然后合并改变之后的路径   并集
        //利用SumPathEffect,分别将圆角特效应用于原始路径,然后将生成的两条特效路径合并
        canvas.translate(0f, gap.toFloat())
        val sumPathEffect = SumPathEffect(cornerPathEffect, dashPathEffect)
        strokePaint.pathEffect = sumPathEffect
        canvas.drawPath(path!!, strokePaint)
        canvas.drawTextOnPath("SumPathEffect 并集", path!!, 0f, 0f, textPaint)
    }


    private fun getPath(startY: Int, gapW: Int, gapH: Int): Path {
        val path = Path()
        path.moveTo(0f, startY.toFloat())
        path.lineTo(gapW.toFloat(), (startY + gapH).toFloat())
        path.lineTo((gapW * 3).toFloat(), startY.toFloat())
        path.lineTo((gapW * 5).toFloat(), (startY + gapH).toFloat())
        path.lineTo((gapW * 7).toFloat(), startY.toFloat())
        path.lineTo((gapW * 9).toFloat(), (startY + gapH).toFloat())
        path.lineTo(width.toFloat(), startY.toFloat())
        return path
    }
}
