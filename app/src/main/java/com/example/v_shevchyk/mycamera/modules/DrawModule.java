package com.example.v_shevchyk.mycamera.modules;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;

public class DrawModule extends SurfaceView {

    private final Paint paint;
    private final SurfaceHolder mHolder;
    private final Activity activity;
    public DrawModule(Activity activity) {
        super(activity);
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        this.activity = activity;
        paint =  new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        Log.e("Touch", event.getX() + " " + event.getY());
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            invalidate();
            if(mHolder.getSurface().isValid()){
                final Canvas canvas = mHolder.lockCanvas();
                if(canvas != null){
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    canvas.drawColor(Color.TRANSPARENT);
                    canvas.drawCircle(event.getX(), event.getY(), 100, paint);
                    mHolder.unlockCanvasAndPost(canvas);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Canvas canvas1 = mHolder.lockCanvas();
                            if(canvas1 != null){
                                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                                mHolder.unlockCanvasAndPost(canvas1);
                            }
                        }
                    }, 1000);
                }
            }
        }
        return false;
    }
}
