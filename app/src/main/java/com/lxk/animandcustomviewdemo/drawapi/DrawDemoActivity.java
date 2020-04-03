package com.lxk.animandcustomviewdemo.drawapi;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.drawapi.view.DrawApiBaseView;

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
                R.id.base_api,
                R.id.group
        );
    }

    @Override
    public void onClick(View v) {
        View view = null;
        switch (v.getId()) {
            case R.id.base_api:
                view = new DrawApiBaseView(this);
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
