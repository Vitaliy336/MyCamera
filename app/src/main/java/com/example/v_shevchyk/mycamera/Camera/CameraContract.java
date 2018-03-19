package com.example.v_shevchyk.mycamera.Camera;

import android.hardware.Camera;
import android.view.Display;
import android.view.View;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.ResizeModule;
import com.example.v_shevchyk.mycamera.base.BasePresenter;
import com.example.v_shevchyk.mycamera.base.BaseView;

import java.security.Policy;
import java.util.List;


public class CameraContract {

    public interface ICameraView extends BaseView {
        void startPreview(CameraPreview cp);

        void openGalery();

        void showSettings();

        void hideSettings();

        void takePicture();

        void flasLight(String[] modes);

    }

    public interface ICameraPresenter extends BasePresenter<ICameraView>{
        void createPreview();

        void clickFlashlight();

        void galeryClick();

        void settingsClick(int visibility);

        void savePicture();

        void uplyZoom();
    }

    public interface ICameraListener {

        void releaseCamera();

        CameraPreview cameraStartPreview();

        void cameraSetOrientation(int orientation);

        ResizeModule cameraFitPreviewSize(Display display);

        void cameraGetTimerParams();

        List<String> cameraGetSceneParams();

        List<String> cameraGetWhiteParams();

        List<String> cameraGetColorParams();

        List<String> cameraGetFlashLightParams();

        int getMaxZoomSupported();

        void applyParameters(int zoom);
    }
}
