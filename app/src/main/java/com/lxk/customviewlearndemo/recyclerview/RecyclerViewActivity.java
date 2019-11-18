package com.lxk.customviewlearndemo.recyclerview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.customviewlearndemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 16:29
 */
public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = findViewById(R.id.rv_test);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new TestLayoutManager());
//        recyclerView.setLayoutManager(new TestLayout2Manager());
//        recyclerView.setLayoutManager(new TestLayout3Manager());
        recyclerView.setLayoutManager(new TestLayout4Manager());
        recyclerView.setAdapter(getAdapter());
//        recyclerView.addItemDecoration(new TestItemDecoration());
    }

    private RecyclerView.Adapter getAdapter() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(String.valueOf(i + 1));
        }
        TestAdapter adapter = new TestAdapter(this, datas);
        return adapter;
    }

}
