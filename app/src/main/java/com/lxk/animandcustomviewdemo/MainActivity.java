package com.lxk.animandcustomviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lxk.animandcustomviewdemo.anim.AnimationCodeActivity;
import com.lxk.animandcustomviewdemo.anim.AnimationXmlActivity;
import com.lxk.animandcustomviewdemo.animator.AnimatorActivity;
import com.lxk.animandcustomviewdemo.recyclerview.RecyclerViewActivity;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 16:29
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.animation_xml).setOnClickListener(this);
        findViewById(R.id.animation_code).setOnClickListener(this);
        findViewById(R.id.animator).setOnClickListener(this);
        findViewById(R.id.layout_manager_decoration).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Class c = null;
        switch (v.getId()) {
            case R.id.layout_manager_decoration:
                c = RecyclerViewActivity.class;
                break;
            case R.id.animation_xml:
                c = AnimationXmlActivity.class;
                break;
            case R.id.animation_code:
                c = AnimationCodeActivity.class;
                break;
            case R.id.animator:
                c = AnimatorActivity.class;
                break;
            default:
                break;
        }
        if (c != null) {
            startActivity(new Intent(this, c));
        }
    }
}
