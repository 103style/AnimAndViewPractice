package com.lxk.animandcustomviewdemo.animator

import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.lxk.animandcustomviewdemo.BaseClickActivity
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.animator.evaluator.CharEvaluator
import com.lxk.animandcustomviewdemo.animator.evaluator.StringEvaluator
import java.util.*

/**
 * @author https://github.com/103style
 * @date 2020/1/10 14:11
 */
class PropertyValuesHolderAndKeyFrameActivity : BaseClickActivity() {

    private var show: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holder_keyframe)

        show = findViewById(R.id.show)

        setClickListener(
                R.id.property_values_holder_float,
                R.id.property_values_holder_object,
                R.id.keyframe_test,
                R.id.keyframe_ofKeyframe_test

        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.property_values_holder_float -> propertyValuesHolderFloatTest()
            R.id.property_values_holder_object -> propertyValuesHolderObjectTest()
            R.id.keyframe_test -> keyframeTest()
            R.id.keyframe_ofKeyframe_test -> keyframeOfKeyframeTest()
            else -> {

            }
        }
    }

    /**
     * PropertyValuesHolder.ofFloat
     * ObjectAnimator.ofPropertyValuesHolder
     * 使用示例
     */
    private fun propertyValuesHolderFloatTest() {
        val rotationHolder = PropertyValuesHolder.ofFloat("Rotation", 0F, 360F)
        val alphaHolder = PropertyValuesHolder.ofFloat("Alpha", 1F, 0F, 1F)
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(show!!, rotationHolder, alphaHolder)
        objectAnimator.duration = 2000
        objectAnimator.start()
    }

    /**
     * PropertyValuesHolder.ofObject
     * ObjectAnimator.ofPropertyValuesHolder
     * 使用示例
     */
    private fun propertyValuesHolderObjectTest() {
        val objectHolder = PropertyValuesHolder.ofObject("CurText", StringEvaluator("在吗？，在！，one hour later!，在吗？，在！，one hour later!，在吗？"), "", "滚!")
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                findViewById<View>(R.id.property_values_holder_object), objectHolder)
        objectAnimator.duration = 6000
        objectAnimator.start()
    }

    /**
     * keyframe使用示例
     * Keyframe 的 第一个参数取值为(0 - 1),即整个动画的起始的一个时间点
     * 第二个参数则为对应的值
     * 即为 指定某一时间点的值为多少
     * <p>
     * 还可以通过 setInterpolator 为每一帧关键帧设置不同的插值器
     * 以及通过  setFraction  setValue 修改构造的值
     */
    private fun keyframeTest() {
        val keyframes = arrayOfNulls<Keyframe>(4)

        keyframes[0] = Keyframe.ofFloat(0f, 0F)

        keyframes[1] = Keyframe.ofFloat(0.25f, -60f)
        keyframes[1]?.interpolator = AccelerateInterpolator()

        keyframes[2] = Keyframe.ofFloat(0.75f, 60f)
        keyframes[2]?.interpolator = DecelerateInterpolator()

        keyframes[3] = Keyframe.ofFloat(0.9f, 10f)
        keyframes[3]?.fraction = 1f
        keyframes[3]?.value = 0f

        val frameHolder = PropertyValuesHolder.ofKeyframe("rotation", *keyframes)
        val animator = ObjectAnimator.ofPropertyValuesHolder(show!!, frameHolder)
        animator.duration = 3000
        animator.start()
    }


    /**
     * keyframe.ofKeyframe 使用示例
     * <p>
     * 如果去掉第0帧，将以第一个关键帧为起始位置
     * 如果去掉结束帧，将以最后一个关键帧为结束位置
     * 使用Keyframe来构建动画，至少要有两个或两个以上帧
     */
    private fun keyframeOfKeyframeTest() {
        val keyframes = ArrayList<Keyframe>()
        keyframes.add(Keyframe.ofObject(0f, Character.valueOf('A')))
        keyframes.add(Keyframe.ofObject(0.25f, Character.valueOf('E')))
        keyframes.add(Keyframe.ofObject(0.75f, Character.valueOf('O')))
//        keyframes.add(Keyframe.ofObject(1f, Character.valueOf('Z')))

        val frameHolder = PropertyValuesHolder.ofKeyframe("CurChar", *keyframes.toTypedArray())
        frameHolder.setEvaluator(CharEvaluator())
        val animator = ObjectAnimator.ofPropertyValuesHolder(findViewById<View>(R.id.keyframe_ofKeyframe_test), frameHolder)
        animator.duration = 3000
        animator.start()

    }
}
