package com.lxk.animandcustomviewdemo.animator.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

/**
 * @author https://github.com/103style
 * @date 2020/1/10 16:32
 * 测试验证 PropertyValuesHolder.ofObject
 * 测试属性为 CurText
 */
class ObjectHolderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    fun setCurText(text: String) {
        setText(text)
    }

    fun setCurChar(c: Char?) {
        text = c.toString()
    }
}
