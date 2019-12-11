package com.lxk.animandcustomviewdemo.animator;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 15:53
 * 自定义属性 的 属性动画 demo
 */
public class CustomPropertyAnimView extends AppCompatButton {

    private int curText;

    public CustomPropertyAnimView(Context context) {
        this(context, null);
    }

    public CustomPropertyAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.buttonStyle);
    }

    public CustomPropertyAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCurText(int curText) {
        setText(String.format("Char: %s", (char) curText));
    }

    @Override
    public boolean performClick() {
        //这里会去执行onclickListener
        super.performClick();

        //执行动画 并消耗点击事件
        startObjectAnim();
        return true;
    }


    /**
     * 自定义属性 的 属性动画  需要实现 "set属性名" 方法
     */
    private void startObjectAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "curText", 'A', 'Z');
        objectAnimator.setEvaluator(new IntEvaluator());
        objectAnimator.setDuration(3000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setText("CustomPropertyAnim");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }
}
