package com.lxk.animandview.practice.arcview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.IntDef;

import com.lxk.animandview.R;
import com.lxk.animandview.utils.DensityUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author https://github.com/103style
 * @date 2020/4/26 10:05
 * <p>
 * 实现 Item之间有一定的角度偏移的布局
 * <p>
 * 参考：https://blog.csdn.net/u011387817/article/details/80788704
 */
public class ArcLayoutView extends ViewGroup implements ArcSlidingHelper.OnSlidingListener {
    /**
     * 中心视图的位置  在 底部 还是 顶部
     */
    public static final int POS_BOTTOM = 0;
    public static final int POS_TOP = 1;
    /**
     * 中心视图的类型 是 视图  还是 颜色
     */
    public static final int TYPE_VIEW = 0;
    public static final int TYPE_COLOR = 1;
    /**
     * 中心视图的位置
     * 中心/左中/左上/左下/右中/右上/右下/中上/中下
     */
    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT_CENTER = 1;
    public static final int GRAVITY_LEFT_TOP = 2;
    public static final int GRAVITY_LEFT_BOTTOM = 3;
    public static final int GRAVITY_RIGHT_CENTER = 4;
    public static final int GRAVITY_RIGHT_TOP = 5;
    public static final int GRAVITY_RIGHT_BOTTOM = 6;
    public static final int GRAVITY_MIDDLE_TOP = 7;
    public static final int GRAVITY_MIDDLE_BOTTOM = 8;
    /**
     * 计算旋转的辅助类
     */
    private ArcSlidingHelper arcSlidingHelper;
    /**
     * 滑动的最短距离
     */
    private int touchSlop;
    /**
     * 记录按下的点坐标  用户处理是否拦截事件
     */
    private float mPreX, mPreY;
    /**
     * 是否拦截事件
     */
    private boolean interceptTouchEvent;
    /**
     * 中心视图类型为color时的 画笔
     */
    private Paint mPaint;
    /**
     * 中心视图
     */
    private View centerView;

    /**
     * 中心视图是否在底部
     */
    private boolean centerViewInBottom;
    /**
     * 中心视图的类型
     */
    private int centerViewType;
    /**
     * 中心视图的半径
     */
    private int centerViewRadius;
    /**
     * 中心视图类型为color时的颜色
     */
    private int centerViewColor;
    /**
     * 中心视图类型为view时的资源id
     */
    private int centerViewLayoutId;
    /**
     * 中心视图类型是否能旋转
     */
    private boolean centerViewCanRotate;

    /**
     * 子view距离中心的偏移量
     */
    private int childOffsetDisWithCenter;
    /**
     * 中心视图的位置
     */
    private int centerViewGravity;
    /**
     * 子view的子view不跟随旋转
     */
    private boolean itemChildNoRotate;
    /**
     * 布局中心点坐标
     */
    private int mPivotX, mPivotY;

    public ArcLayoutView(Context context) {
        this(context, null);
    }

