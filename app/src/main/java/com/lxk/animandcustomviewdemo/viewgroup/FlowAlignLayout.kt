package com.lxk.animandcustomviewdemo.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import android.view.ViewGroup
import androidx.annotation.IntDef
import com.lxk.animandcustomviewdemo.R
import java.util.*

/**
 * @author https://github.com/103style
 * @date 2020/1/12 17:34
 * 在FlowLayout的基础上添加 对齐属性
 */
class FlowAlignLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    /**
     * 对其模式
     */
    private var alignType: Int = 0
    /**
     * 记录每行对应的View的个数
     */
    private val tempParamLists: MutableList<Int>
    /**
     * 每行空余的宽度
     */
    private val gapLists: MutableList<Int>

    init {
        tempParamLists = ArrayList()
        gapLists = ArrayList()
        initAttrs(context, attrs)
    }

    fun setAlignType(@AlignMode alignType: Int) {
        this.alignType = alignType
        if (tempParamLists.size != 0) {
            layoutWithAlign()
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.FlowAlignLayout)
        alignType = ta.getInt(R.styleable.FlowAlignLayout_align_type, 0)
        ta.recycle()
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = getSize(widthMeasureSpec)
        val measureHeight = getSize(heightMeasureSpec)
        //布局内容的总宽高
        var contentWidth = 0
        var contentHeight = 0
        //每行的宽高
        var lineWidth = 0
        var lineHeight = 0
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            //测量 然后获取边距
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val layoutParams = child.layoutParams as MarginLayoutParams

            val childWidth = child.measuredWidth + layoutParams.marginStart + layoutParams.marginEnd
            val childHeight =
                child.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

            if (lineWidth + childWidth > measureWidth) {
                //当即将超出布局宽度时  这里当 childWidth >= measureWidth 也不会有影响
                contentWidth = Math.max(contentWidth, lineWidth)
                contentHeight += lineHeight

                lineWidth = childWidth
                lineHeight = childHeight

            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }

            //上面计算的contentHeight 为已经不能添加内容行数的高度
            //还得加上最后一行的高度
            if (i + 1 == childCount) {
                contentHeight += lineHeight
                contentWidth = Math.max(contentWidth, lineWidth)
            }
        }
        //根据布局的宽高属性设置对应的宽高
        setMeasuredDimension(
            if (getMode(widthMeasureSpec) == EXACTLY) measureWidth else contentWidth,
            if (getMode(heightMeasureSpec) == EXACTLY) measureHeight else contentHeight
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        tempParamLists.clear()
        gapLists.clear()
        val width = measuredWidth
        val childCount = childCount
        //当前行子view已占宽度
        var end = 0
        //每行子view的个数
        var lineChildCount = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val layoutParams = child.layoutParams as MarginLayoutParams
            val cw = layoutParams.marginStart + layoutParams.marginEnd + child.measuredWidth
            //记录每行的的子View的信息
            if (end + cw > width) {
                //记录已经添加的个数 和 剩余的宽度
                gapLists.add(width - end)
                tempParamLists.add(lineChildCount)
                end = cw
                lineChildCount = 0

                //当子item的宽度大于布局的宽度时 直接占一行
                if (cw >= width) {
                    //一行就一个  剩余宽度为0
                    gapLists.add(0)
                    tempParamLists.add(1)
                    //开启新的一行
                    end = 0
                    lineChildCount = 0
                    continue
                }
            } else {
                end += cw
            }
            lineChildCount++

            //处理最后未满一行的子View
            if (i + 1 == childCount) {
                gapLists.add(width - end)
                tempParamLists.add(lineChildCount)
            }
        }
        layoutWithAlign()
    }

    private fun layoutWithAlign() {
        when (alignType) {
            SAME_GAP -> layoutWithSameCap()
            LEFT, RIGHT, CENTER -> layoutWithOtherAlign()
            else -> layoutWithOtherAlign()
        }
    }

    private fun layoutWithOtherAlign() {
        var childIndex = 0
        //布局的上边距的位置
        var top = 0
        for (i in tempParamLists.indices) {
            //获取当前行的个数和剩余空间
            val count = tempParamLists[i]
            val gap = gapLists[i]
            //记录当前行所占的高度
            var lineHeight = 0
            //计算 RIGHT LEFT CENTER 各自对应的布局的 x 起始点
            var left: Int
            when (alignType) {
                RIGHT -> left = gap
                CENTER -> left = gap / 2
                LEFT -> left = 0
                else -> left = 0
            }
            for (j in 0 until count) {
                val child = getChildAt(childIndex)
                val layoutParams = child.layoutParams as MarginLayoutParams
                //计算子view内容所占的  不包括margin 然后布局
                val ll = left + layoutParams.marginStart
                val lt = top + layoutParams.topMargin
                val lr = ll + child.measuredWidth
                val lb = lt + child.measuredHeight
                child.layout(ll, lt, lr, lb)

                //计算子View所占的高度 包括margin
                val h = layoutParams.topMargin + layoutParams.bottomMargin + child.measuredHeight
                //获取当前行所占高度
                lineHeight = Math.max(lineHeight, h)
                //计算一个子View布局的左起始点
                left = lr + layoutParams.marginEnd
                //子view索引加1
                childIndex++
            }
            //计算已占用的高度
            top += lineHeight
        }
    }

    private fun layoutWithSameCap() {
        var childIndex = 0
        //布局的上边距的位置
        var top = 0
        for (i in tempParamLists.indices) {
            //获取当前行的个数和剩余空间
            val count = tempParamLists[i]
            var gap = gapLists[i]
            //计算每个间隙的宽度  2个view 有 3 个间隙
            gap = gap / (count + 1)

            //记录当前行所占的高度
            var lineHeight = 0
            //每行布局的 x 起始点
            var left = gap
            for (j in 0 until count) {
                val child = getChildAt(childIndex)
                val layoutParams = child.layoutParams as MarginLayoutParams
                //计算子view内容所占的  不包括margin 然后布局
                val ll = left + layoutParams.marginStart
                val lt = top + layoutParams.topMargin
                val lr = ll + child.measuredWidth
                val lb = lt + child.measuredHeight
                child.layout(ll, lt, lr, lb)
                //计算子View所占的高度 包括margin
                val h = layoutParams.topMargin + layoutParams.bottomMargin + child.measuredHeight
                //获取当前行所占高度
                lineHeight = Math.max(lineHeight, h)
                //计算一个子View布局的左起始点  得加上一个间隙宽度
                left = lr + layoutParams.marginEnd + gap
                //子view索引加1
                childIndex++
            }
            //计算已占用的高度
            top += lineHeight
        }
    }

    @IntDef(LEFT, RIGHT, CENTER, SAME_GAP)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class AlignMode

    companion object {

        const val LEFT = 0
        const val RIGHT = 1
        const val CENTER = 2
        const val SAME_GAP = 3
    }

}
