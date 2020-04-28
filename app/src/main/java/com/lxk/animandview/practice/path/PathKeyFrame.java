package com.lxk.animandview.practice.path;

import android.graphics.Path;
import android.graphics.PathMeasure;

import androidx.annotation.FloatRange;

/**
 * @author https://github.com/103style
 * @date 2020/4/27 17:51
 * path路径的关键帧（点坐标和角度）
 */
public class PathKeyFrame {
    /**
     * 进度值
     */
    private static final int PRECISION = 1;
    /**
     * 路径上点的 x y 坐标
     */
    private float[] mX, mY;
    /**
     * 路径上每个点的角度
     */
    private float[] mAngel;
    /**
     * 路径长度
     */
    private float pathLength;

    private PathPoint tempPoint;

    public PathKeyFrame(Path path) {
        tempPoint = new PathPoint();
        analysisPath(path);
    }

    /**
     * 解析路径得到点坐标和角度
     */
    private void analysisPath(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathLength = pathMeasure.getLength();
        int pointCount = (int) (pathLength / PRECISION) + 1;
        mX = new float[pointCount];
        mY = new float[pointCount];
        mAngel = new float[pointCount];
        float[] tempPos = new float[2];
        float[] tan = new float[2];
        float dis;
        for (int i = 0; i < pointCount; i++) {
            //获取当前距离
            dis = (i * pathLength) / (pointCount - 1);
            //根据当前距离获取对应的坐标点和正切值
            pathMeasure.getPosTan(dis, tempPos, tan);
            mX[i] = tempPos[0];
            mY[i] = tempPos[1];
            mAngel[i] = fixAngle((float) (Math.atan2(tan[1], tan[0]) * 180F / Math.PI));
        }
    }

    /**
     * 获取小于360的角度正值
     */
    private float fixAngle(float angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    public float getPathLength() {
        return pathLength;
    }

    public PathPoint getValue(@FloatRange(from = 0f, to = 1f) float fraction) {
        if (fraction < 0 || fraction > 1) {
            return null;
        }
        int index = (int) (mX.length * fraction);
        tempPoint.set(mX[index], mY[index], mAngel[index]);
        return tempPoint;
    }

    public void release() {
        mX = null;
        mY = null;
        mAngel = null;
        tempPoint = null;
    }
}
