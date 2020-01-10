package com.lxk.animandcustomviewdemo.animator.evaluator

import android.animation.TypeEvaluator

/**
 * @author https://github.com/103style
 * @date 2020/1/10 14:50
 * 将属性动画的0 - 1值 整形化为起始和结束之间的值
 */
class ReverseEvaluator : TypeEvaluator<Int> {
    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        return (endValue - fraction * (endValue - startValue)).toInt()
    }
}