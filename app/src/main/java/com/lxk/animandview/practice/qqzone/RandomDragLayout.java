package com.lxk.animandview.practice.qqzone;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.lxk.animandview.utils.ActivityUtils;
import com.lxk.animandview.viewgroup.MarginLayoutParamsViewGroup;

/**
 * @author https://github.com/103style
 * @date 2020/4/29 15:35
 * 参考doc: https://blog.csdn.net/u011387817/article/details/84136291
 * <p>
 * 方qq空间头部广告拖动动画
 */
public class RandomDragLayout extends MarginLayoutParamsViewGroup {
    private static final String TAG = "RandomDragLayout";
    /**
     * 松开手之后的动画时长
     */
    private final long ANIMATOR_DURATION = 1500;
    /**
     * 触发惯性滑动的最低速度
     */
    private final int ScrollerVelocityMIN = 3000;
    /**
     * 自定义Evaluator，使ValueAnimator支持PointF
     */
    private TypeEvaluator<PointF> mEvaluator;
    /**
     * 最短滑动距离
     */
    private int touchSlop;
    /**
     * 唯一的 子view
     */
    private View mChild;
    /**
     * 是否已经在拖动状态 用于拦截事件用
     */
    private boolean isDragged;
    /**
     * 上一个触摸事件的走镖
     */
    private float mPreX, mPreY;
    /**
     * 滑动速度测量
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 展示拖动效果的视图
     */
    private GhostView mGhostView;
    /**
     * mGhostView 是否以显示
     */
    private boolean isGhostViewShown;
    /**
     * 处理惯性滑动
     */
    private Scroller mScroller;
    /**
     * 当前视图相同大小的画布
     */
    private Canvas mCanvas;
    /**
     * 当前视图相同大小的bitmap
     */
    private Bitmap mBitmap;
    /**
     * activity 根视图
     */
    private ViewGroup mRootView;
    /**
     * 惯性滑动当前对应的 x y 值
     */
    private float mLastScrollOffsetX, mLastScrollOffsetY;
    /**
     * 非惯性的位移动画
     */
    private ValueAnimator mAnimator;
    /**
     * 是否在松开手指之后进行惯性滑动
     */
    private boolean isFling;

    /**
     * 可触发拖动的区域
     */
    private Region childRegion;
    /**
     * down事件是否在 childRegion 内
     */
    private boolean touchInTheRegion;

    public RandomDragLayout(Context context) {
        this(context, null);
    }

