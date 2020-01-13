package com.lxk.animandcustomviewdemo.animator.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator

import androidx.appcompat.widget.AppCompatButton

import com.lxk.animandcustomviewdemo.animator.evaluator.CharEvaluator

/**
 * @author https://github.com/103style
 * @date 2020/1/10 17:10
 * 自定义 TypeEvaluator 的测试demo
 */
class TestEvaluatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    override fun performClick(): Boolean {
        //这里会去执行onclickListener
        super.performClick()

        //执行动画 并消耗点击事件
        startAnim()
        return true
    }

    /**
     * 文字内容从A - Z 变化的动画
     */
    private fun startAnim() {
        val valueAnimator = ValueAnimator.ofObject(CharEvaluator(), 'A', 'Z')
        valueAnimator.duration = 3000
        valueAnimator.interpolator = AccelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            text = String.format("Char: %s", animation.animatedValue.toString())
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                text = "CharEvaluatorTest"
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        valueAnimator.start()
    }


}
