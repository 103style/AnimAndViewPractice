package com.lxk.animandcustomviewdemo.animator.interpolator

import android.view.animation.Interpolator

/**
 * @author https://github.com/103style
 * @date 2020/1/10 15:02
 * 翻转插值器
 */
class ReverseInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return 1 - input
    }
}
