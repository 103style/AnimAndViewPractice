package com.lxk.animandview.practice.burningrabbit;

import android.os.Bundle;
import android.view.View;

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
    private View frontLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burning_rabbit);
        rvBack = findViewById(R.id.rv_back);
        rvFront = findViewById(R.id.rv_front);
        frontLayout = findViewById(R.id.front_layout);
        imitateBurningRabbit = findViewById(R.id.ibr_layout);
        imitateBurningRabbit.normalState();
        rvBack.setAdapter(new RabbitAdapter(this, true));
        rvFront.setAdapter(new RabbitAdapter(this, false));

        imitateBurningRabbit.getBottomBarView().setOnClickListener(v -> {
            imitateBurningRabbit.linearState();
            rvBack.setVisibility(View.VISIBLE);
            frontLayout.setVisibility(View.GONE);
        });

        imitateBurningRabbit.getCloseBarView().setOnClickListener(v -> {
            imitateBurningRabbit.normalState();
            rvBack.setVisibility(View.GONE);
            frontLayout.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onClick(View v) {

    }
}
