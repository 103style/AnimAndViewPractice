package com.lxk.animandcustomviewdemo.drawapi.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @author https://github.com/103style
 * @date 2020/4/5 16:47
 */
class RangeDemo @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    private val fillPaint: Paint
    private val strokePaint: Paint


    init {
        fillPaint = initPaint(Paint.Style.FILL)
        val scale = resources.displayMetrics.density
        val size = (14 * scale + 0.5f).toInt()
        fillPaint.textSize = size.toFloat()
        strokePaint = initPaint(Paint.Style.STROKE)
    }

    private fun initPaint(style: Paint.Style): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        //设置画笔颜色
        paint.color = Color.RED
        //设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        paint.style = style
        //设置画笔宽度
        paint.strokeWidth = 6f
        return paint
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //设置画布背景
        canvas.drawColor(Color.argb(128, 0, 0, 0))

        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        val gapW = width / 10
        val gapH = height / 10

        val region = Region(gapW, gapH, gapW * 2, gapH * 2)
        drawRegion(canvas, region, fillPaint)
        //绘制一个新的区域
        region.set(gapW * 3, gapH, gapW * 4, gapH * 2)
        drawRegion(canvas, region, strokePaint)


        //构造一个椭圆路径
        val ovalPath = Path()
        val rect =
            RectF((gapW * 5).toFloat(), gapH.toFloat(), (gapW * 8).toFloat(), (gapH * 2).toFloat())
        ovalPath.addOval(rect, Path.Direction.CCW)
        //SetPath时,传入一个比椭圆区域小的矩形区域,让其取交集
        val rgn = Region()
        rgn.setPath(ovalPath, Region(gapW * 5, gapH, gapW * 7, gapH * 2))
        //画出路径
        drawRegion(canvas, rgn, fillPaint)


        //region 的合并交叉等操作
        val thirdOneHor = Region(gapW / 2, gapH * 3, gapW * 3, gapH * 7 / 2)
        val thirdOneVer = Region(gapW * 3 / 2, gapH * 5 / 2, gapW * 2, gapH * 4)
        regionOp(canvas, thirdOneHor, thirdOneVer, Region.Op.INTERSECT)
        canvas.drawText("OP.INTERSECT", (gapW / 2).toFloat(), (gapH * 9 / 2).toFloat(), fillPaint)

        val thirdTwoHor = Region(gapW * 7 / 2, gapH * 3, gapW * 6, gapH * 7 / 2)
        val thirdTwoVer = Region(gapW * 9 / 2, gapH * 5 / 2, gapW * 5, gapH * 4)
        regionOp(canvas, thirdTwoHor, thirdTwoVer, Region.Op.DIFFERENCE)
        canvas.drawText(
            "OP.DIFFERENCE",
            (gapW * 7 / 2).toFloat(),
            (gapH * 9 / 2).toFloat(),
            fillPaint
        )

        val thirdThreeHor = Region(gapW * 13 / 2, gapH * 3, gapW * 9, gapH * 7 / 2)
        val thirdThreeVer = Region(gapW * 15 / 2, gapH * 5 / 2, gapW * 8, gapH * 4)
        regionOp(canvas, thirdThreeHor, thirdThreeVer, Region.Op.UNION)
        canvas.drawText("OP.UNION", (gapW * 13 / 2).toFloat(), (gapH * 9 / 2).toFloat(), fillPaint)


        //region 的合并交叉等操作
        val forthOneHor = Region(gapW / 2, gapH * 6, gapW * 3, gapH * 13 / 2)
        val forthOneVer = Region(gapW * 3 / 2, gapH * 11 / 2, gapW * 2, gapH * 7)
        regionOp(canvas, forthOneHor, forthOneVer, Region.Op.XOR)
        canvas.drawText("OP.XOR", (gapW / 2).toFloat(), (gapH * 15 / 2).toFloat(), fillPaint)

        val forthTwoHor = Region(gapW * 7 / 2, gapH * 6, gapW * 6, gapH * 13 / 2)
        val forthTwoVer = Region(gapW * 9 / 2, gapH * 11 / 2, gapW * 5, gapH * 7)
        regionOp(canvas, forthTwoHor, forthTwoVer, Region.Op.REPLACE)
        canvas.drawText(
            "OP.REPLACE",
            (gapW * 7 / 2).toFloat(),
            (gapH * 15 / 2).toFloat(),
            fillPaint
        )

        val forthThreeHor = Region(gapW * 13 / 2, gapH * 6, gapW * 9, gapH * 13 / 2)
        val forthThreeVer = Region(gapW * 15 / 2, gapH * 11 / 2, gapW * 8, gapH * 7)
        regionOp(canvas, forthThreeHor, forthThreeVer, Region.Op.REVERSE_DIFFERENCE)
        canvas.drawText(
            "OP.REVERSE_DIFFERENCE",
            (gapW * 13 / 2).toFloat(),
            (gapH * 15 / 2).toFloat(),
            fillPaint
        )

    }

    private fun regionOp(canvas: Canvas, a: Region, b: Region, op: Region.Op) {
        drawRegion(canvas, a, strokePaint)
        drawRegion(canvas, b, strokePaint)
        a.op(b, op)
        drawRegion(canvas, a, fillPaint)
    }

    private fun drawRegion(canvas: Canvas, region: Region, paint: Paint) {
        //矩形集枚举区域
        val ri = RegionIterator(region)
        val r = Rect()
        while (ri.next(r)) {
            canvas.drawRect(r, paint)
        }
    }

}