    public RandomDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mVelocityTracker = VelocityTracker.obtain();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        mRootView = ActivityUtils.getRootView(context);
        childRegion = new Region();
    }

    /**
     * 重写父类addView方法，仅允许拥有一个直接子View
     */
    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("RandomDragLayout can only contain 1 child!");
        }
        super.addView(child, index, params);
        mChild = child;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChild == null) {
            throw new IllegalArgumentException("RandomDragLayout must have one child!");
        }
        measureChild(mChild, widthMeasureSpec, heightMeasureSpec);
        childRegion.set(mChild.getLeft(), mChild.getTop(), mChild.getRight(), mChild.getBottom());
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        MarginLayoutParams layoutParams = (MarginLayoutParams) mChild.getLayoutParams();

        //根据设置的宽高模式来配置宽高
        if (widthMode == MeasureSpec.EXACTLY) {
            //match_parent or 指定值
            width = widthSize;
        } else {
            //wrap_content
            width = mChild.getMeasuredWidth()
                    + getPaddingLeft() + getPaddingRight() //内边距
                    + layoutParams.leftMargin + layoutParams.rightMargin;//child的外边距
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mChild.getMeasuredHeight()
                    + getPaddingTop() + getPaddingBottom() //内边距
                    + layoutParams.topMargin + layoutParams.bottomMargin;//child的外边距
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mChild.getLayoutParams();
        //获取左上点的位置  右下则为左上+宽高
        int left = getPaddingLeft() + layoutParams.leftMargin;
        int top = getPaddingTop() + layoutParams.topMargin;
        mChild.layout(left, top, left + mChild.getMeasuredWidth(), top + mChild.getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            //更新画布尺寸
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
    }

    /**
     * 处理拦截事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return false;
        }
        //是否已经在拖动状态
        boolean dragging = ev.getAction() == MotionEvent.ACTION_MOVE && isDragged;
        if (dragging || super.onInterceptTouchEvent(ev)) {
            //禁止父类拦截
            requestDisallowInterceptTouchEvent(true);
            return true;
        }
        float x = ev.getX(), y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchInTheRegion = childRegion.contains((int) x, (int) y);
                mPreX = x;
                mPreY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = x - mPreX;
                float offsetY = y - mPreY;
                if (Math.abs(offsetX) > touchSlop || Math.abs(offsetY) > touchSlop) {
                    //当滑动距离超过 touchSlop 之后 拦截事件
                    mPreX = x;
                    mPreY = y;
                    //标记已经开始拖拽
                    isDragged = touchInTheRegion;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                //手指松开后，要重置拖拽状态
                isDragged = false;
                touchInTheRegion = false;
                break;
            default:
                break;
        }
        requestDisallowInterceptTouchEvent(isDragged);
        return isDragged;
    }

    /**
     * 是否在惯性运动状态
     */
    private boolean isAnimRunning() {
        return !mScroller.isFinished() || (mAnimator != null && mAnimator.isRunning());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!touchInTheRegion) {
            return false;
        }
        if (isAnimRunning()) {
            return true;
        }
        float x = event.getX(), y = event.getY();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //处理拖动事件
                handleActionMove(event, x, y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                //惯性运动状态
                handleActionUp();
                break;
            default:
                break;
        }
        mPreX = x;
        mPreY = y;
        return true;
    }

    /**
     * 处理拖动事件
     */
    private void handleActionMove(MotionEvent event, float x, float y) {
        if (isGhostViewShown) {
            //如果GhostView已经在显示的话，直接更新坐标
            mGhostView.updateOffset(x - mPreX, y - mPreY);
        } else {
            //如果还没添加，先把子View显示的东西都draw到mBitmap上
            mChild.draw(mCanvas);
            //隐藏真实的View
            mChild.setVisibility(INVISIBLE);
            //初始化GhostView
            initializeGhostView();
            //添加到根视图，宽高=屏幕尺寸
            mRootView.addView(mGhostView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            event.offsetLocation(-mChild.getLeft(), -mChild.getTop());
            //回调GhostView的onDown方法，表示已经开始了拖拽
            mGhostView.onDown(event, mBitmap);
            //标记一下状态
            isGhostViewShown = true;
        }
    }

    /**
     * 创建GhostView
     */
    private void initializeGhostView() {
        //防止重复添加
        if (mGhostView != null) {
            mRootView.removeView(mGhostView);
        }
        mGhostView = new GhostView(getContext(),
                view -> {
                    //重置状态
                    reset();
                });
        mGhostView.setViewWH(mChild.getWidth(), mChild.getHeight());
    }

    /**
     * 恢复初始状态
     */
    public void reset() {
        abortAnimation();
        isFling = false;
        touchInTheRegion = false;
        isDragged = false;
        mChild.setVisibility(VISIBLE);
        if (mRootView != null) {
            //防止报：Attempt to read from field 'int android.view.View.mViewFlags' on a null object reference
            post(() -> {
                //惯性移动完毕，从Activity顶层视图移除GhostView
                mRootView.removeView(mGhostView);
                mGhostView = null;
                isGhostViewShown = false;
            });
        }
        //惯性移动完毕，重置偏移量
        mLastScrollOffsetX = 0;
        mLastScrollOffsetY = 0;
    }

    /**
     * 处理松开手指之后的惯性运动
     */
    private void handleActionUp() {
        if (mGhostView != null) {
            //标记状态：已经不是在拖拽中了
            isDragged = false;
            //计算当前滑动速度
            mVelocityTracker.computeCurrentVelocity(1000);
            float xVelocity = mVelocityTracker.getXVelocity();
            float yVelocity = mVelocityTracker.getYVelocity();
            //X轴和Y轴其中一个的滑动速率超过500，则视为有滑动速率，这时候要进行惯性移动
            if (Math.abs(xVelocity) > ScrollerVelocityMIN
                    || Math.abs(yVelocity) > ScrollerVelocityMIN) {
                startFling(xVelocity, yVelocity);
            } else {
                //否则播放位移动画
                startAnimator();
            }
        }
    }

    /**
     * 开始惯性滑动
     */
    private void startFling(float xVelocity, float yVelocity) {
        isFling = true;
        //先标记开始惯性移动
        mGhostView.setFlinging();
        //开始惯性滑动
        mScroller.fling(0, 0, (int) xVelocity, (int) yVelocity,
                Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        //触发调用computeScroll
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (!isFling) {
            //当前不是mScroller触发的惯性滑动状态
            return;
        }
        if (mScroller.computeScrollOffset()) {
            float x = mScroller.getCurrX();
            float y = mScroller.getCurrY();
            //更新坐标 相对上次的差值  类似 scrollBy
            mGhostView.updateOffset(x - mLastScrollOffsetX, y - mLastScrollOffsetY);
            //更新上一次的坐标值
            mLastScrollOffsetX = x;
            mLastScrollOffsetY = y;

            //继续通知回调
            invalidate();
        } else if (mScroller.isFinished()) {
            Log.e(TAG, "computeScroll: mScroller is finished ");
        }
    }


    /**
     * 开始惯性动画
     */
    private void startAnimator() {
        mAnimator = ValueAnimator
                .ofObject(
                        getEvaluator(),
                        mGhostView.getAnimationStartPoint(),
                        mGhostView.getAnimationEndPoint())
                .setDuration(ANIMATOR_DURATION);

        mAnimator.addUpdateListener(animation -> {
            //防止内容在已超出屏幕之后(这时候GhostView已移除和置空)还继续更新位置
            if (mGhostView != null) {
                mGhostView.onAnimationUpdate((PointF) animation.getAnimatedValue());
            }
        });
        mAnimator.start();
    }

    /**
     * 配置动画改变的值
     */
    private TypeEvaluator<PointF> getEvaluator() {
        if (mEvaluator == null) {
            mEvaluator = new TypeEvaluator<PointF>() {

                //对象复用，小幅度提升运行效率和降低内存占用
                private final PointF temp = new PointF();

                @Override
                public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                    //X轴总距离
                    float totalX = endValue.x - startValue.x;
                    //Y轴总距离
                    float totalY = endValue.y - startValue.y;
                    //当前绝对坐标值 = 开始坐标值 + 相对坐标值
                    float x = startValue.x + (totalX * fraction);
                    float y = startValue.y + (totalY * fraction);
                    //更新数值
                    temp.set(x, y);
                    return temp;
                }
            };
        }
        return mEvaluator;
    }


    /**
     * 打断动画
     */
    private void abortAnimation() {
        mScroller.abortAnimation();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

}
