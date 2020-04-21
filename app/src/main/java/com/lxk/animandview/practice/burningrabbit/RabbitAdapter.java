package com.lxk.animandview.practice.burningrabbit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lxk.animandview.R;
import com.lxk.animandview.drawapi.Utils;

/**
 * @author xiaoke.luo@tcl.com 2020/4/21 16:58
 */
public class RabbitAdapter extends RecyclerView.Adapter<RabbitAdapter.Holder> {

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
        int color;
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        switch (position % 3) {
            case 0:
                lp.height = Utils.dpToPx(context, 72);
                color = android.R.color.holo_blue_bright;
                break;
            case 1:
                lp.height = Utils.dpToPx(context, 144);
                color = android.R.color.holo_red_light;
                break;
            case 2:
            default:
                lp.height = Utils.dpToPx(context, 196);
                color = android.R.color.holo_green_light;
                break;
        }
        holder.itemView.setBackgroundColor(context.getResources().getColor(color));
        if (back) {
            lp.height = Utils.dpToPx(context, 192);
        }
        holder.itemView.setLayoutParams(lp);

    }

    static class Holder extends RecyclerView.ViewHolder {

        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
