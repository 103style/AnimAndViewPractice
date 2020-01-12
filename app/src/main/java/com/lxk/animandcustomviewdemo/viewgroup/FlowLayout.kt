package com.lxk.animandcustomviewdemo.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * @author https://github.com/103style
 * @date 2020/1/12 17:34
 */
class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    /**
     * 提取边距
     */
    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)

        //记录每一行的宽高
        var lineWidth = 0
        var lineHeight = 0
        //记录整个FlowLayout内容所占高度
        var height = 0
        var width = 0

        //计算内容的宽高
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val layoutParams = child.layoutParams as MarginLayoutParams
            //int cW = layoutParams.leftMargin + layoutParams.rightMargin + child.getMeasuredWidth();
            val cWidth = layoutParams.marginEnd + layoutParams.marginStart + child.measuredWidth
            val cHeight = layoutParams.topMargin + layoutParams.bottomMargin + child.measuredHeight


            if (cWidth + lineWidth > measureWidth) {
                //超出剩余宽度
                //计算当前行的宽高
                width = Math.max(width, lineWidth)
                height += lineHeight

                //新的一行的占有宽高
                lineHeight = cHeight
                lineWidth = cWidth
            } else {
                //未超出宽度 计算现有行的宽高
                lineHeight = Math.max(lineHeight, cHeight)
                lineWidth += cWidth
            }

            //因为我们在超出宽度之后才新增的高度
            //因为最后一行确认是没有超过宽度的，所以需要在增加最后一行的高度
            if (i + 1 == childCount) {
                height += lineHeight
                width = Math.max(lineWidth, width)
            }
        }

        //根据宽高的限制符 来设置对应的宽高
        setMeasuredDimension(if (measureWidthMode == MeasureSpec.EXACTLY) measureWidth else width,
                if (measureHeightMode == MeasureSpec.EXACTLY) measureHeight else height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        //累加当前行的行 宽高
        var lineWidth = 0
        var lineHeight = 0
        //当前添加元素的右上角坐标的
        var top = 0
        var left = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val layoutParams = child.layoutParams as MarginLayoutParams
            //子元素占据的宽高L
            //int cW = layoutParams.leftMargin + layoutParams.rightMargin + child.getMeasuredWidth();
            val cWidth = layoutParams.marginEnd + layoutParams.marginStart + child.measuredWidth
            val cHeight = layoutParams.topMargin + layoutParams.bottomMargin + child.measuredHeight

            if (lineWidth + cWidth > measuredWidth) {
                //重置为 开始到下一行的最左侧
                left = 0
                top += lineHeight
                //计算当前View 的 right 和 bottom
                lineWidth = cWidth
                lineHeight = cHeight
            } else {
                lineWidth += cWidth
                lineHeight = Math.max(lineHeight, cHeight)
            }

            //子元素内容对应的坐标
            val ll = left + layoutParams.leftMargin
            val lt = top + layoutParams.topMargin
            val lr = ll + child.measuredWidth
            val lb = lt + child.measuredHeight
            child.layout(ll, lt, lr, lb)

            //更新left的值
            left += cWidth
        }
    }
}
