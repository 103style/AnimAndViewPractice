package com.lxk.animandcustomviewdemo.animator;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.animator.evaluator.CharEvaluator;
import com.lxk.animandcustomviewdemo.animator.evaluator.StringEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2019/12/11 17:26
 */
public class PropertyValuesHolderAndKeyFrameActivity extends BaseClickActivity {

    private final String TAG = PropertyValuesHolderAndKeyFrameActivity.class.getSimpleName();
    private ImageView show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder_keyframe);

        show = findViewById(R.id.show);

        setClickListener(
                R.id.property_values_holder_float,
                R.id.property_values_holder_object,
                R.id.keyframe_test,
                R.id.keyframe_ofKeyframe_test

        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.property_values_holder_float:
                propertyValuesHolderFloatTest();
                break;
            case R.id.property_values_holder_object:
                propertyValuesHolderObjectTest();
                break;
            case R.id.keyframe_test:
                keyframeTest();
                break;
            case R.id.keyframe_ofKeyframe_test:
                keyframeOfKeyframeTest();
                break;
            default:
                break;
        }
    }

    /**
     * PropertyValuesHolder.ofFloat
     * ObjectAnimator.ofPropertyValuesHolder
     * 使用示例
     */
    private void propertyValuesHolderFloatTest() {
        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("Rotation", 0, 360);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("Alpha", 1, 0, 1);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(show, rotationHolder, alphaHolder);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    /**
     * PropertyValuesHolder.ofObject
     * ObjectAnimator.ofPropertyValuesHolder
     * 使用示例
     */
    private void propertyValuesHolderObjectTest() {
        PropertyValuesHolder objectHolder = PropertyValuesHolder.ofObject("CurText",
                new StringEvaluator("在吗？，在！，one hour later!，在吗？，在！，one hour later!，在吗？"), "", "滚!");
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                findViewById(R.id.property_values_holder_object)
                , objectHolder);
        objectAnimator.setDuration(6000);
        objectAnimator.start();
    }

    /**
     * keyframe使用示例
     * Keyframe 的 第一个参数取值为(0 - 1),即整个动画的起始的一个时间点
     * 第二个参数则为对应的值
     * 即为 指定某一时间点的值为多少
     * <p>
     * 还可以通过 setInterpolator 为每一帧关键帧设置不同的插值器
     * 以及通过  setFraction  setValue 修改构造的值
     */
    private void keyframeTest() {
        Keyframe[] keyframes = new Keyframe[4];

        keyframes[0] = Keyframe.ofFloat(0f, 0);

        keyframes[1] = Keyframe.ofFloat(0.25f, -60f);
        keyframes[1].setInterpolator(new AccelerateInterpolator());

        keyframes[2] = Keyframe.ofFloat(0.75f, 60f);
        keyframes[2].setInterpolator(new DecelerateInterpolator());

        keyframes[3] = Keyframe.ofFloat(0.9f, 10f);
        keyframes[3].setFraction(1f);
        keyframes[3].setValue(0f);

        PropertyValuesHolder frameHolder = PropertyValuesHolder.ofKeyframe("rotation", keyframes);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(show, frameHolder);
        animator.setDuration(3000);
        animator.start();
    }


    /**
     * keyframe.ofKeyframe 使用示例
     * <p>
     * 如果去掉第0帧，将以第一个关键帧为起始位置
     * 如果去掉结束帧，将以最后一个关键帧为结束位置
     * 使用Keyframe来构建动画，至少要有两个或两个以上帧
     */
    private void keyframeOfKeyframeTest() {
        List<Keyframe> keyframes = new ArrayList<>();
        keyframes.add(Keyframe.ofObject(0f, new Character('A')));
        keyframes.add(Keyframe.ofObject(0.25f, new Character('E')));
        keyframes.add(Keyframe.ofObject(0.75f, new Character('O')));
//        keyframes.add(Keyframe.ofObject(1f, new Character('Z')));

        PropertyValuesHolder frameHolder
                = PropertyValuesHolder.ofKeyframe("CurChar", keyframes.toArray(new Keyframe[keyframes.size()]));
        frameHolder.setEvaluator(new CharEvaluator());
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(findViewById(R.id.keyframe_ofKeyframe_test), frameHolder);
        animator.setDuration(3000);
        animator.start();

    }
}
