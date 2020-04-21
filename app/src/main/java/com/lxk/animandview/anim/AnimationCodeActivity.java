package com.lxk.animandview.anim;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;

/**
 * @author https://github.com/103style
 * @date 2019/11/19 16:40
 * <p>
 * 代码实现补间动画
 */
public class AnimationCodeActivity extends BaseClickActivity {

    private Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_xml);

        show = findViewById(R.id.bt_demo);

        setClickListener(
                R.id.bt_translate,
                R.id.bt_rotate,
                R.id.bt_scale,
                R.id.bt_alpha,
                R.id.bt_set,
                R.id.bt_demo
        );
    }

    @Override
    public void onClick(View v) {
        Animation animation = null;
        switch (v.getId()) {
            case R.id.bt_translate:
                animation = getTranslateAnim();
                break;
            case R.id.bt_alpha:
                animation = getAlphaAnim();
                break;
            case R.id.bt_rotate:
                animation = getRotateAnim();
                break;
            case R.id.bt_scale:
                animation = getScaleAnim();
                break;
            case R.id.bt_set:
                animation = getAnimSet();
                break;
            case R.id.bt_demo:
                Toast.makeText(AnimationCodeActivity.this, "don't touch me!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        if (animation != null) {
            show.startAnimation(animation);
        }
    }

    /**
     * 位移动画
     */
    private Animation getTranslateAnim() {
        TranslateAnimation translateAnimation = new TranslateAnimation(-200, 200,
                -200, 200);
        //在原有基础上增加一次  即一共2次
        translateAnimation.setRepeatCount(1);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setDuration(2000);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        return translateAnimation;
    }

    /**
     * 透明度变化
     */
    private Animation getAlphaAnim() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        //在原有基础上增加一次  即一共2次
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        return alphaAnimation;
    }


    /**
     * 旋转
     */
    private Animation getRotateAnim() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //在原有基础上增加一次  即一共2次
        rotateAnimation.setRepeatCount(1);
        rotateAnimation.setRepeatMode(Animation.REVERSE);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setInterpolator(new OvershootInterpolator());
        return rotateAnimation;
    }

    /**
     * 放缩
     */
    private Animation getScaleAnim() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.2f,
                0.1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //在原有基础上增加一次  即一共2次
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setInterpolator(new OvershootInterpolator());
        return scaleAnimation;
    }

    /**
     * 动画集合
     */
    private Animation getAnimSet() {
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(getAlphaAnim());
        animationSet.addAnimation(getScaleAnim());
        animationSet.addAnimation(getRotateAnim());
        animationSet.addAnimation(getRotateAnim());
        animationSet.setInterpolator(new DecelerateInterpolator());
        return animationSet;
    }
}
