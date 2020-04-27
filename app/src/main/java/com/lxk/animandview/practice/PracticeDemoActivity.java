package com.lxk.animandview.practice;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;
import com.lxk.animandview.practice.arcview.ArcLayoutView;
import com.lxk.animandview.practice.arcview.ArcSlidingTestView;
import com.lxk.animandview.practice.burningrabbit.BurningRabbitActivity;
import com.lxk.animandview.practice.kuAn.ThemeUpdateAnimationView;
import com.lxk.animandview.practice.view.BiliBiliPathView;

/**
 * @author https://github.com/103style
 * @date 2020/4/20 15:13
 */
public class PracticeDemoActivity extends BaseClickActivity {

    private FrameLayout group;
    private View rootView;
    private ViewGroup buttonGroup;

    /**
     * 视图颜色切换相关的颜色
     */
    private int[] BgColor = {Color.WHITE, Color.DKGRAY};
    private int[] actionBarColor = {Color.parseColor("#008577"), Color.DKGRAY};
    private int[] buttonBgColor = {Color.parseColor("#D81B60"), Color.DKGRAY};
    private int[] buttonTextColor = {Color.parseColor("#FFFFFF"), Color.GRAY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_demo);
        rootView = findViewById(R.id.root_view);
        rootView.setBackgroundColor(Color.WHITE);
        buttonGroup = findViewById(R.id.button_group);
        group = findViewById(R.id.group);
        setClickListener(
                R.id.bilibili_path,
                R.id.bruning_rabbit,
                R.id.arc_sliding,
                R.id.arc_layout,
                R.id.ku_an_theme_update_anim,
                R.id.group
        );
    }

    @Override
    public void onClick(View v) {
        View view = null;
        switch (v.getId()) {
            case R.id.bilibili_path:
                view = new BiliBiliPathView(this);
                break;
            case R.id.bruning_rabbit:
                startActivity(new Intent(this, BurningRabbitActivity.class));
                return;
            case R.id.arc_sliding:
                view = new ArcSlidingTestView(this);
                break;
            case R.id.arc_layout:
                view = new ArcLayoutView(this);
                addTestView((ViewGroup) view);
                break;
            case R.id.ku_an_theme_update_anim:
                ThemeUpdateAnimationView.create(v).startAnim();
                updateTheme();
                return;
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

    private void addTestView(ViewGroup group) {
        for (int i = 0; i < 10; i++) {
            Button button = new Button(this);
            button.setAllCaps(false);
            button.setText("Click to change Gravity " + i);
            button.setOnClickListener(v -> {
                ArcLayoutView layoutView = (ArcLayoutView) group;
                layoutView.setCenterViewGravity((layoutView.getCenterViewGravity() + 1) % 9);
            });
            group.addView(button);
        }
    }

    private void showView(View view) {
        group.addView(view);
        group.setVisibility(View.VISIBLE);
    }

    private void clean() {
        group.removeAllViews();
        group.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (group.getChildCount() > 0) {
            clean();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 更新视图颜色
     */
    private void updateTheme() {
        ColorDrawable colorDrawable = (ColorDrawable) rootView.getBackground();
        int index = colorDrawable.getColor() == Color.WHITE ? 1 : 0;
        rootView.setBackgroundColor(BgColor[index]);
        for (int i = 0; i < buttonGroup.getChildCount(); i++) {
            View child = buttonGroup.getChildAt(i);
            child.setBackgroundColor(buttonBgColor[index]);
            if (child instanceof Button) {
                ((Button) child).setTextColor(buttonTextColor[index]);
            }
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor[index]));
        }
    }
}
