package com.lxk.animandview.practice.arcview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lxk.animandview.R;

/**
 * @author https://github.com/103style
 * @date 2020/4/25 14:33
 */
public class ArcSlidingTestView extends FrameLayout {

    private ArcSlidingHelper arcSlidingHelper;

    private TextView textView;

    public ArcSlidingTestView(Context context) {
        this(context, null);
    }

    public ArcSlidingTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.view_arc_sliding_test, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        textView = findViewById(R.id.tv);
        setOnTouchListener((v, event) -> {
            if (arcSlidingHelper == null) {
                //创建对象
                arcSlidingHelper = ArcSlidingHelper.create(textView,
                        angle -> {
                            float rotation = textView.getRotation() + angle;
                            textView.setRotation(rotation);
                            textView.setText(String.valueOf(rotation));
                        });
                //开启惯性滚动
                arcSlidingHelper.enableInertialSliding(true);
            }
            //处理滑动事件
            arcSlidingHelper.handleMovement(event);
            return true;
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (arcSlidingHelper != null) {
            arcSlidingHelper.release();
        }
    }
}
