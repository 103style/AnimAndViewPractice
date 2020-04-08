package com.lxk.animandcustomviewdemo.drawapi;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.drawapi.view.CanvasChangeDemo;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawBezierPenView;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawBezierWaterView;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawColorFilterDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawColorMatrixDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawPathEffectDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawSimpleDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawTextAndPathDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawTextDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawXfermodeDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.QQDotDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.RangeDemo;
import com.lxk.animandcustomviewdemo.drawapi.view.XfermodeSrcInDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.StrokeCapJoinDemoView;
import com.lxk.animandcustomviewdemo.drawapi.view.XfermodeGuaGuaKaView;

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
}
