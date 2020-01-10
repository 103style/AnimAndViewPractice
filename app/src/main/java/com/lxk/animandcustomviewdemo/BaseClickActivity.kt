package com.lxk.animandcustomviewdemo

import android.view.View

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

/**
 * @author https://github.com/103style
 * @date 2020/1/10 9:50
 */
abstract class BaseClickActivity : AppCompatActivity(), View.OnClickListener {
    protected fun setClickListener(@IdRes vararg ids: Int) {
        for (id in ids) {
            findViewById<View>(id).setOnClickListener(this)
        }
    }
}