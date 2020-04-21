package com.lxk.animandview.practice.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.lxk.animandview.drawapi.Utils;

import java.util.Arrays;

/**
 * @author https://github.com/103style
 * @date 2020/4/20 15:03
 * 参考： https://blog.csdn.net/u011387817/article/details/78817827
 * <p>
 * PathMeasure
 */
public class BiliBiliPathView extends View {
    /**
     * 透明头变化的结束值
     */
    private final int MAX_ALPHA = 255;

    private Paint strokePaint;
    /**
     * 路径
     */
    private Path path;
    /**
     * 手指滑动的上一个坐标
     */
    private float preX, preY;
    /**
     * 获取通过PathMeasure 获取对应进度的路径点集合
     */
    private float[] mLightPoints, mDarkPoints;
    /**
     * 对应的亮色 暗色 和 默认的 path路径颜色
     */
    private int mLightColor, mDarkColor, mPathColor;

    /**
     * 获取path对应的路径类
     */
    private KeyFrames keyFrames;

    /**
     * 进度变化 和 透明度变化 动画
     */
    private ValueAnimator progressAnimator, alphaAnimator;
    /**
     * 当前暗色路径的颜色 alpha值
     */
    private int mAlpha = MAX_ALPHA;
    /**
     * 进度动画时长
     */
    private long animDuration = 5000;

    public BiliBiliPathView(Context context) {
        this(context, null);
    }

    public BiliBiliPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path = new Path();

        strokePaint = Utils.initPaint(context, Paint.Style.STROKE);

        //设置线冒 和 连接处 为 圆角
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);

        mLightColor = Color.RED;
        mDarkColor = Color.DKGRAY;
        mPathColor = Color.WHITE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //绘制初始路径
        int gap = getMeasuredWidth() / 8;
        path.moveTo(getMeasuredWidth() / 2, 0);
        path.rLineTo(0, getMeasuredHeight() / 2 - 2 * gap);
        path.rLineTo(-gap, gap);
        path.rLineTo(0, gap);
        path.rLineTo(gap, gap);
        path.lineTo(getMeasuredWidth() / 2, getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //执行初始路径的进度变化动画
        doAnim();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animIsRunning()) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                preX = x;
                preY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float eX = (preX + x) / 2;
                float eY = (preY + y) / 2;
                path.quadTo(preX, preY, eX, eY);
                preX = x;
                preY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                doAnim();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void doAnim() {
        keyFrames = new KeyFrames(path);
        startProgressAnim();
    }


    private void startProgressAnim() {
        if (progressAnimator == null) {
            progressAnimator = ValueAnimator.ofFloat(0f, 2f);
            progressAnimator.setInterpolator(new LinearInterpolator());
            progressAnimator.setDuration(animDuration);
            progressAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                float lengthPer = 0.5f;
                float start = value - lengthPer;
                if (value >= 1f) {
                    //亮线走完
                    startAlphaAnimator(animDuration / 3);
                }
                setLineProgress(start, value, true);
                setLineProgress(start - lengthPer, start, false);
                invalidate();
            });
            progressAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    alphaAnimator = null;
                    mAlpha = 255;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        progressAnimator.start();
    }

    private void startAlphaAnimator(long duration) {
        if (alphaAnimator == null) {
            alphaAnimator = ValueAnimator.ofInt(0, MAX_ALPHA);
            alphaAnimator.setInterpolator(new LinearInterpolator());
            alphaAnimator.setDuration(duration);
            alphaAnimator.addUpdateListener(animation -> mAlpha = MAX_ALPHA - (int) animation.getAnimatedValue());
            alphaAnimator.start();
        }
    }


    private boolean animIsRunning() {
        if (progressAnimator == null) {
            return false;
        }
        return progressAnimator.isRunning();
    }


    private void setLineProgress(float start, float end, boolean isLightPoints) {
        if (keyFrames == null) {
            throw new IllegalStateException("path not set yet");
        }
        if (isLightPoints) {
            mLightPoints = keyFrames.getRangeValue(start, end);
        } else {
            mDarkPoints = keyFrames.getRangeValue(start, end);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.argb(128, 0, 0, 0));
        if (animIsRunning()) {
            strokePaint.setColor(mLightColor);
            if (mLightPoints != null) {
                canvas.drawPoints(mLightPoints, strokePaint);
            }

            strokePaint.setColor(mDarkColor);
            //需要设置颜色后调用
            strokePaint.setAlpha(mAlpha);
            if (mDarkPoints != null) {
                canvas.drawPoints(mDarkPoints, strokePaint);
            }
        } else {
            strokePaint.setColor(mPathColor);
            canvas.drawPath(path, strokePaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animIsRunning()) {
            progressAnimator.cancel();
        }
    }

    private static class KeyFrames {
        /**
         * 精度(数值越少 numPoints 就越大)
         */
        static final float PRECISION = 1f;
        /**
         * 存在的点的个数
         */
        int numPoints;
        /**
         * 依次保存 每个点 的 x、y 坐标
         */
        float[] mPositionData;

        KeyFrames(Path path) {
            init(path);
        }

        void init(Path path) {
            final PathMeasure pathMeasure = new PathMeasure(path, false);
            final float pathLength = pathMeasure.getLength();

            //根据精度获取点的个数
            numPoints = (int) (pathLength / PRECISION + 0.5f);
            //依次保存所有点的 x、y 值
            mPositionData = new float[numPoints * 2];

            final float[] position = new float[2];
            int index = 0;
            int max = numPoints - 1;
            for (int i = 0; i < numPoints; ++i) {
                //获取当前百分比所占的长度
                final float distance = i * pathLength / max;
                //获取当前长度所在的坐标保存到position中
                pathMeasure.getPosTan(distance, position, null);

                //依次保存到mData中
                mPositionData[index] = position[0];
                mPositionData[index + 1] = position[1];

                index += 2;
            }
            numPoints = mPositionData.length;
        }

        float getLegalValue(float v) {
            if (v < 0) {
                return 0;
            } else if (v > 1) {
                return 1;
            } else {
                return v;
            }
        }

        /**
         * 拿到start和end之间的x,y数据
         *
         * @param start 开始百分比 0 - 1
         * @param end   结束百分比 0 - 1
         * @return 裁剪后的数据
         */
        float[] getRangeValue(float start, float end) {
            if (start >= end) {
                return null;
            }
            start = getLegalValue(start);
            end = getLegalValue(end);

            int startIndex = (int) (numPoints * start + 0.5f);
            int endIndex = (int) (numPoints * end + 0.5f);

            //必须是偶数，因为需要float[]{x,y}这样x和y要配对的
            if (startIndex % 2 != 0) {
                //直接减，不用担心 < 0  因为0是偶数，哈哈
                startIndex--;
            }
            if (endIndex % 2 != 0) {
                //不用检查越界
                endIndex++;
            }

            System.out.println(startIndex + "----" + endIndex);
            //根据起止点裁剪
            return Arrays.copyOfRange(mPositionData, startIndex, endIndex);
        }
    }
}
