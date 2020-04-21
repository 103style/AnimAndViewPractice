package com.lxk.animandview.drawapi;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;
import com.lxk.animandview.drawapi.view.BitmapShaderAvatarView;
import com.lxk.animandview.drawapi.view.BitmapShaderDemoView;
import com.lxk.animandview.drawapi.view.BitmapShaderModeDemoView;
import com.lxk.animandview.drawapi.view.CanvasChangeDemo;
import com.lxk.animandview.drawapi.view.DrawBezierPenView;
import com.lxk.animandview.drawapi.view.DrawBezierWaterView;
import com.lxk.animandview.drawapi.view.DrawColorFilterDemoView;
import com.lxk.animandview.drawapi.view.DrawColorMatrixDemoView;
import com.lxk.animandview.drawapi.view.DrawPathEffectDemoView;
import com.lxk.animandview.drawapi.view.DrawSimpleDemoView;
import com.lxk.animandview.drawapi.view.DrawTextAndPathDemoView;
import com.lxk.animandview.drawapi.view.DrawTextDemoView;
import com.lxk.animandview.drawapi.view.DrawXfermodeDemoView;
import com.lxk.animandview.drawapi.view.FlashTextView;
import com.lxk.animandview.drawapi.view.LinearGradientModeView;
import com.lxk.animandview.drawapi.view.QQDotDemoView;
import com.lxk.animandview.drawapi.view.RadialGradientDemoView;
import com.lxk.animandview.drawapi.view.RadialGradientModeView;
import com.lxk.animandview.drawapi.view.RangeDemo;
import com.lxk.animandview.drawapi.view.StrokeCapJoinDemoView;
import com.lxk.animandview.drawapi.view.XfermodeGuaGuaKaView;
import com.lxk.animandview.drawapi.view.XfermodeSrcInDemoView;

/**
 * @author https://github.com/103style
 * @date 2020/4/3 13:29
 */
public class DrawDemoActivity extends BaseClickActivity {

    private final String TAG = DrawDemoActivity.class.getSimpleName();

    private FrameLayout group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_demo);
        group = findViewById(R.id.group);
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
        );
    }

    @Override
    public void onClick(View v) {
        View view = null;
        switch (v.getId()) {
            case R.id.simple:
                view = new DrawSimpleDemoView(this);
                break;
            case R.id.text_and_path:
                view = new DrawTextAndPathDemoView(this);
                break;
            case R.id.range:
                view = new RangeDemo(this);
                break;
            case R.id.canvas_change:
                view = new CanvasChangeDemo(this);
                break;
            case R.id.text:
                view = new DrawTextDemoView(this);
                break;
            case R.id.bezier_pen:
                view = new DrawBezierPenView(this);
                break;
            case R.id.bezier_water:
                view = new DrawBezierWaterView(this);
                break;
            case R.id.cap_join:
                view = new StrokeCapJoinDemoView(this);
                break;
            case R.id.path_effect:
                view = new DrawPathEffectDemoView(this);
                break;
            case R.id.color_matrix:
                view = new DrawColorMatrixDemoView(this);
                break;
            case R.id.color_filter:
                view = new DrawColorFilterDemoView(this);
                break;
            case R.id.xfermode:
                view = new DrawXfermodeDemoView(this);
                break;
            case R.id.guaguaka:
                view = new XfermodeGuaGuaKaView(this);
                break;
            case R.id.src_in:
                view = new XfermodeSrcInDemoView(this);
                break;
            case R.id.qq_dot:
                view = new QQDotDemoView(this);
                break;
            case R.id.bitmap_shader_mode:
                view = new BitmapShaderModeDemoView(this);
                break;
            case R.id.bitmap_shader_demo:
                view = new BitmapShaderDemoView(this);
                break;
            case R.id.bitmap_shader_avatar:
                view = new BitmapShaderAvatarView(this);
                break;
            case R.id.linear_gradient:
                view = new LinearGradientModeView(this);
                break;
            case R.id.flash_text:
                view = new FlashTextView(this);
                break;
            case R.id.radial_gradient:
                view = new RadialGradientModeView(this);
                break;
            case R.id.radial_gradient_demo:
                view = new RadialGradientDemoView(this);
                break;
            case R.id.group:
                clean();
                break;
            default:
                break;
        }

        if (view != null) {
            showView(view);
        }

    }

    private void showView(View view) {
        group.addView(view);
    }

    private void clean() {
        group.removeAllViews();
    }


    @Override
    public void onBackPressed() {
        if (group.getChildCount() > 0) {
            clean();
        } else {
            super.onBackPressed();
        }
    }
}
