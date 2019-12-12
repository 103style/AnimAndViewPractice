package com.lxk.animandcustomviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lxk.animandcustomviewdemo.anim.AnimationCodeActivity;
import com.lxk.animandcustomviewdemo.anim.AnimationXmlActivity;
import com.lxk.animandcustomviewdemo.animator.AnimatorActivity;
import com.lxk.animandcustomviewdemo.animator.AnimatorXmlActivity;
import com.lxk.animandcustomviewdemo.animator.PropertyValuesHolderAndKeyFrameActivity;
import com.lxk.animandcustomviewdemo.layoutAnimation.LayoutAnimationActivity;
import com.lxk.animandcustomviewdemo.recyclerview.RecyclerViewActivity;

/**
 * @author https://github.com/103style
 * @date 2019/11/14 16:29
 * 动画 和 自定义View 示例
 */
public class MainActivity extends BaseClickActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setClickListener(
                R.id.animation_xml,
                R.id.animation_code,
                R.id.animator,
                R.id.animator_xml,
                R.id.property_keyframe,
                R.id.layout_anim,
                R.id.layout_manager_decoration
        );
    }

    @Override
    public void onClick(View v) {
        Class c = null;
        switch (v.getId()) {
            case R.id.animation_xml:
                c = AnimationXmlActivity.class;
                break;
            case R.id.animation_code:
                c = AnimationCodeActivity.class;
                break;
            case R.id.animator:
                c = AnimatorActivity.class;
                break;
            case R.id.animator_xml:
                c = AnimatorXmlActivity.class;
                break;
            case R.id.property_keyframe:
                c = PropertyValuesHolderAndKeyFrameActivity.class;
                break;
            case R.id.layout_manager_decoration:
                c = RecyclerViewActivity.class;
                break;
            case R.id.layout_anim:
                c = LayoutAnimationActivity.class;
                break;
            default:
                break;
        }
        if (c != null) {
            startActivity(new Intent(this, c));
        }
    }
}
