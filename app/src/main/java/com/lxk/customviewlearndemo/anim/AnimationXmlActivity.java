package com.lxk.customviewlearndemo.anim;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.lxk.customviewlearndemo.R;

/**
 * @author https://github.com/103style
 * @date 2019/11/19 11:38
 */
public class AnimationXmlActivity extends AppCompatActivity implements View.OnClickListener {

    private Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_xml);

        show = findViewById(R.id.bt_demo);

        findViewById(R.id.bt_translate).setOnClickListener(this);
        findViewById(R.id.bt_rotate).setOnClickListener(this);
        findViewById(R.id.bt_scale).setOnClickListener(this);
        findViewById(R.id.bt_alpha).setOnClickListener(this);
        findViewById(R.id.bt_set).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int animId = -1;
        switch (v.getId()) {
            case R.id.bt_translate:
                animId = R.anim.tween_translation;
                break;
            case R.id.bt_alpha:
                animId = R.anim.tween_alpha;
                break;
            case R.id.bt_rotate:
                animId = R.anim.tween_rotation;
                break;
            case R.id.bt_scale:
                animId = R.anim.tween_scale;
                break;
            case R.id.bt_set:
                animId = R.anim.tween_anim_set;
                break;
            default:
                break;
        }

        if (animId != -1) {
            Animation animation = AnimationUtils.loadAnimation(this, animId);
            show.startAnimation(animation);
        }
    }
}
