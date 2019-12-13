package com.lxk.animandcustomviewdemo.layoutAnimation;

import android.animation.AnimatorInflater;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.animator.view.TestEvaluatorView;

/**
 * @author https://github.com/103style
 * @date 2019/12/13 10:22
 * 验证 animateLayoutChanges 的示例
 */
public class AnimateLayoutChangesActivity extends BaseClickActivity {

    private LinearLayout animateLayoutChangeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animate_changes);

        animateLayoutChangeLayout = findViewById(R.id.ll_animate_layout_change);

        setClickListener(
                R.id.bt_add,
                R.id.bt_delete,
                R.id.bt_transition
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                addItem();
                break;
            case R.id.bt_delete:
                deleteItem();
                break;
            case R.id.bt_transition:
                updateAnimateLayoutChanges();
                break;
            default:
                break;
        }
    }

    /**
     * 测试验证 animateLayoutChanges
     */
    private void addItem() {
        animateLayoutChangeLayout.addView(getAddView(), 0);
    }

    /**
     * 测试删除动画
     */
    private void deleteItem() {
        int count = animateLayoutChangeLayout.getChildCount();
        if (count > 0) {
            animateLayoutChangeLayout.removeViewAt(0);
        }
    }

    /**
     * 获取测试数据
     */
    private View getAddView() {
        TestEvaluatorView button = new TestEvaluatorView(this);
        button.setText(button.getClass().getSimpleName());
        button.setAllCaps(false);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        button.setPadding(8, 8, 8, 8);
        button.setLayoutParams(params);
        return button;
    }


    /**
     * 通过代码修改  animateLayoutChanges 属性
     * {@link ViewGroup R.styleable.ViewGroup_animateLayoutChanges:}
     * <p>
     * APPEARING           —— 元素在容器中出现时所定义的动画。
     * DISAPPEARING        —— 元素在容器中消失时所定义的动画。
     * CHANGE_APPEARING    —— 由于容器中要显现一个新的元素，其它需要变化的元素所应用的动画
     * CHANGE_DISAPPEARING —— 当容器中某个元素消失，其它需要变化的元素所应用的动画
     *  LayoutTransition构造函数中默认动画都是通过 PropertyValuesHolder 添加的
     * <p>
     * CHANGE_XXX 必须使用 PropertyValuesHolder 所构造的动画才会有效果
     */
    private void updateAnimateLayoutChanges() {
        animateLayoutChangeLayout.removeAllViews();
        LayoutTransition layoutTransition = new LayoutTransition();
        // translationX 0 - 200
        layoutTransition.setAnimator(LayoutTransition.APPEARING,
                AnimatorInflater.loadAnimator(this, R.animator.animator_appearing));
        // translationX 0 - -200
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING,
                AnimatorInflater.loadAnimator(this, R.animator.animator_disappearing));

        PropertyValuesHolder changeAppear = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder changeDisappear = PropertyValuesHolder.ofInt("top", 1, 0);
        ObjectAnimator changeAppearingAnim
                = ObjectAnimator.ofPropertyValuesHolder(animateLayoutChangeLayout, changeAppear);
        ObjectAnimator changeDisappearingAnim
                = ObjectAnimator.ofPropertyValuesHolder(animateLayoutChangeLayout, changeDisappear);
        layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeAppearingAnim);
        layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeDisappearingAnim);

        //设置所有动画完成所需要的时长
        //setDuration(int transitionType, long duration) 对单个type设置时长
        layoutTransition.setDuration(1000);

        //针对单个type设置插值器
        layoutTransition.setInterpolator(LayoutTransition.APPEARING, new AccelerateInterpolator());

        //针对单个type设置动画延时
        layoutTransition.setStartDelay(LayoutTransition.DISAPPEARING, 1000);

        //设置单个item间的动画间隔的。
        layoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 1000);
        layoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 1000);

        animateLayoutChangeLayout.setLayoutTransition(layoutTransition);

        Toast.makeText(this, "try add and delete again!", Toast.LENGTH_SHORT).show();
    }


}
