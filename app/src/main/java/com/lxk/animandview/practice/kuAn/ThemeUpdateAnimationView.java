package com.lxk.animandview.practice.kuAn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.lxk.animandview.utils.DensityUtils;

/**
 * @author https://github.com/103style
 * @date 2020/4/27 14:10
 * 参考 doc:https://blog.csdn.net/u011387817/article/details/79604418
 * 仿酷安的主题切换动画
 */
public class ThemeUpdateAnimationView extends View {

    private Context context;
    /**
     * 动画起始圆的圆心
     */
    private float startX, startY;
    /**
     * 动画圆当前的半径
     */
    private int radius;
    /**
     * 动画圆的起始半径
     */
    private int startRadius;
    /**
     * 动画圆的结束半径
     */
    private int endRadius;
    /**
     * activity视图
     */
    private ViewGroup mRootView;

    private Paint mPaint;

    /**
     * 动画更新的回调
     */
    private AnimatorListenerAdapter animListenerAdapter;
    /**
     * 动画结束的回调
     */
    private ValueAnimator.AnimatorUpdateListener animUpdateListener;
    /**
     * 主题更新动画
     */
    private ValueAnimator themeUpdateAnim;
    /**
     * 动画时长
     */
    private long animDuration = 3000;
    /**
     * Activity的视图
     */
    private Bitmap activityBitmap;
    /**
     * 更新动画结束的回调
     */
    private ThemeUpdateAnimFinishListener finishListener;

    private ThemeUpdateAnimationView(Context context, float startX, float startY, int radius) {
        super(context);
        this.context = context;
        this.startX = startX;
        this.startY = startY;
        this.radius = radius;
        startRadius = radius;
        mRootView = (ViewGroup) getActFromContext(context).getWindow().getDecorView();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        getEndRadius();
        initAnimListener();
    }

    public static ThemeUpdateAnimationView create(View targetView) {
        Context context = targetView.getContext();
        int halfX = targetView.getWidth() / 2;
        int halfY = targetView.getHeight() / 2;
        float x = DensityUtils.getAbsoluteX(targetView) + halfX;
        float y = DensityUtils.getAbsoluteY(targetView) + halfY;
        int radius = Math.max(halfX, halfY);
        return new ThemeUpdateAnimationView(context, x, y, radius);
    }

    public ThemeUpdateAnimationView setAnimFinishListener(ThemeUpdateAnimFinishListener finishListener) {
        this.finishListener = finishListener;
        return this;
    }

    public ThemeUpdateAnimationView setAnimDuration(long animDuration) {
        this.animDuration = animDuration;
        return this;
    }

    /**
     * 获取所在的Activity
     */
    private Activity getActFromContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        throw new RuntimeException("Activity not found!");
    }

    /**
     * 获取当前视图动画的最大显示的半径
     */
    private void getEndRadius() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int w = metrics.widthPixels;
        int h = metrics.heightPixels;
        int lLeftTopCenter = (int) Math.sqrt(Math.pow(startX, 2) + Math.pow(startY, 2));
        int lRightTopCenter = (int) Math.sqrt(Math.pow(w - startX, 2) + Math.pow(startY, 2));
        int lLeftBottomCenter = (int) Math.sqrt(Math.pow(startX, 2) + Math.pow(h - startY, 2));
        int lRightBottomCenter = (int) Math.sqrt(Math.pow(w - startX, 2) + Math.pow(h - startY, 2));
        endRadius = Math.max(
                Math.max(lLeftBottomCenter, lLeftTopCenter),
                Math.max(lRightBottomCenter, lRightTopCenter)
        );
    }

    /**
     * 配置动画的回调
     */
    private void initAnimListener() {
        animListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (finishListener != null) {
                    finishListener.finish();
                }
                radius = startRadius;
                detachedFromRootView();
            }
        };
        animUpdateListener = animation -> {
            radius = (int) animation.getAnimatedValue();
            postInvalidate();
        };
    }

    public void startAnim() {
        if (themeUpdateAnim == null) {
            themeUpdateAnim = getUpdateAnimator();
        }
        if (themeUpdateAnim.isRunning()) {
            return;
        }

        updateActivityBitmap();

        attachToRootView();

        themeUpdateAnim.start();
    }

    private ValueAnimator getUpdateAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(radius, endRadius);
        animator.addUpdateListener(animUpdateListener);
        animator.addListener(animListenerAdapter);
        animator.setDuration(animDuration);
        animator.setInterpolator(new AccelerateInterpolator());
        return animator;
    }

    private void updateActivityBitmap() {
        if (activityBitmap != null && !activityBitmap.isRecycled()) {
            activityBitmap.recycle();
        }
        activityBitmap = getBitmapFromRootView();
    }

    /**
     * 获取当前Activit的视图
     */
    private Bitmap getBitmapFromRootView() {
        mRootView.measure(MeasureSpec.makeMeasureSpec(mRootView.getLayoutParams().width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mRootView.getLayoutParams().height, MeasureSpec.EXACTLY));
        Bitmap bitmap = Bitmap.createBitmap(mRootView.getWidth(), mRootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mRootView.draw(canvas);
        return bitmap;
    }

    /**
     * 添加到根视图
     */
    private void attachToRootView() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView.addView(this);
    }

    /**
     * 移除
     */
    private void detachedFromRootView() {
        if (mRootView != null) {
            mRootView.removeView(this);
            mRootView = null;
        }
        if (activityBitmap != null) {
            if (!activityBitmap.isRecycled()) {
                activityBitmap.recycle();
            }
            activityBitmap = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制圆形
        //在新的图层上面绘制
        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        canvas.drawBitmap(activityBitmap, 0, 0, null);
        canvas.drawCircle(startX, startY, radius, mPaint);
        canvas.restoreToCount(layer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //消耗事件
        return true;
    }

    public interface ThemeUpdateAnimFinishListener {
        /**
         * 动画执行完成
         */
        void finish();
    }
}
