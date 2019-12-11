package com.lxk.animandcustomviewdemo.animator;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.animator.evaluator.ReverseEvaluator;
import com.lxk.animandcustomviewdemo.animator.interpolator.ReverseInterpolator;

/**
 * @author https://github.com/103style
 * @date 2019/12/10 14:33
 * 属性动画
 */
public class AnimatorActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = AnimatorActivity.class.getSimpleName();
    private Button show;
    private TextView tvShow;
    private ViewGroup.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

        tvShow = findViewById(R.id.tv_show);
        show = findViewById(R.id.bt_demo);

        findViewById(R.id.bt_reset).setOnClickListener(this);
        findViewById(R.id.bt_translate).setOnClickListener(this);
        findViewById(R.id.bt_translate_with_reverse_interpolator).setOnClickListener(this);
        findViewById(R.id.bt_evaluator_test).setOnClickListener(this);
        findViewById(R.id.bt_reserve_evaluator).setOnClickListener(this);
        findViewById(R.id.bt_rotate).setOnClickListener(this);
        findViewById(R.id.bt_scale).setOnClickListener(this);
        findViewById(R.id.bt_alpha).setOnClickListener(this);
        findViewById(R.id.bt_set).setOnClickListener(this);
        findViewById(R.id.bt_demo).setOnClickListener(this);
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
                tvShow.setText("");
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
            case R.id.bt_alpha:
                animator = getAlphaAnim();
                break;
            case R.id.bt_rotate:
                animator = getRotateAnim();
                break;
            case R.id.bt_scale:
                animator = getScaleAnim();
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

    /**
     * 文字变化
     */
    private ValueAnimator getTvShowAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1000);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                tvShow.setText(String.valueOf(value));
            }
        });
        return valueAnimator;
    }

    private void reverseEvaluator() {
        //返回从 1000 到 0 的Integer值
        ValueAnimator animator = getTvShowAnim();
        animator.setEvaluator(new ReverseEvaluator());
        animator.start();
    }

    private void testEvaluator() {
        //返回从 0 到 1000 的Integer值
        ValueAnimator animator = getTvShowAnim();
        animator.setEvaluator(new IntEvaluator());
        animator.start();
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
    private Animator getAlphaAnim() {
        return null;
    }

    /**
     * 旋转
     */
    private Animator getRotateAnim() {

        return null;
    }

    /**
     * 放缩
     */
    private Animator getScaleAnim() {
        return null;
    }

    /**
     * 动画集合
     */
    private Animator getAnimSet() {
        return null;
    }
}
