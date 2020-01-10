package com.lxk.animandcustomviewdemo.recyclerview

import android.graphics.*
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author https://github.com/103style
 * @date  2020/1/10 15:26
 */
class TestItemDecoration : RecyclerView.ItemDecoration() {
    private val TAG: String = TestItemDecoration::class.java.name
    private val p: Path = Path()

    private val mPaint: Paint = Paint()

    init {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10F
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        Log.e(TAG, "onDraw: ")
        c.save()
        val cCount = parent.childCount
        val layoutManager = parent.layoutManager ?: return
        for (i in 0 until cCount - 1) {
            val child = parent.getChildAt(i)
            val left = layoutManager.getLeftDecorationWidth(child)
            val x = left / 2f
            val y = child.top + child.height / 2f
            val radius = Math.min(left, child.height) / 2f
            c.drawCircle(x, y, radius, mPaint)
            p.reset()
            p.moveTo(child.left.toFloat(), child.top.toFloat())
            p.lineTo(child.right.toFloat(), child.top.toFloat())
            p.lineTo(child.right.toFloat(), child.bottom.toFloat())
            p.lineTo(child.left.toFloat(), child.bottom.toFloat())
            p.close()
            c.drawPath(p, mPaint)
        }
        c.restore()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        Log.e(TAG, "onDrawOver: ")
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        Log.e(TAG, "getItemOffsets: ")
        outRect.bottom = 10
        outRect.top = 10
        outRect.right = 10
        outRect.left = 10
    }

}
