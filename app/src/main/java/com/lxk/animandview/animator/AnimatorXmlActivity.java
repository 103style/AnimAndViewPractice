package com.lxk.animandview.animator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.AnimatorRes;

import com.lxk.animandview.BaseClickActivity;
import com.lxk.animandview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/12/12 13:32
 * xml实现属性动画
 */
public class AnimatorXmlActivity extends BaseClickActivity {

    private final String TAG = AnimatorXmlActivity.class.getSimpleName();

    /**
     * 保存菜单动画的打开动画
     */
    List<Animator> menuAnimators = new ArrayList<>();
    /**
     * 保存菜单动画的关闭动画
     */
    List<Animator> menuReverseAnimators = new ArrayList<>();

    /**
     * 菜单是否打开
     */
    private boolean menuOpen;
    /**
     * 菜单动画是否不在执行中
     */
    private boolean menuAnimEnable = true;

    private Button show;
    private ViewGroup menuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_xml);

        show = findViewById(R.id.bt_demo);
        menuLayout = findViewById(R.id.menu_layout);

        setClickListener(
                R.id.bt_xml_alpha,
                R.id.bt_object_alpha,
                R.id.bt_object_color,
                R.id.bt_animator_set,
                R.id.menu_switch,
                R.id.bt_demo
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_xml_alpha:
                loadXmlAnimator();
                break;
            case R.id.bt_object_alpha:
                loadXmlObjectRotationAnimator();
                break;
            case R.id.bt_object_color:
                loadXmlObjectColorAnimator();
                break;
            case R.id.bt_animator_set:
                loadXmlSetAnimator();
                break;
            case R.id.menu_switch:
                startMenuAnim();
                break;
            case R.id.bt_demo:
                Toast.makeText(AnimatorXmlActivity.this, "don't touch me!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 加载 ValueAnimator 的xml动画
     */
    private void loadXmlAnimator() {
        ValueAnimator valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(this, R.animator.animator_alpha);
        valueAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        show.setAlpha((Float) animation.getAnimatedValue());
                    }
                });
        valueAnimator.start();
    }

    /**
     * 加载 ObjectAnimator 的透明度变化的 xml动画
     */
    private void loadXmlObjectRotationAnimator() {
        startObjectXmlAnim(R.animator.object_animator_alpha);
    }

    /**
     * 加载 ObjectAnimator 的背景颜色变化的 xml动画
     */
    private void loadXmlObjectColorAnimator() {
        startObjectXmlAnim(R.animator.object_animator_backgroup_color);
    }

    private void startObjectXmlAnim(@AnimatorRes int id) {
        ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, id);
        objectAnimator.setTarget(show);
        objectAnimator.start();
    }

    /**
     * 加载 AnimationSet xml动画
     */
    private void loadXmlSetAnimator() {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animator_set);
        animatorSet.setTarget(show);
        animatorSet.start();
    }

    /**
     * 菜单动画
     */
    private void startMenuAnim() {
        if (!menuAnimEnable) {
            return;
        }
        int count = menuLayout.getChildCount();
        int minSize = 4;
        if (count < minSize) {
            Toast.makeText(AnimatorXmlActivity.this, "need more then " + minSize + " child", Toast.LENGTH_SHORT).show();
            return;
        }
        menuAnimEnable = false;
        //减去按钮的视图 和 演示的视图
        int usableCount = count - 2;
        if (menuAnimators.size() == 0) {
            //计算间隔的角度
            double angle = Math.PI / 2 / (usableCount - 1);
            //设置移动的距离
            int radius = dp2px(256);
            for (int i = 1; i <= usableCount; i++) {
                double t = angle * (i - 1);
                View child = menuLayout.getChildAt(i);
                //添加 平移、透明度、缩放 动画
                addAnim(child, "translationX", radius * Math.cos(t));
                addAnim(child, "translationY", radius * Math.sin(t));
                addAnim(child, "alpha", 1);
                addAnim(child, "scaleX", 1);
                addAnim(child, "scaleY", 1);

                //设置对应的点击事件
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startMenuAnim();
                        Toast.makeText(AnimatorXmlActivity.this,
                                String.valueOf(menuLayout.indexOfChild(v)),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        //开始执行动画
        startAnimSet();
    }

    private void startAnimSet() {
        AnimatorSet animatorSet = new AnimatorSet();
        if (menuOpen) {
            animatorSet.playTogether(menuReverseAnimators.toArray(new Animator[menuAnimators.size()]));
        } else {
            animatorSet.playTogether(menuAnimators.toArray(new Animator[menuReverseAnimators.size()]));
        }
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                menuAnimEnable = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.setDuration(1000);
        animatorSet.start();
        menuOpen = !menuOpen;
    }

    private void addAnim(View child, String propertyName, double value) {
        float end = (float) value;

        ObjectAnimator reverseObjectAnimator = ObjectAnimator.ofFloat(child, propertyName, 0, end);
        reverseObjectAnimator.setInterpolator(new DecelerateInterpolator());
        menuAnimators.add(reverseObjectAnimator);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(child, propertyName, end, 0);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        menuReverseAnimators.add(objectAnimator);

    }

    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
