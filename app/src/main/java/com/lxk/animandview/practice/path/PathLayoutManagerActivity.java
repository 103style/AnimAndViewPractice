package com.lxk.animandview.practice.path;

import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;

/**
 * @author https://github.com/103style
 * @date 2020/4/28 10:26
 */
public class PathLayoutManagerActivity extends BaseClickActivity {

    private RecyclerView rv;
    private PathLayoutManager pathLayoutManager;
    private PathTestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_layout_manager);
        rv = findViewById(R.id.rv);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && pathLayoutManager == null) {
            initRV();
        }
    }

    private void initRV() {
        pathLayoutManager = getPathLayoutManager();
        rv.setLayoutManager(pathLayoutManager);
        adapter = new PathTestAdapter(this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private PathLayoutManager getPathLayoutManager() {
        Path path = new Path();
        int offset = rv.getWidth() / 6;
        path.moveTo(rv.getLeft() + offset, rv.getTop() + offset);
        path.quadTo(rv.getRight() - offset, rv.getTop() - offset,
                rv.getRight() - offset, rv.getBottom() - offset);
        return new PathLayoutManager(path, offset);
    }

    @Override
    public void onClick(View v) {

    }
}
