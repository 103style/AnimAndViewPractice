package com.lxk.animandcustomviewdemo.layoutAnimation

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.GridLayoutAnimationController
import android.view.animation.LayoutAnimationController
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ListView
import com.lxk.animandcustomviewdemo.BaseClickActivity
import com.lxk.animandcustomviewdemo.R

/**
 * @author https://github.com/103style
 * @date  2020/1/10 10:33
 * 验证 layoutAnimation 的示例
 */
class LayoutAnimationActivity : BaseClickActivity() {

    private var listView: ListView? = null
    private var listViewController: ListView? = null
    private var gridView: GridView? = null
    private var gridViewController: GridView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_animation)

        initView()

        setClickListener(
                R.id.bt_list_view_anim_attr,
                R.id.bt_list_view_anim_controller,
                R.id.bt_grid_view_anim_attr,
                R.id.bt_grid_view_anim_controller
        )
    }

    private fun initView() {
        listView = findViewById(R.id.lv)
        listViewController = findViewById(R.id.lv_code)
        gridView = findViewById(R.id.gv)
        gridViewController = findViewById(R.id.gv_code)
    }

    @Override
    override fun onClick(v: View) {
        reset()
        when (v.id) {
            R.id.bt_list_view_anim_attr -> testListViewLayoutAnimWithAttr()
            R.id.bt_list_view_anim_controller -> testListViewLayoutAnimController()
            R.id.bt_grid_view_anim_attr -> testGridLayoutAnimWithAttr()
            R.id.bt_grid_view_anim_controller -> testGridLayoutAnimController()
            else -> {
            }
        }
    }

    private fun reset() {
        listView!!.visibility = View.GONE
        listViewController!!.visibility = View.GONE
        gridView!!.visibility = View.GONE
        gridViewController!!.visibility = View.GONE
    }

    /**
     * ListView 的动画设置
     * 直接在 xml 中配置 android:layoutAnimation="@anim/layout_animation_test" 属性
     * <p>
     * 在 xml 中配置 只有第一次加载数据 在可视区域的才会有动画
     */
    private fun testListViewLayoutAnimWithAttr() {
        listView!!.visibility = View.VISIBLE
        val mAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData())
        listView!!.adapter = mAdapter
    }

    /**
     * ListView 的动画设置
     * 直通过代码来设置 LayoutAnimationController
     * <p>
     * 每次点击 setAdapter 或者 adapter.notifyDataSetChanged 展示在可视区域的都会有动画
     */
    private fun testListViewLayoutAnimController() {
        listViewController!!.visibility = View.VISIBLE
        val mAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData())
        listViewController!!.layoutAnimation = getLayoutAnimationController()
        listViewController!!.adapter = mAdapter
    }

    /**
     * 获取布局动画管理器
     */
    private fun getLayoutAnimationController(): LayoutAnimationController {
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        val animationController = LayoutAnimationController(animation)
        animationController.delay = 1f
        animationController.order = LayoutAnimationController.ORDER_NORMAL
//        animationController.order = LayoutAnimationController.ORDER_REVERSE
//        animationController.order = LayoutAnimationController.ORDER_RANDOM
        animationController.interpolator = AccelerateInterpolator()
        return animationController
    }

    /**
     * GridView 的动画设置
     * 直接在 xml 中配置 android:layoutAnimation="@anim/grid_layout_animation_test" 属性
     * <p>
     * 在 xml 中配置 只有第一次加载数据 在可视区域的才会有动画
     */
    private fun testGridLayoutAnimWithAttr() {
        gridView!!.visibility = View.VISIBLE
        if (gridView!!.adapter == null) {
            val mAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData())
            gridView!!.adapter = mAdapter
        } else {
            (gridView!!.adapter as BaseAdapter).notifyDataSetChanged()
        }
    }


    /**
     * GridView 的动画设置
     * 通过代码来设置 LayoutAnimationController
     * <p>
     * 通过代码设置, 每次点击 setAdapter 或者 adapter.notifyDataSetChanged 展示在可视区域的都会有动画
     */
    private fun testGridLayoutAnimController() {
        gridViewController!!.visibility = View.VISIBLE

        gridViewController!!.layoutAnimation = getGridLayoutAnimationController()

        if (gridViewController!!.adapter == null) {
            val mAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData())
            gridViewController!!.adapter = mAdapter
        } else {
            (gridViewController!!.adapter as BaseAdapter).notifyDataSetChanged()
        }
    }


    /**
     * 获取布局动画管理器
     * <p>
     * Direction 和 DirectionPriority 可以多样搭配
     */
    private fun getGridLayoutAnimationController(): GridLayoutAnimationController {
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        val animationController = GridLayoutAnimationController(animation)
//        animationController.columnDelay = 0.5F
//        animationController.rowDelay = 1F

        animationController.direction = GridLayoutAnimationController.DIRECTION_RIGHT_TO_LEFT
//        animationController.direction = GridLayoutAnimationController.DIRECTION_LEFT_TO_RIGHT
//        animationController.direction = GridLayoutAnimationController.DIRECTION_TOP_TO_BOTTOM
//        animationController.direction = GridLayoutAnimationController.DIRECTION_BOTTOM_TO_TOP

//        animationController.directionPriority= GridLayoutAnimationController.PRIORITY_ROW
        animationController.directionPriority = GridLayoutAnimationController.PRIORITY_COLUMN
//        animationController.directionPriority = GridLayoutAnimationController.PRIORITY_NONE
//        animationController.order =LayoutAnimationController.ORDER_NORMAL
//        animationController.interpolator = AccelerateInterpolator()
        return animationController
    }

    private fun getData(): List<String> {
        val data = ArrayList<String>()
        for (i in 1 until 50) {
            data.add(i.toString())
        }
        return data
    }
}
