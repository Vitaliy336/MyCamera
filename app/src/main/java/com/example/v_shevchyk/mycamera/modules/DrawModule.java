package com.example.v_shevchyk.mycamera.modules;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.v_shevchyk.mycamera.R;

public class DrawModule {
    private Activity activity;
    private SurfaceHolder tHolder;
    private SurfaceView transparentView;
    private SurfaceHolder.Callback callback;

    public DrawModule(Activity activity, SurfaceHolder.Callback callback) {
        this.activity = activity;
        this.callback = callback;
        init();
        initListener();
    }

    private void initListener() {
        transparentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("clicked on:", "x= " + motionEvent.getX() + " y= " + motionEvent.getY());
                int l = (int) motionEvent.getX() - 10;
                int r = (int) motionEvent.getX() + 10;
                int u = (int) motionEvent.getY() - 10;
                int d = (int) motionEvent.getY() + 10;

                drawFocusRect(new Rect(l, u, d, r), Color.BLUE);
                return false;
            }
        });
    }

    private void init() {
        transparentView = activity.findViewById(R.id.TransparentView);
        tHolder = transparentView.getHolder();
        tHolder.addCallback(callback);
        tHolder.setFormat(PixelFormat.TRANSPARENT);
        tHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }


    private void drawFocusRect(Rect rect, int color) {
        Canvas canvas = tHolder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(3f);
        canvas.drawRect(rect, paint);

        tHolder.unlockCanvasAndPost(canvas);
        tHolder.removeCallback(callback);
    }
}
