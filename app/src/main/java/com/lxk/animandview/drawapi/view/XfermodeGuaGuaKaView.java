package com.lxk.animandview.drawapi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lxk.animandview.R;
import com.lxk.animandview.utils.DensityUtils;

/**
 * @author https://github.com/103style
 * @date 2020/4/8 11:43
 * doc:https://blog.csdn.net/harvic880925/article/details/51264653
 * doc:https://developer.android.com/reference/android/graphics/PorterDuff.Mode.html
 */
public class XfermodeGuaGuaKaView extends View {

    private Paint fillPaint, strokePaint, textPaint;
    private int width, height;
    private Bitmap src, dst;
    private Path mPath;
    private Region region;
    private int perX, perY;

    //因为这边只是测试  所以只写了一个参数的构造方法， 各位按需重写
    public XfermodeGuaGuaKaView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        fillPaint = DensityUtils.initPaint(context, Paint.Style.FILL);
        strokePaint = DensityUtils.initPaint(context, Paint.Style.STROKE);
        strokePaint.setStrokeWidth(DensityUtils.dpToPx(context, 16));
        strokePaint.setColor(Color.WHITE);
        textPaint = DensityUtils.initPaint(context, Paint.Style.FILL);
        textPaint.setTextSize(DensityUtils.dpToPx(context, 20));
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        src = BitmapFactory.decodeResource(getResources(), R.drawable.guaguaka);
        Matrix matrix = new Matrix();
        float scale = width * 1.0f / src.getWidth();
        matrix.setScale(scale, scale);
        src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        createDst(src);
        region = new Region(0, 0, src.getWidth(), src.getHeight());
    }

    private void createDst(Bitmap src) {
        dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        String txt = "想中奖，就来关注公众号 103Tech ！";
        Canvas canvas = new Canvas(dst);
        canvas.drawText(txt, (dst.getWidth() - textPaint.measureText(txt)) / 2, dst.getHeight() / 2, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (region.contains(x, y)) {
                    mPath.moveTo(x, y);
                    perX = x;
                    perY = y;
                }
                if (y > src.getHeight() + DensityUtils.dpToPx(getContext(), 8)) {
                    ViewGroup group = (ViewGroup) getParent();
                    group.removeView(this);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (region.contains(x, y)) {
                    mPath.quadTo(perX, perY, x, y);
                    perX = x;
                    perY = y;
                }
                break;
            default:
                break;
        }

        if (region.contains(x, y)) {
            postInvalidate();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置画布背景
        canvas.drawColor(Color.WHITE);
        //—————————— 以下为示例  开发时不要在 onDraw中通过new创建对象 ——————————


        canvas.drawBitmap(dst, 0, 0, fillPaint);

        int layerId = canvas.saveLayer(0, 0, width, height, fillPaint);

        //使用 SRC_OUT 模式  去掉 src 和  mPath 重叠的部分
        canvas.drawPath(mPath, strokePaint);
        strokePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //类似google示例中dst src 的完全重叠
        canvas.drawBitmap(src, 0, 0, strokePaint);
        //还原图层
        strokePaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }
}
