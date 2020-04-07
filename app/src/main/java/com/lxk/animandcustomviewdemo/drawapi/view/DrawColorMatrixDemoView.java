package com.lxk.animandcustomviewdemo.drawapi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;

import com.lxk.animandcustomviewdemo.R;
import com.lxk.animandcustomviewdemo.drawapi.Utils;

/**
 * @author https://github.com/103style
 * @date 2020/4/7 16:06
 * <p>
 * 学习文档：https://blog.csdn.net/harvic880925/article/details/51187277
 */
public class DrawColorMatrixDemoView extends View {
    private Paint fillPaint, textPaint;
    private Path path;
    private int width, height;
    private Bitmap bitmap;

    public DrawColorMatrixDemoView(Context context) {
        super(context);
        fillPaint = Utils.initPaint(context, Paint.Style.FILL);
        fillPaint.setStrokeWidth(Utils.doToPx(context, 2));
        textPaint = Utils.initPaint(context, Paint.Style.FILL);
        textPaint.setTextSize(Utils.doToPx(context, 12));
        textPaint.setColor(Color.WHITE);
        path = new Path();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.argb(255, 255, 255, 255));
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————
        int gapW = width / 10;
        int gapH = height / 10;

        fillPaint.setColor(Color.argb(255, 103, 58, 183));
        Rect rect = new Rect(0, 0, 2 * gapW, gapH);
        draw(canvas, rect, gapH, "原色");

        canvas.save();
        int translateX = gapW * 5 / 2;
        int translateY = gapH * 2;
        //只原色的保留透明度和蓝色通道
        fillPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                0, 0, 0, 0, 0,//R
                0, 0, 0, 0, 0,//G
                0, 0, 1, 0, 0,//B
                0, 0, 0, 1, 0,//A
        })));
        canvas.translate(translateX, 0);
        draw(canvas, rect, gapH, "蓝色通道");

        //只保留透明度和绿色通道
        fillPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                0, 0, 0, 0, 0,//R
                0, 1, 0, 0, 0,//G
                0, 0, 0, 0, 0,//B
                0, 0, 0, 1, 0,//A
        })));
        canvas.translate(translateX, 0);
        draw(canvas, rect, gapH, "绿色通道");


        //只原色的保留透明度和红色通道
        fillPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                1, 0, 0, 0, 0,//R
                0, 0, 0, 0, 0,//G
                0, 0, 0, 0, 0,//B
                0, 0, 0, 1, 0,//A
        })));
        canvas.translate(translateX, 0);
        draw(canvas, rect, gapH, "红色通道");

        //恢复平移前的状态
        canvas.restore();


        canvas.save();

        //色彩增强1.2倍
        fillPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                1.2f, 0, 0, 0, 0,//R
                0, 1.2f, 0, 0, 0,//G
                0, 0, 1.2f, 0, 0,//B
                0, 0, 0, 1, 0,//A
        })));
        canvas.translate(0, translateY);
        draw(canvas, rect, gapH, "RGB * 1.2");


        //色彩增强1.2倍
        fillPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                0.8f, 0, 0, 0, 0,//R
                0, 0.8f, 0, 0, 0,//G
                0, 0, 0.8f, 0, 0,//B
                0, 0, 0, 1, 0,//A
        })));
        canvas.translate(translateX, 0);
        draw(canvas, rect, gapH, "RGB * 0.8");


        //黑白
        fillPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                0.213f, 0.715f, 0.072f, 0, 0,//R
                0.213f, 0.715f, 0.072f, 0, 0,//G
                0.213f, 0.715f, 0.072f, 0, 0,//B
                0, 0, 0, 1, 0,//A
        })));
        canvas.translate(translateX, 0);
        draw(canvas, rect, gapH, "黑白");


        //绿红反色
        fillPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                0, 1, 0, 0, 0,//R
                1, 0, 0, 0, 0,//G
                0, 0, 1, 0, 0,//B
                0, 0, 0, 1, 0,//A
        })));
        canvas.translate(translateX, 0);
        draw(canvas, rect, gapH, "绿红反色");

        //恢复平移前的状态
        canvas.restore();

        //饱和度测试
        colorSaturationChange(canvas, rect, gapH, translateX, translateY);

        //色彩旋转
        colorRotateChange(canvas, rect, gapH, translateX, translateY);
    }


    private void draw(Canvas canvas, Rect rect, int gapH, String txt) {
        canvas.drawRect(rect, fillPaint);
        canvas.drawBitmap(bitmap, 0, gapH, fillPaint);
        canvas.drawText(txt, 0, gapH / 2, textPaint);
    }


    /**
     * 饱和度测试
     */
    private void colorSaturationChange(Canvas canvas, Rect rect, int gapH, int translateX, int translateY) {
        canvas.save();
        canvas.translate(0, translateY * 2);
        ColorMatrix mSaturationMatrix = new ColorMatrix();

        //设置饱和度为 0.5
        mSaturationMatrix.setSaturation(0.5f);
        fillPaint.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
        draw(canvas, rect, gapH, "饱和度设为0.5");


        canvas.translate(translateX, 0);
        //设置饱和度为 1
        mSaturationMatrix.setSaturation(1f);
        fillPaint.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
        canvas.drawRect(rect, fillPaint);
        canvas.drawBitmap(bitmap, 0, gapH, fillPaint);
        draw(canvas, rect, gapH, "饱和度设为1");

        canvas.translate(translateX, 0);
        //设置饱和度为 2
        mSaturationMatrix.setSaturation(2);
        fillPaint.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
        draw(canvas, rect, gapH, "饱和度设为2");


        canvas.translate(translateX, 0);
        //设置饱和度为 5
        mSaturationMatrix.setSaturation(5f);
        fillPaint.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
        draw(canvas, rect, gapH, "饱和度设为5");
        canvas.restore();
    }


    /**
     * 色彩旋转测试
     */
    private void colorRotateChange(Canvas canvas, Rect rect, int gapH, int translateX, int translateY) {
        canvas.save();
        canvas.translate(0, translateY * 3);
        ColorMatrix rotateMatrix = new ColorMatrix();

        //颜色旋转-30°
        rotateMatrix.setRotate(0, -30);
        fillPaint.setColorFilter(new ColorMatrixColorFilter(rotateMatrix));
        draw(canvas, rect, gapH, "颜色旋转-30°");


        canvas.translate(translateX, 0);
        //颜色旋转30°
        rotateMatrix.setRotate(0, 30);
        fillPaint.setColorFilter(new ColorMatrixColorFilter(rotateMatrix));
        draw(canvas, rect, gapH, "颜色旋转30°");

        canvas.translate(translateX, 0);
        //颜色旋转60°
        rotateMatrix.setRotate(0, 60);
        fillPaint.setColorFilter(new ColorMatrixColorFilter(rotateMatrix));
        draw(canvas, rect, gapH, "颜色旋转60°");


        canvas.translate(translateX, 0);
        //颜色旋转120°
        rotateMatrix.setRotate(0, 120);
        fillPaint.setColorFilter(new ColorMatrixColorFilter(rotateMatrix));
        draw(canvas, rect, gapH, "颜色旋转120°");
        canvas.restore();
    }
}
