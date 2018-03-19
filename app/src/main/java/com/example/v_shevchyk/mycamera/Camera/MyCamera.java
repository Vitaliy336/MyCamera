package com.example.v_shevchyk.mycamera.Camera;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
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
    Display display;

    public MyCamera(Context context, Display defaultDisplay) {
        this.mContext = context;
        mCamera = getCameraInstance();
        parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        applyParameters(parameters);
        this.display = defaultDisplay;
    }

    private void applyParameters(Camera.Parameters parameters) {
        mCamera.setParameters(parameters);
    }


    @Override
    public void takePicture(Camera.PictureCallback callback) {
        mCamera.takePicture(null,null, callback);
        Log.e("TAG", "Picture taken");
    }

    @Override
    public void releaseCamera() {
        if (mCamera != null) {
            mPreview.getHolder().removeCallback(mPreview);
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
            applyParameters(parameters);
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            parameters.set("orientation", "portrait");
            parameters.set("rotation", 90);
            applyParameters(parameters);
        }
    }

    @Override
    public ResizeModule cameraFitPreviewSize() {
        resizeModule = new ResizeModule(display, mCamera);
        return resizeModule;
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

    @Override
    public int getMaxZoomSupported() {
        if (mCamera.getParameters().isZoomSupported()) {
            return parameters.getMaxZoom();
        } else{
            return 0;
        }
    }

    @Override
    public void applyZoom(int zoom) {
        parameters.setZoom(zoom);
        applyParameters(parameters);
    }

    @Override
    public void applyScene(String scene) {
        parameters.setSceneMode(scene);
        applyParameters(parameters);
    }

    @Override
    public void applyWhite(String white) {
        parameters.setWhiteBalance(white);
        applyParameters(parameters);
    }

    @Override
    public void applyColors(String colors) {
        parameters.setColorEffect(colors);
        mCamera.setParameters(parameters);
    }

    @Override
    public void applyFlashLight(String flasLIght) {
        parameters.setFlashMode(flasLIght);
        applyParameters(parameters);
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
