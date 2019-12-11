package com.lxk.animandcustomviewdemo.animator.evaluator;

import android.animation.TypeEvaluator;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 13:49
 * 将属性动画的值 整形化
 */
public class ReverseEvaluator implements TypeEvaluator<Integer> {

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        return (int) (endValue - fraction * (endValue - startValue));
    }
}
