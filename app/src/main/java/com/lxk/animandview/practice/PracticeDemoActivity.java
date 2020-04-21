package com.lxk.animandview.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;
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
                R.id.bruning_rabbit
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
