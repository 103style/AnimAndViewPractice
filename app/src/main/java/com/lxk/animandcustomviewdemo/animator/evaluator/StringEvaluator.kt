package com.lxk.animandcustomviewdemo.animator.evaluator

import android.animation.TypeEvaluator

/**
 * @author https://github.com/103style
 * @date 2020/1/10 14:52
 * 字符串变化
 */
class StringEvaluator(var text: String) : TypeEvaluator<String> {

    private val array: Array<String> = text.split("，".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    private val size: Int = array.size

    override fun evaluate(fraction: Float, startValue: String, endValue: String): String {
        if (0.toFloat().equals(fraction)) {
            return startValue
        } else if (1.toFloat().equals(fraction)) {
            return endValue
        }
        var index = (fraction / (1f / size)).toInt()
        if (index == size) {
            index--
        }
        return array[index]
    }
}