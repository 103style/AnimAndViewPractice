package com.lxk.animandcustomviewdemo.animator.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

/**
 * @author https://github.com/103style
 * @date 2019/12/12 10:41
 * 测试验证 PropertyValuesHolder.ofObject
 * 测试属性为 CurText
 */
public class ObjectHolderView extends AppCompatButton {
    public ObjectHolderView(Context context) {
        this(context, null);
    }

    public ObjectHolderView(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.buttonStyle);
    }

    public ObjectHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCurText(String text) {
        setText(text);
    }

    public void setCurChar(Character c) {
        setText(String.valueOf(c));
    }
}
