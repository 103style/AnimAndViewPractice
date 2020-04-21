package com.lxk.animandview.animator.evaluator;

import android.animation.TypeEvaluator;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 13:49
 * 将属性动画的0 - 1值 整形化为起始和结束之间的值
 */
public class ReverseEvaluator implements TypeEvaluator<Integer> {

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        return (int) (endValue - fraction * (endValue - startValue));
    }
}
