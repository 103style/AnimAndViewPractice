package com.lxk.animandcustomviewdemo.animator;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lxk.animandcustomviewdemo.BaseClickActivity;
import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.animator.evaluator.StringEvaluator;

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
                R.id.key_frame_test
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
            case R.id.key_frame_test:
                keyFrameTest();
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


    private void keyFrameTest() {

    }
}
