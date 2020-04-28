package com.lxk.animandview.practice.path;

import android.graphics.PointF;

import java.util.Locale;

/**
 * @author https://github.com/103style
 * @date 2020/4/28 9:59
 */
public class PathPoint extends PointF {
    /**
     * 在路径上的位置 (百分比)
     */
    public float fraction;

    /**
     * item所对应的索引
     */
    public int index;

    /**
     * item的旋转角度
     */
    private float angle;

    public PathPoint() {
    }

    private PathPoint(int index, float x, float y, float angle) {
        super(x, y);
        this.angle = angle;
        this.index = index;
    }

    public PathPoint(PathPoint p, int index, float fraction) {
        this(index, p.x, p.y, p.angle);
        this.fraction = fraction;
    }


    public void set(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public float getChildAngle() {
        return angle;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "x: %f\ty: %f\tangle: %f", x, y, angle);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof PathPoint ? (index == ((PathPoint) obj).index) : this == obj;
    }
}