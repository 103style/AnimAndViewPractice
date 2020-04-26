package com.lxk.animandview.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;
import com.lxk.animandview.practice.arcview.ArcLayoutView;
import com.lxk.animandview.practice.arcview.ArcSlidingTestView;
import com.lxk.animandview.practice.burningrabbit.BurningRabbitActivity;
import com.lxk.animandview.practice.view.BiliBiliPathView;

/**
 * @author https://github.com/103style
 * @date 2020/4/20 15:13
 */
public class PracticeDemoActivity extends BaseClickActivity {

    private final String TAG = PracticeDemoActivity.class.getSimpleName();

    private FrameLayout group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_demo);
        group = findViewById(R.id.group);
        setClickListener(
                R.id.bilibili_path,
                R.id.bruning_rabbit,
                R.id.arc_sliding,
                R.id.arc_layout,
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
            button.setText("ArcLayout Test Item " + i);
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
}
