package com.lxk.animandview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lxk.animandview.anim.AnimationCodeActivity;
import com.lxk.animandview.anim.AnimationXmlActivity;
import com.lxk.animandview.animator.AnimatorActivity;
import com.lxk.animandview.animator.AnimatorXmlActivity;
import com.lxk.animandview.animator.PropertyValuesHolderAndKeyFrameActivity;
import com.lxk.animandview.drawapi.DrawDemoActivity;
import com.lxk.animandview.layoutAnimation.AnimateLayoutChangesActivity;
import com.lxk.animandview.layoutAnimation.LayoutAnimationActivity;
import com.lxk.animandview.practice.PracticeDemoActivity;
import com.lxk.animandview.recyclerview.RecyclerViewActivity;

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
                R.id.layout_animate_changes,
                R.id.layout_manager_decoration,
                R.id.draw_api,
                R.id.practice
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
            case R.id.layout_animate_changes:
                c = AnimateLayoutChangesActivity.class;
                break;
            case R.id.draw_api:
                c = DrawDemoActivity.class;
                break;
            case R.id.practice:
                c = PracticeDemoActivity.class;
                break;
            default:
                break;
        }
        if (c != null) {
            startActivity(new Intent(this, c));
        }
    }
}
