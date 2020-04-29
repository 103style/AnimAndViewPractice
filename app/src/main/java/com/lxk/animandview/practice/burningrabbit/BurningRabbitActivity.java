package com.lxk.animandview.practice.burningrabbit;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;

/**
 * @author https://github.com/103style
 * @date 2020/4/20 15:13
 */
public class BurningRabbitActivity extends BaseClickActivity {

    private RecyclerView rvBack, rvFront;
    private ImitateBurningRabbitView imitateBurningRabbit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burning_rabbit);
        initStatusBar();

        rvBack = findViewById(R.id.back_view);
        rvFront = findViewById(R.id.front_view);
        imitateBurningRabbit = findViewById(R.id.ibr_layout);
        rvBack.setAdapter(new RabbitAdapter(this, true));
        rvFront.setAdapter(new RabbitAdapter(this, false));

        imitateBurningRabbit.getPullOutBottomView().setOnClickListener(v -> {
            imitateBurningRabbit.doBackNormal();
        });
        imitateBurningRabbit.getBottomBarView().setOnClickListener(v -> {
            rvFront.scrollToPosition(rvFront.getLayoutManager().getItemCount() - 1);
        });
    }

    @Override
    public void onClick(View v) {

    }


    private void initStatusBar() {
        //设置状态栏透明
        Window window = getWindow();
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //android 5.0以上的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
