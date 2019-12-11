package com.lxk.animandcustomviewdemo.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;

import com.lxk.animandcustomviewdemo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/12/10 15:06
 * 在FlowLayout的基础上添加 对其属性
 */
public class FlowAlignLayout extends ViewGroup {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int CENTER = 2;
    public static final int SAME_GAP = 3;
    /**
     * 对其模式
     */
    private int alignType;
    /**
     * 记录每行对应的View的个数
     */
    private List<Integer> tempParamLists;
    /**
     * 每行空余的宽度
     */
    private List<Integer> gapLists;

    public FlowAlignLayout(Context context) {
        this(context, null);
    }

    public FlowAlignLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowAlignLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tempParamLists = new ArrayList<>();
        gapLists = new ArrayList<>();
        initAttrs(context, attrs);
    }

    public int getAlignType() {
        return alignType;
    }

    public void setAlignType(@AlignMode int alignType) {
        this.alignType = alignType;
        if (tempParamLists.size() != 0) {
            layoutWithAlign();
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowAlignLayout);
        alignType = ta.getInt(R.styleable.FlowAlignLayout_align_type, 0);
        ta.recycle();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        //布局内容的总宽高
        int contentWidth = 0, contentHeight = 0;
        //每行的宽高
        int lineWidth = 0, lineHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //测量 然后获取边距
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + layoutParams.getMarginStart() + layoutParams.getMarginEnd();
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (lineWidth + childWidth > measureWidth) {
                //当即将超出布局宽度时  这里当 childWidth >= measureWidth 也不会有影响
                contentWidth = Math.max(contentWidth, lineWidth);
                contentHeight += lineHeight;

                lineWidth = childWidth;
                lineHeight = childHeight;

            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            //上面计算的contentHeight 为已经不能添加内容行数的高度
            //还得加上最后一行的高度
            if (i + 1 == childCount) {
                contentHeight += lineHeight;
                contentWidth = Math.max(contentWidth, lineWidth);
            }
        }
        //根据布局的宽高属性设置对应的宽高
        setMeasuredDimension(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ? measureWidth : contentWidth,
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY ? measureHeight : contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        tempParamLists.clear();
        gapLists.clear();
        int width = getMeasuredWidth();
        int childCount = getChildCount();
        //当前行子view已占宽度
        int end = 0;
        //每行子view的个数
        int lineChildCount = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int cw = layoutParams.getMarginStart() + layoutParams.getMarginEnd() + child.getMeasuredWidth();
            //记录每行的的子View的信息
            if (end + cw > width) {
                //记录已经添加的个数 和 剩余的宽度
                gapLists.add(width - end);
                tempParamLists.add(lineChildCount);
                end = cw;
                lineChildCount = 0;

                //当子item的宽度大于布局的宽度时 直接占一行
                if (cw >= width) {
                    //一行就一个  剩余宽度为0
                    gapLists.add(0);
                    tempParamLists.add(1);
                    //开启新的一行
                    end = 0;
                    lineChildCount = 0;
                    continue;
                }
            } else {
                end += cw;
            }
            lineChildCount++;

            //处理最后未满一行的子View
            if (i + 1 == childCount) {
                gapLists.add(width - end);
                tempParamLists.add(lineChildCount);
            }
        }
        layoutWithAlign();
    }

    private void layoutWithAlign() {
        switch (alignType) {
            case SAME_GAP:
                layoutWithSameCap();
                break;
            case LEFT:
            case RIGHT:
            case CENTER:
            default:
                layoutWithOtherAlign();
                break;
        }
    }

    private void layoutWithOtherAlign() {
        int childIndex = 0;
        //布局的上边距的位置
        int top = 0;
        for (int i = 0; i < tempParamLists.size(); i++) {
            //获取当前行的个数和剩余空间
            int count = tempParamLists.get(i);
            int gap = gapLists.get(i);
            //记录当前行所占的高度
            int lineHeight = 0;
            //计算 RIGHT LEFT CENTER 各自对应的布局的 x 起始点
            int left;
            switch (alignType) {
                case RIGHT:
                    left = gap;
                    break;
                case CENTER:
                    left = gap / 2;
                    break;
                case LEFT:
                default:
                    left = 0;
                    break;
            }
            for (int j = 0; j < count; j++) {
                View child = getChildAt(childIndex);
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                //计算子view内容所占的  不包括margin 然后布局
                int ll = left + layoutParams.getMarginStart();
                int lt = top + layoutParams.topMargin;
                int lr = ll + child.getMeasuredWidth();
                int lb = lt + child.getMeasuredHeight();
                child.layout(ll, lt, lr, lb);

                //计算子View所占的高度 包括margin
                int h = layoutParams.topMargin + layoutParams.bottomMargin + child.getMeasuredHeight();
                //获取当前行所占高度
                lineHeight = Math.max(lineHeight, h);
                //计算一个子View布局的左起始点
                left = lr + layoutParams.getMarginEnd();
                //子view索引加1
                childIndex++;
            }
            //计算已占用的高度
            top += lineHeight;
        }
    }


    private void layoutWithSameCap() {
        int childIndex = 0;
        //布局的上边距的位置
        int top = 0;
        for (int i = 0; i < tempParamLists.size(); i++) {
            //获取当前行的个数和剩余空间
            int count = tempParamLists.get(i);
            int gap = gapLists.get(i);
            //计算每个间隙的宽度  2个view 有 3 个间隙
            gap = gap / (count + 1);

            //记录当前行所占的高度
            int lineHeight = 0;
            //每行布局的 x 起始点
            int left = gap;
            for (int j = 0; j < count; j++) {
                View child = getChildAt(childIndex);
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                //计算子view内容所占的  不包括margin 然后布局
                int ll = left + layoutParams.getMarginStart();
                int lt = top + layoutParams.topMargin;
                int lr = ll + child.getMeasuredWidth();
                int lb = lt + child.getMeasuredHeight();
                child.layout(ll, lt, lr, lb);
                //计算子View所占的高度 包括margin
                int h = layoutParams.topMargin + layoutParams.bottomMargin + child.getMeasuredHeight();
                //获取当前行所占高度
                lineHeight = Math.max(lineHeight, h);
                //计算一个子View布局的左起始点  得加上一个间隙宽度
                left = lr + layoutParams.getMarginEnd() + gap;
                //子view索引加1
                childIndex++;
            }
            //计算已占用的高度
            top += lineHeight;
        }
    }


    @IntDef({LEFT, RIGHT, CENTER, SAME_GAP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlignMode {
    }

}
