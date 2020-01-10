package com.lxk.animandcustomviewdemo.anim

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import com.lxk.animandcustomviewdemo.BaseClickActivity
import com.lxk.animandcustomviewdemo.R

/**
 * @author https://github.com/103style
 * @date 2020/1/10 11:23
 * <p>
 * xml 实现补间动画
 */
class AnimationXmlActivity : BaseClickActivity() {

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
        var animId = -1
        when (v.id) {
            R.id.bt_translate -> animId = R.anim.tween_translation
            R.id.bt_alpha -> animId = R.anim.tween_alpha
            R.id.bt_rotate -> animId = R.anim.tween_rotation
            R.id.bt_scale -> animId = R.anim.tween_scale
            R.id.bt_set -> animId = R.anim.tween_anim_set
            R.id.bt_demo -> Toast.makeText(this, "don't touch me!", Toast.LENGTH_SHORT).show()
            else -> {
            }
        }

        if (animId != -1) {
            val animation = AnimationUtils.loadAnimation(this, animId)
            show!!.startAnimation(animation)
        }
    }
}
