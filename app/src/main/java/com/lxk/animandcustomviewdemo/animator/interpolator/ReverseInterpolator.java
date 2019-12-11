package com.lxk.animandcustomviewdemo.animator.interpolator;

import android.view.animation.Interpolator;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 13:32
 * 翻转插值器
 */
public class ReverseInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return 1 - input;
    }
}
