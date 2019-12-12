package com.lxk.animandcustomviewdemo.animator.evaluator;

import android.animation.TypeEvaluator;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 17:53
 * 字符串变化
 */
public class StringEvaluator implements TypeEvaluator<String> {
    /**
     * 默认文字
     */
    private String text;

    private int size;

    private String[] array;

    public StringEvaluator(String text) {
        this.text = text;
        array = text.split("，");
        size = array.length;
    }

    @Override
    public String evaluate(float fraction, String startValue, String endValue) {
        if (Float.valueOf(0).equals(fraction)) {
            return startValue;
        } else if (Float.valueOf(1).equals(fraction)) {
            return endValue;
        }
        int index = (int) (fraction / (1f / (size)));
        if (index == size) {
            index--;
        }
        return array[index];
    }
}
