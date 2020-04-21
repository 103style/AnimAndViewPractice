package com.lxk.animandview.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.R;

import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 16:43
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private static final String TAG = "TestAdapter";
    private Context mContext;
    private List<String> mDatas;
    private int createCount;

    public TestAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        createCount = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        createCount++;
        Log.e(TAG, "onCreateViewHolder: createViewCount = " + createCount);
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: position = " + position);
        holder.tv.setText(mDatas.get(position));
        holder.itemView.setBackgroundColor(position % 2 == 0 ? Color.RED : Color.BLUE);
        holder.pic.setImageResource(position % 2 == 0 ? R.drawable.img1 : R.drawable.img2);
    }


    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;
        private ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
