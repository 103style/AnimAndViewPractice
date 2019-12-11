package com.lxk.animandcustomviewdemo.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.animator.evaluator.ReverseEvaluator;
import com.lxk.animandcustomviewdemo.animator.interpolator.ReverseInterpolator;

/**
 * @author https://github.com/103style
 * @date 2019/12/10 14:33
 * 属性动画
 */
public class AnimatorActivity extends BaseClickActivity implements View.OnClickListener {

    private final String TAG = AnimatorActivity.class.getSimpleName();
    private Button show;
    private ViewGroup.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

        show = findViewById(R.id.bt_demo);

        setClickListener(
                R.id.bt_reset,
                R.id.bt_translate,
                R.id.bt_translate_with_reverse_interpolator,
                R.id.bt_evaluator_test,
                R.id.bt_reserve_evaluator,
                R.id.bt_argb_evaluator,
                R.id.bt_rotate,
                R.id.bt_scale,
                R.id.bt_alpha,
                R.id.bt_set,
                R.id.bt_demo
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            layoutParams = show.getLayoutParams();
        }
    }

    @Override
    public void onClick(View v) {
        Animator animator = null;
        switch (v.getId()) {
            case R.id.bt_reset:
                show.setLayoutParams(layoutParams);
                show.setText(getString(R.string.demo_view));
                break;
            case R.id.bt_translate:
                animator = getTranslateAnim();
                break;
            case R.id.bt_translate_with_reverse_interpolator:
                animator = getTranslateWithInterpolatorAnim();
                break;
            case R.id.bt_evaluator_test:
                testEvaluator();
                break;
            case R.id.bt_reserve_evaluator:
                reverseEvaluator();
                break;
            case R.id.bt_argb_evaluator:
                animator = getArgbAnim();
                break;
            case R.id.bt_alpha:
                animator = getAlphaObjectAnim();
                break;
            case R.id.bt_rotate:
                animator = getRotateObjectAnim();
                break;
            case R.id.bt_scale:
                animator = getScaleObjectAnim();
                break;
            case R.id.bt_set:
                animator = getAnimSet();
                break;
            case R.id.bt_demo:
                Toast.makeText(AnimatorActivity.this, "don't touch me!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        if (animator != null) {
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Log.e(TAG, "onAnimationStart: ");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.e(TAG, "onAnimationEnd: ");
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    Log.e(TAG, "onAnimationCancel: ");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    Log.e(TAG, "onAnimationRepeat: ");
                }
            });
            animator.start();
        }
    }

    private ValueAnimator getTextUpdateAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1000);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                show.setText(String.valueOf(value));
            }
        });
        return valueAnimator;
    }

    private void reverseEvaluator() {
        //返回从 1000 到 0 的Integer值
        ValueAnimator animator = getTextUpdateAnim();
        animator.setEvaluator(new ReverseEvaluator());
        animator.start();
    }

    private void testEvaluator() {
        //返回从 0 到 1000 的Integer值
        ValueAnimator animator = getTextUpdateAnim();
        animator.setEvaluator(new IntEvaluator());
        animator.start();
    }

    /**
     * 颜色渐变
     * 将属性动画对应的 0 - 1 转化为对应的颜色
     */
    private ValueAnimator getArgbAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0x00000000, 0xFFFFFFFF, 0xFFD81B60);
        valueAnimator.setDuration(2000);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int) animation.getAnimatedValue();
                show.setBackgroundColor(curValue);
            }
        });
        return valueAnimator;
    }

    /**
     * 位移动画
     */
    private ValueAnimator getTranslateAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(show.getTop(), show.getBottom());
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                show.layout(show.getLeft(), value,
                        show.getRight(),
                        value + show.getHeight());
            }
        });
        return valueAnimator;
    }

    /**
     * 设置翻转插值器
     */
    private Animator getTranslateWithInterpolatorAnim() {
        ValueAnimator valueAnimator = getTranslateAnim();
        valueAnimator.setInterpolator(new ReverseInterpolator());
        return valueAnimator;
    }

    /**
     * 透明度变化
     */
    private Animator getAlphaObjectAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(show, "alpha", 1, 0, 1);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(1);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        return objectAnimator;
    }

    /**
     * 旋转
     */
    private ObjectAnimator getRotateObjectAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(show, "rotation", 0, 360);
        objectAnimator.setDuration(2000);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        return objectAnimator;
    }

    /**
     * 放缩
     */
    private Animator getScaleObjectAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(show, "scaleX", 0, 3, 1);
        objectAnimator.setDuration(2000);
        return objectAnimator;
    }

    /**
     * 动画集合
     */
    private Animator getAnimSet() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(getRotateObjectAnim())
                .with(getArgbAnim())
                .with(getTextUpdateAnim())
                .after(getTranslateWithInterpolatorAnim())
                .before(getAlphaObjectAnim());
        animatorSet.setDuration(5000);
        return animatorSet;
    }
}
