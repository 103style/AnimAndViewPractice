package com.lxk.animandview.practice.burningrabbit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.R;
import com.lxk.animandview.utils.DensityUtils;

/**
 * @author xiaoke.luo@tcl.com 2020/4/21 16:58
 */
public class RabbitAdapter extends RecyclerView.Adapter<RabbitAdapter.Holder> {
    private static final String TAG = "RabbitAdapter";

    private Context context;
    private int count = 100;
    private boolean back;

    public RabbitAdapter(Context context, boolean back) {
        this.context = context;
        this.back = back;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_rabbit, parent, false));
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String color;
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        switch (position % 4) {
            case 0:
                lp.height = DensityUtils.dpToPx(context, 96);
                color = "#ff00ddff";
                break;
            case 1:
                lp.height = DensityUtils.dpToPx(context, 144);
                color = "#ffffbb33";
                break;
            case 2:
                lp.height = DensityUtils.dpToPx(context, 224);
                color = "#FF3F51B5";
                break;
            case 3:
            default:
                lp.height = DensityUtils.dpToPx(context, 176);
                color = "#ff99cc00";
                break;
        }
        holder.itemView.setBackgroundColor(Color.parseColor(color));
        if (back) {
            lp.height = DensityUtils.dpToPx(context, 192);
        }
        holder.itemView.setLayoutParams(lp);
        ((TextView) holder.itemView).setText(String.valueOf(position));

    }

    static class Holder extends RecyclerView.ViewHolder {

        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
