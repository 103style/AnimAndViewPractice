package com.lxk.animandcustomviewdemo.animator.evaluator

import android.animation.TypeEvaluator

/**
 * @author https://github.com/103style
 * @date 2020/1/10 14:45
 * <p>
 * 字符变化
 */
class CharEvaluator : TypeEvaluator<Char> {

    override fun evaluate(fraction: Float, startValue: Char, endValue: Char): Char {
        val s = startValue.toInt()
        val e = endValue.toInt()
        val cur = s + (fraction * (e - s)).toInt()
        return cur.toChar()
    }
}
