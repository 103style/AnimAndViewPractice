package com.lxk.animandview.practice.arcview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewParent;
import android.widget.Scroller;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

/**
 * @author https://github.com/103style
 * @date 2020/4/24 16:17
 * <p>
 * 参考：https://blog.csdn.net/u011387817/article/details/80313184
 */
public class ArcSlidingHelper {

    private static final String TAG = "ArcSlidingHelper";
    /**
     * 计算角度的固定点坐标
     */
    private int mPivotX, mPivotY;
    /**
     * 上一次触摸事件的坐标
     */
    private float mPreX, mPreY;
    private OnSlidingListener mListener;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private boolean isSelfSliding;
    private boolean isClockwiseScrolling;
    private boolean isShouldBeGetY;
    private boolean isRecycled;
    /**
     * 是否开启惯性滑动
     */
    private boolean isInertialSlidingEnable;
    private InertialSlidingHandler mHandler;
    private float mScrollAvailabilityRatio;
    private float mLastScrollOffset;
    private OnSlideFinishListener mSlideFinishListener;

    private ArcSlidingHelper(Context context, int pivotX, int pivotY, OnSlidingListener listener) {
        mPivotX = pivotX;
        mPivotY = pivotY;
        mListener = listener;
        mScroller = new Scroller(context);
        mScrollAvailabilityRatio = .3F;
        mVelocityTracker = VelocityTracker.obtain();
        mHandler = new InertialSlidingHandler(this);
    }

    public static ArcSlidingHelper create(@NonNull View targetView, @NonNull OnSlidingListener listener) {
        int[] res = getCenterPoint(targetView);
        return new ArcSlidingHelper(targetView.getContext(), res[0], res[1], listener);
    }

    public static int[] getCenterPoint(@NonNull View targetView) {
        int width = targetView.getMeasuredWidth();
        int height = targetView.getMeasuredHeight();
        //如果宽度为0，提示宽度无效，需要调用updatePivotX方法来设置x轴的旋转基点
        if (width == 0) {
            Log.e(TAG, "targetView width = 0! please invoke the updatePivotX(int) method to update the PivotX!", new RuntimeException());
        }
        //如果高度为0，提示高度无效，需要调用updatePivotY方法来设置y轴的旋转基点
        if (height == 0) {
            Log.e(TAG, "targetView height = 0! please invoke the updatePivotY(int) method to update the PivotY!", new RuntimeException());
        }
        //获取以view左上点为原点的 中心点坐标
        int centerX = width / 2;
        int centerY = height / 2;
        //获取view在屏幕根布局的 左上点的绝对坐标
        int left = (int) getAbsoluteX(targetView);
        int top = (int) getAbsoluteY(targetView);
        int[] res = new int[2];
        res[0] = left + centerX;
        res[1] = top + centerY;
        return res;
    }

    /**
     * 获取当前视图的x 的绝对坐标
     *
     * @param targetView
     * @return
     */
    private static float getAbsoluteX(View targetView) {
        float res = targetView.getX();
        ViewParent parent = targetView.getParent();
        if (parent instanceof View) {
            res += getAbsoluteX((View) parent);
        }
        return res;
    }

    private static float getAbsoluteY(View targetView) {
        float res = targetView.getY();
        ViewParent parent = targetView.getParent();
        if (parent instanceof View) {
            res += getAbsoluteY((View) parent);
        }
        return res;
    }

    public void updateTargetView(@NonNull View targetView) {
        int[] res = getCenterPoint(targetView);
        updateTargetParams(res[0], res[1]);
    }

    private void updateTargetParams(int pivotX, int pivotY) {
        mPivotX = pivotX;
        mPivotY = pivotY;
    }

    /**
     * 设置自身滑动
     *
     * @param isSelfSliding 是否view自身滑动
     */
    public void setSelfSliding(boolean isSelfSliding) {
        checkIsRecycled();
        this.isSelfSliding = isSelfSliding;
    }

    /**
     * 设置惯性滑动
     *
     * @param enable 是否开启
     */
    public void enableInertialSliding(boolean enable) {
        checkIsRecycled();
        isInertialSlidingEnable = enable;
    }

