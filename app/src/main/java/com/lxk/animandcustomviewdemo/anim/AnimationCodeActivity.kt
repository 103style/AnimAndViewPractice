package com.lxk.animandcustomviewdemo.anim

import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.Button
import android.widget.Toast
import com.lxk.animandcustomviewdemo.BaseClickActivity
import com.lxk.animandcustomviewdemo.R

/**
 * @author https://github.com/103style
 * @date 2020/1/10 11:17
 * <p>
 * 代码实现补间动画
 */
class AnimationCodeActivity : BaseClickActivity() {

    private var show: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_xml)

        show = findViewById(R.id.bt_demo)

        setClickListener(
                R.id.bt_translate,
                R.id.bt_rotate,
                R.id.bt_scale,
                R.id.bt_alpha,
                R.id.bt_set,
                R.id.bt_demo
        )
    }

    override fun onClick(v: View) {
        var animation: Animation? = null
        when (v.id) {
            R.id.bt_translate -> animation = getTranslateAnim()
            R.id.bt_alpha -> animation = getAlphaAnim()
            R.id.bt_rotate -> animation = getRotateAnim()
            R.id.bt_scale -> animation = getScaleAnim()
            R.id.bt_set ->
                animation = getAnimSet()
            R.id.bt_demo ->
                Toast.makeText(this, "don't touch me!", Toast.LENGTH_SHORT).show()
            else -> {
            }
        }
        if (animation != null) {
            show!!.startAnimation(animation)
        }
    }

    /**
     * 位移动画
     */
    private fun getTranslateAnim(): Animation {
        val translateAnimation = TranslateAnimation(-200F, 200F, -200F, 200F)
        //在原有基础上增加一次  即一共2次
        translateAnimation.repeatCount = 1
        translateAnimation.repeatMode = Animation.REVERSE
        translateAnimation.duration = 2000
        translateAnimation.interpolator = AccelerateDecelerateInterpolator()
        return translateAnimation
    }

    /**
     * 透明度变化
     */
    private fun getAlphaAnim(): Animation {
        val alphaAnimation = AlphaAnimation(0.1f, 1.0f)
        //在原有基础上增加一次  即一共2次
        alphaAnimation.repeatCount = 1
        alphaAnimation.repeatMode = Animation.REVERSE
        alphaAnimation.duration = 2000
        alphaAnimation.interpolator = LinearInterpolator()
        return alphaAnimation
    }


    /**
     * 旋转
     */
    private fun getRotateAnim(): Animation {
        val rotateAnimation = RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        //在原有基础上增加一次  即一共2次
        rotateAnimation.repeatCount = 1
        rotateAnimation.repeatMode = Animation.REVERSE
        rotateAnimation.duration = 2000
        rotateAnimation.interpolator = OvershootInterpolator()
        return rotateAnimation
    }

    /**
     * 放缩
     */
    private fun getScaleAnim(): Animation {
        val scaleAnimation = ScaleAnimation(0.1f, 1.2f,
                0.1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        //在原有基础上增加一次  即一共2次
        scaleAnimation.repeatCount = 1
        scaleAnimation.repeatMode = Animation.REVERSE
        scaleAnimation.duration = 2000
        scaleAnimation.interpolator = OvershootInterpolator()
        return scaleAnimation
    }

    /**
     * 动画集合
     */
    private fun getAnimSet(): Animation {
        val animationSet = AnimationSet(false)
        animationSet.addAnimation(getAlphaAnim())
        animationSet.addAnimation(getScaleAnim())
        animationSet.addAnimation(getRotateAnim())
        animationSet.addAnimation(getRotateAnim())
        animationSet.interpolator = DecelerateInterpolator()
        return animationSet
    }
}
