package com.lxk.animandview.practice.qqzone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.lxk.animandview.utils.DensityUtils;

import java.util.Random;

/**
 * @author https://github.com/103style
 * @date 2020/4/29 17:28
 */
public class GhostView extends View {
    /**
     * 内容
     */
    private Bitmap mBitmap;
    /**
     * 手指按下时的坐标
     */
    private float mDownX, mDownY;
    /**
     * 手指按下时，在屏幕上的绝对X,Y值
     */
    private float mDownRawX, mDownRawY;
    /**
     * Bitmap的中心点
     */
    private float mBitmapCenterX, mBitmapCenterY;
    /**
     * 当前手指在屏幕上的绝对坐标点
     */
    private float mCurrentRawX, mCurrentRawY;
    /**
     * 手指按下的角度
     */
    private float mStartAngle;
    /**
     * 当前角度
     */
    private float mCurrentAngle;
    /**
     * 手指按下时，是否偏向View的左边
     */
    private boolean isLeanLeft;
    /**
     * 应用旋转的矩阵
     */
    private Matrix mMatrix;
    /**
     * Bitmap的边界(通过mapRect映射后的矩形)
     */
    private RectF mBitmapRect;
    /**
     * 滑出屏幕的回调
     */
    private OnOutOfScreenListener mOnOutOfScreenListener;
    /**
     * 是否已经开始惯性移动
     */
    private boolean isFlinging;
    /**
     * 目标的方向
     */
    private int mTargetOrientation;

    private Paint mPaint;
    /**
     * 触摸视图的宽高
     */
    private int childW, childH;

