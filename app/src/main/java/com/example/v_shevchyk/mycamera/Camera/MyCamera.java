package com.example.v_shevchyk.mycamera.Camera;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.Display;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.ResizeModule;

import java.util.List;

public class MyCamera implements CameraContract.ICameraListener {
    private Camera mCamera;
    private Context mContext;
    private Camera.Parameters parameters;
    private CameraPreview mPreview;
    private ResizeModule resizeModule;

    public MyCamera(Context context) {
        this.mContext = context;
        mCamera = getCameraInstance();
    }


    @Override
    public void releaseCamera() {
        if (mCamera != null) {
            //mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public CameraPreview cameraStartPreview() {
        mPreview = new CameraPreview(mContext, mCamera);
        return mPreview;

    }

    @Override
    public void cameraSetOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            parameters.set("orientation", "landscape");
            parameters.set("rotation", 90);
            mCamera.setParameters(parameters);
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            parameters.set("orientation", "portrait");
            parameters.set("rotation", 90);
            mCamera.setParameters(parameters);
        }
    }

    @Override
    public ResizeModule cameraFitPreviewSize(Display display) {
        resizeModule = new ResizeModule(display, mCamera);
        return resizeModule;
    }

    @Override
    public void cameraGetTimerParams() {

    }

    @Override
    public List<String> cameraGetSceneParams() {
        return parameters.getSupportedSceneModes();
    }

    @Override
    public List<String> cameraGetWhiteParams() {
        return parameters.getSupportedWhiteBalance();
    }

    @Override
    public List<String> cameraGetColorParams() {
        return parameters.getSupportedColorEffects();
    }

    @Override
    public List<String> cameraGetFlashLightParams() {
        return parameters.getSupportedFlashModes();
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }
}
