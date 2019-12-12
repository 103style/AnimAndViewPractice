package com.lxk.animandcustomviewdemo.recyclerview;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 16:29
 */
public class RecyclerViewActivity extends BaseClickActivity {

    private RecyclerView rv;
    private TestRecyclerView cRv;
    private TestItemDecoration itemDecoration = new TestItemDecoration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        rv = findViewById(R.id.rv_test);
        cRv = findViewById(R.id.crv_test);

        setClickListener(
                R.id.linear_layout_manager,
                R.id.item_decoration,
                R.id.recycler_offset_children,
                R.id.recycler_layoutmanager,
                R.id.rv_3d
        );
    }

    private RecyclerView.Adapter getAdapter() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add(String.valueOf(i + 1));
        }
        return new TestAdapter(this, datas);
    }

    @Override
    public void onClick(View v) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RecyclerView.LayoutManager layoutManager;
        rv.setVisibility(View.GONE);
        cRv.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.item_decoration:
                rv.removeItemDecoration(itemDecoration);
                rv.addItemDecoration(itemDecoration);
                layoutManager = new LinearLayoutManager(this);
                rv.setLayoutManager(layoutManager);
                rv.setVisibility(View.VISIBLE);
                break;

            case R.id.linear_layout_manager:
                rv.removeItemDecoration(itemDecoration);
                layoutManager = new LinearLayoutManager(this);
                rv.setLayoutManager(layoutManager);
                rv.setVisibility(View.VISIBLE);
                break;

            case R.id.recycler_offset_children:
                layoutManager = new TestLayoutManager();
                rv.setLayoutManager(layoutManager);
                rv.setVisibility(View.VISIBLE);
                break;

            case R.id.recycler_layoutmanager:
                layoutManager = new TestLayout3Manager();
                rv.setLayoutManager(layoutManager);
                rv.setVisibility(View.VISIBLE);
                break;

            case R.id.rv_3d:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                layoutManager = new TestLayout4Manager();
                cRv.setLayoutManager(layoutManager);
                cRv.setVisibility(View.VISIBLE);
                cRv.setAdapter(getAdapter());
                break;

            default:
                break;
        }
        rv.setAdapter(getAdapter());

    }
}
