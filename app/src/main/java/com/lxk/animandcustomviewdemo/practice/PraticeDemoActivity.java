package com.lxk.animandcustomviewdemo.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.practice.view.BiliBiliPathView;

/**
 * @author https://github.com/103style
 * @date 2020/4/20 15:13
 */
public class PraticeDemoActivity extends BaseClickActivity {

    private final String TAG = PraticeDemoActivity.class.getSimpleName();

    private FrameLayout group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parctice_demo);
        group = findViewById(R.id.group);
        setClickListener(
                R.id.bilibili_path
        );
    }

    @Override
    public void onClick(View v) {
        View view = null;
        switch (v.getId()) {
            case R.id.bilibili_path:
                view = new BiliBiliPathView(this);
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
