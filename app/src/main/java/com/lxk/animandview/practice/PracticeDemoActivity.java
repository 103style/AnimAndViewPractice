package com.lxk.animandview.practice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;
import com.lxk.animandview.practice.arcview.ArcSlidingHelper;
import com.lxk.animandview.practice.burningrabbit.BurningRabbitActivity;
import com.lxk.animandview.practice.view.BiliBiliPathView;

/**
 * @author https://github.com/103style
 * @date 2020/4/20 15:13
 */
public class PracticeDemoActivity extends BaseClickActivity {

    private final String TAG = PracticeDemoActivity.class.getSimpleName();

    private FrameLayout group;

    private ArcSlidingHelper arcSlidingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_demo);
        group = findViewById(R.id.group);
        setClickListener(
                R.id.bilibili_path,
                R.id.bruning_rabbit,
                R.id.arc_sliding,
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
                showView(wrapperView());
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

    private View wrapperView() {
        View frameLayout = LayoutInflater.from(this).inflate(R.layout.view_arc_sliding_test, group, false);
        TextView tv = frameLayout.findViewById(R.id.tv);
        if (arcSlidingHelper == null) {
            //创建对象
            arcSlidingHelper = ArcSlidingHelper.create(tv,
                    angle -> tv.setRotation(tv.getRotation() + angle));
            //开启惯性滚动
            arcSlidingHelper.enableInertialSliding(true);
        }
        arcSlidingHelper.updateTargetView(tv);
        frameLayout.setOnTouchListener((v, event) -> {
            //处理滑动事件
            arcSlidingHelper.handleMovement(event);
            return true;
        });
        return frameLayout;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (arcSlidingHelper != null) {
            arcSlidingHelper.release();
        }
    }
}
