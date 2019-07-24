package com.hd.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Note：照相覆盖的边框
 * Created by Liuguodong on 2018/11/24 16:28
 * E-Mail Address：986850427@qq.com
 */
public class CameraCoverView extends View {
    float borderPercentWidth = 0.1f;//0~0.5
    float borderPercentHeight = 0.1f;
    float strokeWidth = 1f;

    private Paint paint;


    public CameraCoverView(Context context) {
        super(context);
        initViews();
    }

    public CameraCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CameraCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    @TargetApi(21)
    public CameraCoverView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    private void initViews() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);

        this.borderPercentWidth = 0.2F;
        this.borderPercentHeight =0.2F;
        this.radius = 50;
        this.color = 0xAA000000;
    }

    float radius;
    int color;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasWidth = getWidth();
        if (canvasWidth == 0) {
            return;
        }



//
        fun2(canvas);

    }

    private void fun(Canvas canvas){
        int canvasWidth = getWidth();
        if (canvasWidth == 0) {
            return;
        }

        int canvasHeight = getHeight();
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawColor(color);

        float dividerWidth = canvasWidth * borderPercentWidth;
        float dividerHeight = canvasHeight * borderPercentHeight;
        paint.setColor(Color.WHITE);

        drawRect(canvas, dividerWidth, dividerHeight, canvasWidth - dividerWidth, canvasHeight - dividerHeight, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        dividerWidth += strokeWidth;
        dividerHeight += strokeWidth;
        drawRect(canvas, dividerWidth, dividerHeight, canvasWidth - dividerWidth, canvasHeight - dividerHeight, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    private void fun2(Canvas canvas){
        canvas.save();
        Path path=new Path();
        path.addRoundRect(rectF,50,50,Path.Direction.CW);
        canvas.clipPath(path,Region.Op.DIFFERENCE);
        canvas.drawColor(color);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rectF.left=100;
        rectF.top=100;
        rectF.right=getWidth()-100;

        rectF.bottom=getHeight()-100;
    }

    RectF rectF = new RectF();

    private void drawRect(Canvas canvas, float left, float top, float right, float bottom, Paint paint) {
        rectF.left = left;
        rectF.top = top;
        rectF.right = right;
        rectF.bottom = bottom;
        canvas.drawRoundRect(rectF, radius, radius, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
