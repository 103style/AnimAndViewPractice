package com.lxk.animandcustomviewdemo.recyclerview

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxk.animandcustomviewdemo.BaseClickActivity
import com.lxk.animandcustomviewdemo.R
import java.util.*

/**
 * @author https://github.com/103style
 * @date 2020/1/10 11:01
 */
class RecyclerViewActivity : BaseClickActivity() {

    private var rv: RecyclerView? = null
    private var cRv: TestRecyclerView? = null
    private val itemDecoration = TestItemDecoration()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        rv = findViewById(R.id.rv_test)
        cRv = findViewById(R.id.crv_test)

        setClickListener(
                R.id.linear_layout_manager,
                R.id.item_decoration,
                R.id.recycler_offset_children,
                R.id.recycler_layoutmanager,
                R.id.rv_3d
        )
    }

    private fun getAdapter(): TestAdapter {
        val data = ArrayList<String>()
        for (i in 1 until 50) {
            data.add(i.toString())
        }
        return TestAdapter(this, data)
    }

    override fun onClick(v: View) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val layoutManager: RecyclerView.LayoutManager?
        rv!!.visibility = View.GONE
        cRv!!.visibility = View.GONE
        when (v.id) {
            R.id.item_decoration -> {
                rv!!.removeItemDecoration(itemDecoration)
                rv!!.addItemDecoration(itemDecoration)
                layoutManager = LinearLayoutManager(this)
                rv!!.layoutManager = layoutManager
                rv!!.visibility = View.VISIBLE
            }
            R.id.linear_layout_manager -> {
                rv!!.removeItemDecoration(itemDecoration)
                layoutManager = LinearLayoutManager(this)
                rv!!.layoutManager = layoutManager
                rv!!.visibility = View.VISIBLE
            }
            R.id.recycler_offset_children -> {
                layoutManager = TestLayoutManager()
                rv!!.layoutManager = layoutManager
                rv!!.visibility = View.VISIBLE
            }
            R.id.recycler_layoutmanager -> {
                layoutManager = TestLayout3Manager()
                rv!!.layoutManager = layoutManager
                rv!!.visibility = View.VISIBLE
            }
            R.id.rv_3d -> {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                layoutManager = TestLayout4Manager()
                cRv!!.layoutManager = layoutManager
                cRv!!.visibility = View.VISIBLE
                cRv!!.adapter = getAdapter()
            }
            else -> {
            }
        }
        rv!!.adapter = getAdapter()

    }
}
