package com.hd.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class YuanJiaoImageView extends ImageView {



    Paint paint;
    private Path path;

    public YuanJiaoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
    }



    protected void onDraw(Canvas canvas) {
        int canvasWidth = getWidth();
        if (canvasWidth == 0) {
            return;
        }



        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG|Paint.ANTI_ALIAS_FLAG));
//        path.cubicTo(0, 0, 100, 0, 100, 100);
//        path.cubicTo(100, 100, 0, 100, 0, 0);

        //绘制圆角imageview
//

        asRoundRect();
        canvas.clipPath(path);
        super.onDraw(canvas);
    }




    private void asRound(){
              path.addCircle(getWidth()/2,getHeight()/2,Math.min(getWidth()/2,getHeight()/2),Path.Direction.CW);
    }

    private void asRoundRect(){
        path.addRoundRect(new RectF(0,0,getWidth(),getHeight()),50,50,Path.Direction.CW);
    }
}