    /**
     * 打断动画
     */
    public void abortAnimation() {
        checkIsRecycled();
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    /**
     * 更新当前手指触摸的坐标，在ViewGroup的onInterceptTouchEvent中使用
     */
    public void updateMovement(MotionEvent event) {
        checkIsRecycled();
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isSelfSliding) {
                mPreX = event.getRawX();
                mPreY = event.getRawY();
            } else {
                mPreX = event.getX();
                mPreY = event.getY();
            }
        }
    }


    /**
     * 更新圆心x坐标
     *
     * @param pivotX 新的x坐标
     */

    public void updatePivotX(int pivotX) {
        checkIsRecycled();
        mPivotX = pivotX;
    }

    /**
     * 更新圆心y坐标
     *
     * @param pivotY 新的y坐标
     */
    public void updatePivotY(int pivotY) {
        checkIsRecycled();
        mPivotY = pivotY;
    }

    /**
     * 设置惯性滑动的利用率
     */
    public void setScrollAvailabilityRatio(@FloatRange(from = 0, to = 1) float ratio) {
        checkIsRecycled();
        mScrollAvailabilityRatio = ratio;
    }


    /**
     * 处理触摸时间
     */
    public void handleMovement(MotionEvent event) {
        checkIsRecycled();
        float x, y;
        if (isSelfSliding) {
            x = event.getRawX();
            y = event.getRawY();
        } else {
            x = event.getX();
            y = event.getY();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (isInertialSlidingEnable) {
                    mVelocityTracker.computeCurrentVelocity(1000);
                    mScroller.fling(0, 0, (int) mVelocityTracker.getXVelocity(), (int) mVelocityTracker.getYVelocity(),
                            Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    startFling();
                }
                break;
            default:
                break;
        }
        mPreX = x;
        mPreY = y;
    }

    /**
     * 计算滑动角度
     */
    private void handleActionMove(float x, float y) {
        //              __________
        //根据公式 bc = √ ab² + ac² 计算出对角线的长度
        //        A (mPreX,mPreY)
        //        /\
        //       /  \
        //      /____\
        //  P(pX,pY)  B (X,Y)
        //圆心P到起始点A的线条长度
        float lineAP = (float) Math.sqrt(Math.pow(Math.abs(mPreX - mPivotX), 2) + Math.pow(Math.abs(mPreY - mPivotY), 2));
        //圆心P到结束点B的线条长度
        float lineBP = (float) Math.sqrt(Math.pow(Math.abs(x - mPivotX), 2) + Math.pow(Math.abs(y - mPivotY), 2));
        //起始点A到结束点B的线条长度
        float lineAB = (float) Math.sqrt(Math.pow(Math.abs(x - mPreX), 2) + Math.pow(Math.abs(y - mPreY), 2));

        if (lineAP > 0 && lineBP > 0 && lineAB > 0) {
            //根据余弦定理 公式 计算 cos /_APB = (AP² + BP² - AB²) / 2AP*BP
            //余弦定理：https://baike.baidu.com/item/%E4%BD%99%E5%BC%A6%E5%AE%9A%E7%90%86/957460?fr=aladdin
            double cosAPB = (Math.pow(lineAP, 2) + Math.pow(lineBP, 2) - Math.pow(lineAB, 2)) / (2 * lineAP * lineBP);
            //转换为角度
            float angle = (float) Math.toDegrees(Math.acos(cosAPB));
            if (!Float.isNaN(angle)) {
                isClockwiseScrolling = isClockwise(x, y);
                angle = isClockwiseScrolling ? angle : -angle;
                Log.e(TAG, "handleActionMove: angel = " + angle);
                mListener.onSliding(angle);
            }
        }
    }

    /**
     * 检测手指是否顺时针滑动
     *
     * @param x 当前手指的x坐标
     * @param y 当前手指的y坐标
     * @return 是否顺时针
     */
    private boolean isClockwise(float x, float y) {
        isShouldBeGetY = Math.abs(y - mPreY) > Math.abs(x - mPreX);

        return isShouldBeGetY ? x < mPivotX != y > mPreY : y < mPivotY == x > mPreX;
    }

    private void startFling() {
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 处理惯性滚动
     */
    private void computeInertialSliding() {
        checkIsRecycled();
        if (mScroller.computeScrollOffset()) {
            //获取滑动的距离
            float dis = ((isShouldBeGetY ? mScroller.getCurrY() : mScroller.getCurrX()) * mScrollAvailabilityRatio);

            if (mLastScrollOffset != 0) {

                float offset = fixAngle(Math.abs(dis - mLastScrollOffset));
                mListener.onSliding(isClockwiseScrolling ? offset : -offset);
            }
            mLastScrollOffset = dis;

            startFling();
        } else if (mScroller.isFinished()) {
            mLastScrollOffset = 0;
            if (mSlideFinishListener != null) {
                mSlideFinishListener.onSlideFinished();
            }
        }
    }

    /**
     * 调整角度，使其在360之间
     *
     * @param rotation 当前角度
     * @return 调整后的角度
     */
    private float fixAngle(float rotation) {
        float angle = 360F;
        if (rotation < 0) {
            rotation += angle;
        }
        if (rotation > angle) {
            rotation = rotation % angle;
        }
        return rotation;
    }

    public void setSlideFinishListener(OnSlideFinishListener mSlideFinishListener) {
        this.mSlideFinishListener = mSlideFinishListener;
    }

    /**
     * 检查资源释放已经释放
     */
    private void checkIsRecycled() {
        if (isRecycled) {
            throw new IllegalStateException("ArcSlidingHelper is recycled!");
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        checkIsRecycled();
        mScroller = null;
        mVelocityTracker.recycle();
        mVelocityTracker = null;
        mListener = null;
        mHandler = null;
        isRecycled = true;
    }


    public interface OnSlidingListener {
        /**
         * 本次滑动的角度
         */
        void onSliding(float angele);
    }


    /**
     * 监听滚动完毕
     */
    public interface OnSlideFinishListener {
        /**
         * 滚动完毕
         */
        void onSlideFinished();
    }

    /**
     * 主线程回调惯性滚动
     */
    private static class InertialSlidingHandler extends Handler {

        ArcSlidingHelper mHelper;

        InertialSlidingHandler(ArcSlidingHelper helper) {
            mHelper = helper;
        }

        @Override
        public void handleMessage(Message msg) {
            mHelper.computeInertialSliding();
        }
    }
}
