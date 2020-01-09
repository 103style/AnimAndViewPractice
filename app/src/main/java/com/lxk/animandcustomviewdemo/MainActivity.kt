package com.lxk.animandcustomviewdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lxk.animandcustomviewdemo.anim.AnimationCodeActivity
import com.lxk.animandcustomviewdemo.anim.AnimationXmlActivity
import com.lxk.animandcustomviewdemo.animator.AnimatorActivity
import com.lxk.animandcustomviewdemo.animator.AnimatorXmlActivity
import com.lxk.animandcustomviewdemo.animator.PropertyValuesHolderAndKeyFrameActivity
import com.lxk.animandcustomviewdemo.layoutAnimation.AnimateLayoutChangesActivity
import com.lxk.animandcustomviewdemo.layoutAnimation.LayoutAnimationActivity
import com.lxk.animandcustomviewdemo.recyclerview.RecyclerViewActivity

/**
 * @author https://github.com/103style
 * @date 2019/11/14 16:29
 * 动画 和 自定义View 示例
 */
class MainActivity : BaseClickActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListener(
                R.id.animation_xml,
                R.id.animation_code,
                R.id.animator,
                R.id.animator_xml,
                R.id.property_keyframe,
                R.id.layout_anim,
                R.id.layout_animate_changes,
                R.id.layout_manager_decoration
        )
    }

    override fun onClick(v: View) {
        val c = when (v.id) {
            R.id.animation_xml -> AnimationXmlActivity::class.java
            R.id.animation_code -> AnimationCodeActivity::class.java
            R.id.animator -> AnimatorActivity::class.java
            R.id.animator_xml -> AnimatorXmlActivity::class.java
            R.id.property_keyframe -> PropertyValuesHolderAndKeyFrameActivity::class.java
            R.id.layout_manager_decoration -> RecyclerViewActivity::class.java
            R.id.layout_anim -> LayoutAnimationActivity::class.java
            R.id.layout_animate_changes -> AnimateLayoutChangesActivity::class.java
            else -> null
        }
        if (c != null) {
            startActivity(Intent(this, c))
        }
    }
}