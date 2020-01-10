package com.lxk.animandcustomviewdemo.animator

import android.animation.*
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.annotation.AnimatorRes
import com.lxk.animandcustomviewdemo.BaseClickActivity
import com.lxk.animandcustomviewdemo.R
import java.util.*

/**
 * @author https://github.com/103style
 * @date 2020/1/10 13:25
 * xml实现属性动画
 */
class AnimatorXmlActivity : BaseClickActivity() {

    /**
     * 保存菜单动画的打开动画
     */
    var menuAnimators = ArrayList<Animator>()
    /**
     * 保存菜单动画的关闭动画
     */
    var menuReverseAnimators = ArrayList<Animator>()

    /**
     * 菜单是否打开
     */
    private var menuOpen: Boolean = false
    /**
     * 菜单动画是否不在执行中
     */
    private var menuAnimEnable: Boolean = true

    private var show: Button? = null
    private var menuLayout: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animator_xml)

        show = findViewById(R.id.bt_demo)
        menuLayout = findViewById(R.id.menu_layout)

        setClickListener(
                R.id.bt_xml_alpha,
                R.id.bt_object_alpha,
                R.id.bt_object_color,
                R.id.bt_animator_set,
                R.id.menu_switch,
                R.id.bt_demo
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_xml_alpha -> loadXmlAnimator()
            R.id.bt_object_alpha -> loadXmlObjectRotationAnimator()
            R.id.bt_object_color -> loadXmlObjectColorAnimator()
            R.id.bt_animator_set -> loadXmlSetAnimator()

            R.id.menu_switch -> startMenuAnim()
            R.id.bt_demo -> Toast.makeText(this, "don't touch me!", Toast.LENGTH_SHORT).show()
            else -> {

            }
        }
    }

    /**
     * 加载 ValueAnimator 的xml动画
     */
    private fun loadXmlAnimator() {
        val valueAnimator = AnimatorInflater.loadAnimator(this, R.animator.animator_alpha) as ValueAnimator
        valueAnimator.addUpdateListener { animation ->
            show?.alpha = animation.animatedValue as Float

        }
        valueAnimator.start()
    }

    /**
     * 加载 ObjectAnimator 的透明度变化的 xml动画
     */
    private fun loadXmlObjectRotationAnimator() {
        startObjectXmlAnim(R.animator.object_animator_alpha)
    }

    /**
     * 加载 ObjectAnimator 的背景颜色变化的 xml动画
     */
    private fun loadXmlObjectColorAnimator() {
        startObjectXmlAnim(R.animator.object_animator_backgroup_color)
    }

    private fun startObjectXmlAnim(@AnimatorRes id: Int) {
        val objectAnimator = AnimatorInflater.loadAnimator(this, id) as ObjectAnimator
        objectAnimator.target = show
        objectAnimator.start()
    }

    /**
     * 加载 AnimationSet xml动画
     */
    private fun loadXmlSetAnimator() {
        val animatorSet = AnimatorInflater.loadAnimator(this, R.animator.animator_set) as AnimatorSet
        animatorSet.setTarget(show)
        animatorSet.start()
    }

    /**
     * 菜单动画
     */
    private fun startMenuAnim() {
        if (!menuAnimEnable) {
            return
        }
        val count: Int = menuLayout!!.childCount
        val minSize = 4
        if (count < minSize) {
            Toast.makeText(this, "need more then " + minSize + " child", Toast.LENGTH_SHORT).show()
            return
        }
        menuAnimEnable = false
        //减去按钮的视图 和 演示的视图
        val usableCount = count - 2
        if (menuAnimators.size == 0) {
            //计算间隔的角度
            val angle = Math.PI / 2 / (usableCount - 1)
            //设置移动的距离
            val radius = dp2px(256F)
            for (i in 1 until usableCount) {
                val t = angle * (i - 1)
                val child = menuLayout!!.getChildAt(i)
                //添加 平移、透明度、缩放 动画
                addAnim(child, "translationX", radius * Math.cos(t))
                addAnim(child, "translationY", radius * Math.sin(t))
                addAnim(child, "alpha", 1.0)
                addAnim(child, "scaleX", 1.0)
                addAnim(child, "scaleY", 1.0)

                //设置对应的点击事件
                child.setOnClickListener { v ->
                    startMenuAnim()
                    Toast.makeText(this, menuLayout!!.indexOfChild(v).toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        //开始执行动画
        startAnimSet()
    }

    private fun startAnimSet() {
        val animatorSet = AnimatorSet()
        if (menuOpen) {
            animatorSet.playTogether(menuReverseAnimators)
        } else {
            animatorSet.playTogether(menuAnimators)
        }
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                menuAnimEnable = true
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        animatorSet.duration = 1000
        animatorSet.start()
        menuOpen = !menuOpen
    }

    private fun addAnim(child: View, propertyName: String, value: Double) {
        val end: Float = value.toFloat()

        val reverseObjectAnimator = ObjectAnimator.ofFloat(child, propertyName, 0F, end)
        reverseObjectAnimator.interpolator = DecelerateInterpolator()
        menuAnimators.add(reverseObjectAnimator)

        val objectAnimator = ObjectAnimator.ofFloat(child, propertyName, end, 0F)
        objectAnimator.interpolator = AccelerateInterpolator()
        menuReverseAnimators.add(objectAnimator)

    }

    fun dp2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}
