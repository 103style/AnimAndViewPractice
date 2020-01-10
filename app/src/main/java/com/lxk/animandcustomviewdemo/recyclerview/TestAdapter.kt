package com.lxk.animandcustomviewdemo.recyclerview

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lxk.animandcustomviewdemo.R

/**
 * @author https://github.com/103style
 * @date  2020/1/10 15:04
 */
class TestAdapter(private var mContext: Context, private var mData: ArrayList<String>?) : RecyclerView.Adapter<TestAdapter.ViewHolder>() {
    private val TAG: String = TestAdapter::class.java.name
    private var createCount: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        createCount++
        Log.e(TAG, "onCreateViewHolder: createViewCount = $createCount")
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder: position = " + position)
        holder.tv.text = mData!!.get(position)
        holder.itemView.setBackgroundColor(if (position % 2 == 0) Color.RED else Color.BLUE)
        holder.pic.setImageResource(if (position % 2 == 0) R.drawable.img1 else R.drawable.img2)
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.text)
        val pic: ImageView = itemView.findViewById(R.id.pic)

    }
}


