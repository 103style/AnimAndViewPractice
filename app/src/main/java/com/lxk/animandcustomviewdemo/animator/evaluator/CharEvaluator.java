package com.lxk.animandcustomviewdemo.animator.evaluator;

import android.animation.TypeEvaluator;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 15:58
 * <p>
 * 字符变化
 */
public class CharEvaluator implements TypeEvaluator<Character> {

    @Override
    public Character evaluate(float fraction, Character startValue, Character endValue) {
        int s = startValue;
        int e = endValue;
        int cur = s + (int) (fraction * (e - s));
        return (char) cur;
    }
}
