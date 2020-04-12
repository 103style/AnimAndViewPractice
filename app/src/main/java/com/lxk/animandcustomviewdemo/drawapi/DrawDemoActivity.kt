package com.lxk.animandcustomviewdemo.drawapi

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.lxk.animandcustomviewdemo.BaseClickActivity
import com.lxk.animandcustomviewdemo.R
import com.lxk.animandcustomviewdemo.drawapi.view.*

/**
 * @author https://github.com/103style
 * @date 2020/4/3 13:29
 */
class DrawDemoActivity : BaseClickActivity() {
    private var group: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_demo)
        group = findViewById(R.id.group)
        setClickListener(
                R.id.simple,
                R.id.text_and_path,
                R.id.range,
                R.id.canvas_change,
                R.id.text,
                R.id.bezier_pen,
                R.id.bezier_water,
                R.id.cap_join,
                R.id.path_effect,
                R.id.color_matrix,
                R.id.color_filter,
                R.id.xfermode,
                R.id.guaguaka,
                R.id.src_in,
                R.id.qq_dot,
                R.id.bitmap_shader_mode,
                R.id.bitmap_shader_demo,
                R.id.bitmap_shader_avatar,
                R.id.linear_gradient,
                R.id.flash_text,
                R.id.radial_gradient,
                R.id.radial_gradient_demo,
                R.id.group
        )
    }

    override fun onClick(v: View) {
        var view: View? = null
        when (v.id) {
            R.id.simple -> view = DrawSimpleDemoView(this)
            R.id.text_and_path -> view = DrawTextAndPathDemoView(this)
            R.id.range -> view = RangeDemo(this)
            R.id.canvas_change -> view = CanvasChangeDemo(this)
            R.id.text -> view = DrawTextDemoView(this)
            R.id.bezier_pen -> view = DrawBezierPenView(this)
            R.id.bezier_water -> view = DrawBezierWaterView(this)
            R.id.cap_join -> view = StrokeCapJoinDemoView(this)
            R.id.path_effect -> view = DrawPathEffectDemoView(this)
            R.id.color_matrix -> view = DrawColorMatrixDemoView(this)
            R.id.color_filter -> view = DrawColorFilterDemoView(this)
            R.id.xfermode -> view = DrawXfermodeDemoView(this)
            R.id.guaguaka -> view = XfermodeGuaGuaKaView(this)
            R.id.src_in -> view = XfermodeSrcInDemoView(this)
            R.id.qq_dot -> view = QQDotDemoView(this)
            R.id.bitmap_shader_mode -> view = BitmapShaderModeDemoView(this)
            R.id.bitmap_shader_demo -> view = BitmapShaderDemoView(this)
            R.id.bitmap_shader_avatar -> view = BitmapShaderAvatarView(this)
            R.id.linear_gradient -> view = LinearGradientModeView(this)
            R.id.flash_text -> view = FlashTextView(this)
            R.id.radial_gradient -> view = RadialGradientModeView(this)
            R.id.radial_gradient_demo -> view = RadialGradientDemoView(this)
            R.id.group -> clean()
            else -> {
            }
        }
        view?.let { showView(it) }
    }

    private fun showView(view: View) {
        group!!.addView(view)
    }

    private fun clean() {
        group!!.removeAllViews()
    }
}