package com.lxk.animandcustomviewdemo.animator.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;

import androidx.appcompat.widget.AppCompatButton;

import com.lxk.animandcustomviewdemo.animator.evaluator.CharEvaluator;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 15:53
 */
public class TestEvaluatorView extends AppCompatButton {

    public TestEvaluatorView(Context context) {
        this(context, null);
    }

    public TestEvaluatorView(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.buttonStyle);
    }

    public TestEvaluatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        //这里会去执行onclickListener
        super.performClick();

        //执行动画 并消耗点击事件
        startAnim();
        return true;
    }

    /**
     * 文字内容从A - Z 变化的动画
     */
    private void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new CharEvaluator(), 'A', 'Z');
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setText(String.format("Char: %s", animation.getAnimatedValue().toString()));
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setText("CharEvaluatorTest");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }


}
