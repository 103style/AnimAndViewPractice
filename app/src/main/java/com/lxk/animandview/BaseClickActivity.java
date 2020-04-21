package com.lxk.animandview;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 14:08
 */
public abstract class BaseClickActivity extends AppCompatActivity implements View.OnClickListener {

    protected void setClickListener(@IdRes int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }
}