    public ArcLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.argb(128, 0, 0, 0));
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initAttrs(context, attrs);
    }

    /**
     * 读取配置的属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcLayoutView);
        int pos = ta.getInt(R.styleable.ArcLayoutView_alv_center_view_position, POS_BOTTOM);
        centerViewInBottom = pos == POS_BOTTOM;
        itemChildNoRotate = ta.getBoolean(R.styleable.ArcLayoutView_alv_item_child_no_rotate, false);
        centerViewGravity = ta.getInt(R.styleable.ArcLayoutView_alv_center_view_gravity, GRAVITY_CENTER);
        centerViewRadius = ta.getDimensionPixelOffset(R.styleable.ArcLayoutView_alv_center_radius, DensityUtils.dpToPx(context, 32));
        centerViewType = ta.getInt(R.styleable.ArcLayoutView_alv_center_view_type, TYPE_COLOR);
        centerViewColor = ta.getColor(R.styleable.ArcLayoutView_alv_center_color, Color.CYAN);
        centerViewLayoutId = ta.getResourceId(R.styleable.ArcLayoutView_alv_center_layout_id, -1);
        centerViewCanRotate = ta.getBoolean(R.styleable.ArcLayoutView_alv_center_view_can_rotate, false);
        childOffsetDisWithCenter = ta.getDimensionPixelOffset(R.styleable.ArcLayoutView_alv_child_offset_dis_with_center, 0);
        if (centerViewTypeIsColor()) {
            //如果是Color类型，就获取轴承的半径，默认：0
            //初始化画笔
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(centerViewColor);
            //使其回调onDraw方法
            setWillNotDraw(false);
        } else {
            if (centerViewLayoutId == -1) {
                throw new IllegalArgumentException("need set centerViewLayoutId....");
            }
            centerView = LayoutInflater.from(context).inflate(centerViewLayoutId, this, false);
            addView(centerView);
        }
        ta.recycle();
    }

    /**
     * 中心视图是否在底部
     */
    public boolean isCenterViewInBottom() {
        return centerViewInBottom;
    }

    /**
     * 中心视图的类型是否时颜色
     */
    public boolean centerViewTypeIsColor() {
        return centerViewType == TYPE_COLOR;
    }

    /**
     * 中心视图能否旋转
     */
    public boolean centerViewCanRotate() {
        return centerViewCanRotate;
    }

    /**
     * 获取中心视图的对齐方式
     */
    public int getCenterViewGravity() {
        return centerViewGravity;
    }

    /**
     * 设置中心视图的对齐方式
     */
    public void setCenterViewGravity(@CenterViewGravity int centerViewGravity) {
        this.centerViewGravity = centerViewGravity;
        requestLayout();
    }

    /**
     * 中心点是否在右边
     */
    private boolean gravityInRight() {
        return centerViewGravity == GRAVITY_RIGHT_CENTER
                || centerViewGravity == GRAVITY_RIGHT_TOP
                || centerViewGravity == GRAVITY_RIGHT_BOTTOM;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            //获取最大的子View宽度
            int childMaxWidth = 0;
            for (int i = 0; i < getChildCount(); i++) {
                childMaxWidth = Math.max(childMaxWidth, getChildAt(i).getMeasuredWidth());
            }
            widthSize = 2 * centerViewRadius + childOffsetDisWithCenter + childMaxWidth;
        }
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            //获取最大的子View高度
            int childMaxHeight = 0;
            for (int i = 0; i < getChildCount(); i++) {
                childMaxHeight = Math.max(childMaxHeight, getChildAt(i).getMeasuredHeight());
            }
            heightSize = 2 * centerViewRadius + childOffsetDisWithCenter + childMaxHeight;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int[] pos = getPosWithGravity();
        mPivotX = pos[0];
        mPivotY = pos[1];
        //更新SlidingHelper的旋转点
        updateSlidingHelperPivot();

        int startIndex = layoutCenterView(mPivotX, mPivotY);

        int count = getChildCount();
        int angle = 360 / (count - startIndex);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == centerView) {
                continue;
            }
            int halfH = child.getMeasuredHeight() / 2;
            int cWidth = child.getMeasuredWidth();
            int left, cPivotX;
            if (gravityInRight()) {
                left = mPivotX - centerViewRadius - childOffsetDisWithCenter - cWidth;
                cPivotX = cWidth + centerViewRadius + childOffsetDisWithCenter;
            } else {
                left = mPivotX + centerViewRadius + childOffsetDisWithCenter;
                cPivotX = -centerViewRadius - childOffsetDisWithCenter;
            }
            child.layout(left, mPivotY - halfH, left + cWidth, mPivotY + halfH);
            //更新旋转的中心点
            child.setPivotX(cPivotX);
            child.setPivotY(halfH);
            //配置旋转角度 如果中心视图在底部的话 我们索引则减掉1
            int index = isCenterViewInBottom() && !centerViewTypeIsColor() ? i - 1 : i;
            child.setRotation(angle * index);
        }
    }

    private int[] getPosWithGravity() {
        int x = 0, y = 0;
        switch (centerViewGravity) {
            case GRAVITY_LEFT_CENTER:
                y = getMeasuredHeight() / 2;
                break;
            case GRAVITY_LEFT_TOP:
                break;
            case GRAVITY_LEFT_BOTTOM:
                y = getMeasuredHeight();
                break;
            case GRAVITY_RIGHT_CENTER:
                x = getMeasuredWidth();
                y = getMeasuredHeight() / 2;
                break;
            case GRAVITY_RIGHT_TOP:
                x = getMeasuredWidth();
                break;
            case GRAVITY_RIGHT_BOTTOM:
                x = getMeasuredWidth();
                y = getMeasuredHeight();
                break;
            case GRAVITY_MIDDLE_TOP:
                x = getMeasuredWidth() / 2;
                break;
            case GRAVITY_MIDDLE_BOTTOM:
                x = getMeasuredWidth() / 2;
                y = getMeasuredHeight();
                break;
            case GRAVITY_CENTER:
            default:
                x = getMeasuredWidth() / 2;
                y = getMeasuredHeight() / 2;
                break;
        }
        return new int[]{x, y};
    }

    /**
     * 中心视图的布局
     */
    private int layoutCenterView(int mPivotX, int mPivotY) {
        if (centerViewTypeIsColor()) {
            return 0;
        }
        int halfW = centerView.getMeasuredWidth() / 2;
        int halfH = centerView.getMeasuredHeight() / 2;
        //轴承放在旋转中心点上
        centerView.layout(mPivotX - halfW, mPivotY - halfH, mPivotX + halfW, mPivotY + halfH);
        return 1;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //必须不是View类型，并且是在底部才draw
        if (centerViewTypeIsColor() && isCenterViewInBottom()) {
            canvas.drawCircle(mPivotX, mPivotY, centerViewRadius, mPaint);
        }
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        //必须不是View类型，并且是在顶部才draw
        if (centerViewTypeIsColor() && !isCenterViewInBottom()) {
            canvas.drawCircle(mPivotX, mPivotY, centerViewRadius, mPaint);
        }
    }


    /**
     * 重写addview方法 解决centerView在顶部时添加子view会导致 centerView不在顶部的问题
     */
    @Override
    public void addView(View child, int index, LayoutParams params) {
        //中心视图是view类型 并且在顶部时
        boolean needAdd = false;
        if (!isCenterViewInBottom()
                && !centerViewTypeIsColor()
                && getChildCount() > 0
                && child != centerView) {
            if (centerView != null) {
                super.removeView(centerView);
                needAdd = true;
            }
        }
        super.addView(child, index, params);
        if (needAdd) {
            addView(centerView);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (arcSlidingHelper == null) {
            arcSlidingHelper = ArcSlidingHelper.create(this, this);
            arcSlidingHelper.enableInertialSliding(true);
        }
        updateSlidingHelperPivot();
    }

    /**
     * 更新SlidingHelper的旋转点
     */
    private void updateSlidingHelperPivot() {
        if (arcSlidingHelper != null) {
            arcSlidingHelper.updatePivotX(mPivotX);
            arcSlidingHelper.updatePivotY(mPivotY);
        }
    }

    @Override
    public void onSliding(float angele) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == centerView && !centerViewTypeIsColor() && !centerViewCanRotate()) {
                //配置了中心视图不旋转
                continue;
            }
            child.setRotation(child.getRotation() + angele);
            makeItemChildNoRotate(child);
        }
    }

    /**
     * 处理子view的子view不旋转
     */
    private void makeItemChildNoRotate(View child) {
        if (child instanceof ViewGroup && itemChildNoRotate && child != centerView) {
            ViewGroup group = (ViewGroup) child;
            for (int i = 0; i < group.getChildCount(); i++) {
                group.getChildAt(i).setRotation(-group.getRotation());
            }
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (arcSlidingHelper != null) {
            arcSlidingHelper.release();
            arcSlidingHelper = null;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE && interceptTouchEvent) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        float x = ev.getX();
        float y = ev.getY();
        //拦截move事件
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //当手指按下时，停止惯性滚动
                arcSlidingHelper.abortAnimation();
                //记录按下的坐标
                mPreX = x;
                mPreY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(x - mPreX) > touchSlop
                        || Math.abs(y - mPreY) > touchSlop) {
                    interceptTouchEvent = true;
                }
                //更新上一个点的坐标
                arcSlidingHelper.updateMovement(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                interceptTouchEvent = false;
                break;
        }
        return interceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        arcSlidingHelper.handleMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                interceptTouchEvent = false;
                break;
        }
        return true;
    }

    @IntDef({POS_BOTTOM, POS_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CenterViewPosition {
    }

    @IntDef({TYPE_VIEW, TYPE_COLOR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CenterViewType {
    }

    @IntDef({GRAVITY_CENTER, GRAVITY_LEFT_CENTER, GRAVITY_LEFT_TOP, GRAVITY_LEFT_BOTTOM,
            GRAVITY_RIGHT_CENTER, GRAVITY_RIGHT_TOP, GRAVITY_RIGHT_BOTTOM,
            GRAVITY_MIDDLE_TOP, GRAVITY_MIDDLE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CenterViewGravity {
    }

}
