package com.lxk.animandcustomviewdemo.animator.view

import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatButton

/**
 * @author https://github.com/103style
 * @date 2020/1/10 16:56
 * 验证 自定义属性 的 属性动画 demo
 */
class CustomPropertyAnimView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var curText: Int = 0


    fun setCurText(curText: Int) {
        text = String.format("Char: %s", curText.toChar())
    }

    override fun performClick(): Boolean {
        //这里会去执行onclickListener
        super.performClick()

        //执行动画 并消耗点击事件
        startObjectAnim()
        return true
    }


    /**
     * 自定义属性 的 属性动画  需要实现 "set属性名" 方法
     */
    private fun startObjectAnim() {
        val objectAnimator = ObjectAnimator.ofInt(this, "curText", 'A'.toInt(), 'Z'.toInt())
        objectAnimator.setEvaluator(IntEvaluator())
        objectAnimator.duration = 3000
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                text = "CustomPropertyAnim"
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

        })
        objectAnimator.start()
    }
}