    GhostView(Context context, OnOutOfScreenListener listener) {
        super(context);
        mOnOutOfScreenListener = listener;
        mMatrix = new Matrix();
        mBitmapRect = new RectF();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DensityUtils.dpToPx(context, 2));
        mPaint.setColor(Color.BLACK);
    }


    public void setViewWH(int width, int height) {
        childW = width;
        childH = height;
    }

    /**
     * 当此方法被调用时，表示已经开始了拖动
     *
     * @param event  触摸事件
     * @param bitmap View所对应的Bitmap
     */
    void onDown(MotionEvent event, Bitmap bitmap) {
        //当前手指在屏幕中的绝对坐标值
        mCurrentRawX = mDownRawX = event.getRawX();
        mCurrentRawY = mDownRawY = event.getRawY();
        //在View内的坐标值
        mDownX = event.getX();
        mDownY = event.getY();
        //计算出Bitmap的Left值和Top值
        float l = mCurrentRawX - mDownX, t = mCurrentRawY - mDownY;
        //根据Bitmap的Left和Top分别得出Bitmap的中心点位置
        mBitmapCenterX = l + childW / 2F;
        mBitmapCenterY = t + childH / 2F;
        //根据手指当前位置与Bitmap中心点位置计算出旋转角度
        mStartAngle = getRotationAngle(mBitmapCenterX, mBitmapCenterY, mCurrentRawX, mCurrentRawY);
        //Bitmap宽度的一半
        float halfWidth = bitmap.getWidth() / 2F;
        //如果手指在View内的X值小于Bitmap宽度的一半，那么手指的位置就是在View的左边
        isLeanLeft = mDownX < halfWidth;

        mBitmap = bitmap;
        //通知draw一下
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {

            //计算跟随旋转的视图对应的的l,t,r,b值
            float l = mCurrentRawX - mDownX, t = mCurrentRawY - mDownY;
            float r = l + childW;
            float b = t + childH;
            //应用到这个矩形里面
            mBitmapRect.set(l, t, r, b);
            //旋转操作，旋转中心就是手指的当前位置
            mMatrix.setRotate(mCurrentAngle, mCurrentRawX, mCurrentRawY);
            //映射矩形
            mMatrix.mapRect(mBitmapRect);

            //将进行过旋转操作后的矩阵应用到Canvas里
            canvas.setMatrix(mMatrix);
            //画出内容
            canvas.drawBitmap(mBitmap, l, t, null);
            //绘制当前的状态
            canvas.setMatrix(null);
            canvas.drawCircle(mBitmapCenterX, mBitmapCenterY, DensityUtils.dpToPx(getContext(), 4), mPaint);
            canvas.drawLine(mBitmapCenterX, 0, mBitmapCenterX, getMeasuredHeight(), mPaint);
            canvas.drawLine(0, mBitmapCenterY, getMeasuredWidth(), mBitmapCenterY, mPaint);
            canvas.drawLine(mBitmapCenterX, mBitmapCenterY, mCurrentRawX, mCurrentRawY, mPaint);
            canvas.drawCircle(mCurrentRawX, mCurrentRawY, DensityUtils.dpToPx(getContext(), 16), mPaint);
            canvas.drawRect(mBitmapRect, mPaint);

            //检查是否超出边界，如果超出边界则回调监听器
            if (checkIsContentOutOfScreen()) {
                if (mOnOutOfScreenListener != null) {
                    mOnOutOfScreenListener.onOutOfScreen(this);
                }
            }
        }
    }

    /**
     * 检查Bitmap是否完全draw在屏幕之外
     */
    private boolean checkIsContentOutOfScreen() {
        return mBitmapRect.bottom < 0
                || mBitmapRect.top > getBottom()
                || mBitmapRect.right < 0
                || mBitmapRect.left > getRight();
    }

    /**
     * 获取顺势正的旋转角度
     */
    private float getRotationAngle(float startX, float startY, float endX, float endY) {
        //获取所在象限需要增加的角度
        int appendAngle = calculateAppendAngle(startX, startY, endX, endY);
        //
        //  P (sX,eY)___ E(eX,eY)           S(sX,sY)                 E(eX,eY)           S(sX,sY)____P (eX,sY)
        //          |  /                       /|                        |\                     \  |
        //   象限1  | /               象限3   / |                象限2   | \              象限4   \ |
        //    270  |/                  90   /__|                  180   |__\               0     \|
        //        S(sX,sY)          E(eX,eY)    P (sX,eY)      P (eX,sY)  S(sX,sY)             E(eX,eY)
        //
        //  所以我们需要的锐角就是  角 PSE
        //              __________
        //根据公式 SE = √ PS² + PE² 计算出对角线的长度
        float PS, PE, SE;
        if (appendAngle == 90 || appendAngle == 270) {
            //第一和第三象限
            PS = Math.abs(startY - endY);
            PE = Math.abs(endX - startX);
            SE = (float) Math.sqrt(PS * PS + PE * PE);
        } else {
            //第二和第四象限
            PE = Math.abs(startY - endY);
            PS = Math.abs(endX - startX);
            SE = (float) Math.sqrt(PS * PS + PE * PE);
        }
        double cosPSE = PS / SE;
        float angele = (float) Math.toDegrees(Math.acos(cosPSE));
        return angele + appendAngle;
    }

    /**
     * 获取线方向所在象限 应该增加的顺势正角度
     * 1 ： 270
     * 2 ： 180
     * 3 ： 90
     * 4 ： 0
     */
    private int calculateAppendAngle(float startX, float startY, float endX, float endY) {
        if (endX > startX) {
            // 1、4 象限  endY > startY 说明在下方
            return endY > startY ? 0 : 270;
        } else {
            // 2、3 象限   endY > startY 说明在下方
            return endY > startY ? 90 : 180;
        }
    }

    /**
     * 标记已经开始惯性移动
     */
    void setFlinging() {
        isFlinging = true;
    }

    /**
     * 更新Bitmap的坐标和旋转角度
     *
     * @param offsetX X轴上新的位置（相对）
     * @param offsetY Y轴上新的位置（相对）
     */
    void updateOffset(float offsetX, float offsetY) {
        //更新坐标值
        mCurrentRawX += offsetX;
        mCurrentRawY += offsetY;
        if (isFlinging) {
            //如果已经开始了惯性移动，则每次更新中心点位置
            mBitmapCenterX = mBitmapRect.centerX();
            mBitmapCenterY = mBitmapRect.centerY();
            //解除左右移动不能旋转的束缚
            mDownRawX = mCurrentRawX;
        }
        //更新角度值: 为什么要减去起始角度呢？因为手指按下时的角度不可能每次都是0，
        //这时候移动的话，比如说从90度降到了85度，实际上只是旋转了5度，如果不减去起始角度，
        //在应用旋转时就是85度，这显然是错误的。
        mCurrentAngle = getRotationAngle(mBitmapCenterX, mBitmapCenterY, mDownRawX, mCurrentRawY) - mStartAngle;
        invalidate();
    }

    /**
     * 获取位移动画的起点
     *
     * @return 起点位置
     */
    PointF getAnimationStartPoint() {
        return new PointF(mCurrentRawX, mCurrentRawY);
    }

    /**
     * 获取位移动画的终点
     *
     * @return 终点位置
     */
    PointF getAnimationEndPoint() {
        //屏幕一半宽度
        float halfWidth = mBitmapCenterX;
        //屏幕一半高度
        float halfHeight = mBitmapCenterY;
        //以屏幕中心为起点，手指向左边移动相对于屏幕宽度一半的百分比距离
        float leftPercent = 1F - mCurrentRawX / halfWidth;
        //同上，此为向右
        float rightPercent = (mCurrentRawX - halfWidth) / halfWidth;
        //向上
        float topPercent = 1F - mCurrentRawY / halfHeight;
        //向下
        float bottomPercent = (mCurrentRawY - halfHeight) / halfHeight;

        //取其中最大值
        float max = Math.max(Math.max(leftPercent, rightPercent), Math.max(topPercent, bottomPercent));

        //反正一移动出屏幕就会移除View并中断动画，并且我们需要在任何地方的移动速度都不变，所以我们的距离可以指定为屏幕高度 + View高度
        int maxBitmapLength = Math.max(childW, childH);

        float distance = Math.max(getWidth(), getHeight()) + maxBitmapLength;

        //一个随机大小的偏移量，范围: -maxBitmapLength ~ maxBitmapLength
        int offset = -maxBitmapLength + new Random().nextInt(maxBitmapLength * 2);
        float toX, toY;
        //根据手指在四个方向上移动距离最长的那一方作为目标落点方向
        if (max == leftPercent) {
            toX = -distance;
            toY = offset;
            //记录当前方向
            mTargetOrientation = Orientation.LEFT;
        } else if (max == rightPercent) {
            toX = mCurrentRawX + distance;
            toY = offset;
            //记录当前方向
            mTargetOrientation = Orientation.RIGHT;
        } else if (max == topPercent) {
            toX = offset;
            toY = -distance;
            //记录当前方向
            mTargetOrientation = Orientation.TOP;
        } else {
            toX = offset;
            toY = mCurrentRawY + distance;
            //记录当前方向
            mTargetOrientation = Orientation.BOTTOM;
        }
        return new PointF(toX, toY);
    }

    /**
     * 播放位移动画时的帧更新回调
     *
     * @param location 新的位置（绝对）
     */
    void onAnimationUpdate(PointF location) {
        //更新坐标值
        mCurrentRawX = location.x;
        mCurrentRawY = location.y;
        invalidate();
    }


    public interface OnOutOfScreenListener {
        /**
         * 当Canvas的内容全部draw在View的边界外面时回调此方法
         *
         * @param view 发生事件所对应的View
         */
        void onOutOfScreen(GhostView view);
    }
}
