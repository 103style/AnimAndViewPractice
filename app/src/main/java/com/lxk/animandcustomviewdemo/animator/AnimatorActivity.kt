package com.lxk.animandcustomviewdemo.animator

import android.animation.*
import android.animation.Animator.AnimatorListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.Toast
import com.lxk.animandcustomviewdemo.BaseClickActivity
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.animator.evaluator.ReverseEvaluator
import com.lxk.animandcustomviewdemo.animator.interpolator.ReverseInterpolator

/**
 * @author https://github.com/103style
 * @date 2020/1/10 11:27
 * 属性动画
 */
class AnimatorActivity : BaseClickActivity() {

    private val TAG: String = AnimatorActivity::class.java.name
    private var show: Button? = null
    private var layoutParams: ViewGroup.LayoutParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animator)

        show = findViewById(R.id.bt_demo)

        setClickListener(
                R.id.bt_reset,
                R.id.bt_translate,
                R.id.bt_translate_with_reverse_interpolator,
                R.id.bt_evaluator_test,
                R.id.bt_reserve_evaluator,
                R.id.bt_argb_evaluator,
                R.id.bt_rotate,
                R.id.bt_scale,
                R.id.bt_alpha,
                R.id.bt_set,
                R.id.bt_demo
        )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            layoutParams = show!!.layoutParams
        }
    }

    override fun onClick(v: View) {
        var animator: Animator? = null
        when (v.id) {
            R.id.bt_reset -> {
                show!!.layoutParams = layoutParams
                show!!.text = getString(R.string.demo_view)
            }
            R.id.bt_translate -> animator = getTranslateAnim()
            R.id.bt_translate_with_reverse_interpolator -> animator = getTranslateWithInterpolatorAnim()
            R.id.bt_evaluator_test -> testEvaluator()
            R.id.bt_reserve_evaluator -> reverseEvaluator()
            R.id.bt_argb_evaluator -> animator = getArgbAnim()
            R.id.bt_alpha -> animator = getAlphaObjectAnim()
            R.id.bt_rotate -> animator = getRotateObjectAnim()
            R.id.bt_scale -> animator = getScaleObjectAnim()
            R.id.bt_set -> animator = getAnimSet()
            R.id.bt_demo -> Toast.makeText(this, "don't touch me!", Toast.LENGTH_SHORT).show()
            else -> {
            }

        }
        if (animator != null) {
            animator.addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    Log.e(TAG, "onAnimationStart: ")
                }

                override fun onAnimationEnd(animation: Animator) {
                    Log.e(TAG, "onAnimationEnd: ")
                }

                override fun onAnimationCancel(animation: Animator) {
                    Log.e(TAG, "onAnimationCancel: ")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    Log.e(TAG, "onAnimationRepeat: ")
                }
            })
            animator.start()
        }
    }

    private fun getTextUpdateAnim(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(0, 1000)
        valueAnimator.duration = 2000
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            show!!.text == value.toString()
        }
        return valueAnimator
    }

    private fun reverseEvaluator() {
        //返回从 1000 到 0 的Integer值
        val animator = getTextUpdateAnim()
        animator.setEvaluator(ReverseEvaluator())
        animator.start()
    }

    private fun testEvaluator() {
        //返回从 0 到 1000 的Integer值
        val animator = getTextUpdateAnim()
        animator.setEvaluator(IntEvaluator())
        animator.start()
    }

    /**
     * 颜色渐变
     * 将属性动画对应的 0 - 1 转化为对应的颜色
     */
    private fun getArgbAnim(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(0x00000000, 0xFFFFFFFF.toInt(), 0xFFD81B60.toInt())
        valueAnimator.duration = 2000
        valueAnimator.setEvaluator(ArgbEvaluator())
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val curValue = animation.animatedValue as Int
            show!!.setBackgroundColor(curValue)
        }
        return valueAnimator
    }

    /**
     * 位移动画
     */
    private fun getTranslateAnim(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(show!!.top, show!!.bottom)
        valueAnimator.duration = 2000
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            show!!.layout(show!!.left, value,
                    show!!.right,
                    value + show!!.height)
        }
        return valueAnimator
    }

    /**
     * 设置翻转插值器
     */
    private fun getTranslateWithInterpolatorAnim(): Animator {
        val valueAnimator = getTranslateAnim()
        valueAnimator.interpolator = ReverseInterpolator()
        return valueAnimator
    }

    /**
     * 透明度变化
     */
    private fun getAlphaObjectAnim(): Animator {
        val objectAnimator = ObjectAnimator.ofFloat(show!!, "alpha", 1F, 0F, 1F)
        objectAnimator.duration = 2000
        objectAnimator.repeatCount = 1
        objectAnimator.repeatMode = ValueAnimator.RESTART
        objectAnimator.interpolator = AccelerateInterpolator()
        return objectAnimator
    }

    /**
     * 旋转
     */
    private fun getRotateObjectAnim(): ObjectAnimator {
        val objectAnimator = ObjectAnimator.ofFloat(show!!, "rotation", 0F, 360F)
        objectAnimator.duration = 2000
        objectAnimator.interpolator = AccelerateInterpolator()
        return objectAnimator
    }

    /**
     * 放缩
     */
    private fun getScaleObjectAnim(): Animator {
        val objectAnimator = ObjectAnimator.ofFloat(show!!, "scaleX", 0F, 3F, 1F)
        objectAnimator.duration = 2000
        return objectAnimator
    }

    /**
     * 动画集合
     */
    private fun getAnimSet(): Animator {
        val animatorSet = AnimatorSet()
        animatorSet.play(getRotateObjectAnim())
                .with(getArgbAnim())
                .with(getTextUpdateAnim())
                .after(getTranslateWithInterpolatorAnim())
                .before(getAlphaObjectAnim())
        animatorSet.duration = 5000
        return animatorSet
    }
}
