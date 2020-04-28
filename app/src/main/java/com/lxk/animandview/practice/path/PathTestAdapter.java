package com.lxk.animandview.practice.path;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.R;

/**
 * @author https://github.com/103style
 * @date 2020/4/28 10:34
 */
public class PathTestAdapter extends RecyclerView.Adapter<PathTestAdapter.ViewHolder> {

    private int count = 100;

    private Context context;

    public PathTestAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_path_layout_manager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.itemView instanceof Button) {
            ((Button) holder.itemView).setText(String.valueOf(position));
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
