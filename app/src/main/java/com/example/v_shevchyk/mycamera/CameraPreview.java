package com.example.v_shevchyk.mycamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by v_shevchyk on 09.03.18.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceHolder mHolder;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("s", "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mCamera.stopPreview();
        setCameraDisplayOrientation(0);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d("s", "Error starting camera preview: " + e.getMessage());
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private void setCameraDisplayOrientation(int camID) {
        int rotation = 90;
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
            case Surface.ROTATION_90:
                degrees = 90;
            case Surface.ROTATION_180:
                degrees = 180;
            case Surface.ROTATION_270:
                degrees = 270;
        }
        int res = 0;

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(camID, cameraInfo);

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            res = ((360 - degrees) + cameraInfo.orientation);
        } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            res = ((360 - degrees) - cameraInfo.orientation);
            res += 360;
        }

        res = res % 360;
        mCamera.setDisplayOrientation(res);
    }
}
