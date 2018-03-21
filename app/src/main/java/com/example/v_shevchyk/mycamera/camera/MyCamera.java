package com.example.v_shevchyk.mycamera.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Display;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.ResizeModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyCamera implements CameraContract.ICameraListener {
    private Camera mCamera;
    private Context mContext;
    private Camera.Parameters parameters;
    private CameraPreview mPreview;
    private ResizeModule resizeModule;
    Display display;
    private MediaRecorder mMediaRecorder;

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

    @Override
    public boolean prepareVideoRecorder() {
        String os = (String.format("/sdcard/MyFolder/%d.3gp",
                System.currentTimeMillis()));

        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mMediaRecorder.setOutputFile(os);

        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        mMediaRecorder.setOrientationHint(90);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    @Override
    public void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    @Override
    public void startRecord() {
        if(prepareVideoRecorder()){
            mMediaRecorder.start();
        } else {
            releaseMediaRecorder();
        }
    }

    @Override
    public void stopRecord() {
        if(mMediaRecorder != null){
            mMediaRecorder.stop();
            releaseMediaRecorder();
        }
    }

    @Override
    public void touchTofocus(Rect rect) {
        if(parameters.getMaxNumMeteringAreas() > 0){
            List<Camera.Area>focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(rect, 1000));
            parameters.setFocusAreas(focusAreas);
            mCamera.cancelAutoFocus();
            applyParameters(parameters);
        }
        else {

        }
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