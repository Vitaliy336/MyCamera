package com.example.v_shevchyk.mycamera.Camera;

import android.hardware.Camera;
import android.view.Display;
import android.view.View;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.ResizeModule;
import com.example.v_shevchyk.mycamera.base.BasePresenter;
import com.example.v_shevchyk.mycamera.base.BaseView;

import java.util.List;


public class CameraContract {
    public interface ICameraView extends BaseView{
        void takePicture(Camera.PictureCallback callback);

        void startPreview();

        void fitPreview();

        void showGalery();

        void updateGaleryBroadcast();

        void showSettings();

        void hideSettings();

        void aplyColor();

        void aplyFlashLight();

        void applyWhiteLvl();

        void applySceneMode();

        void applyTimer();
    }

    public interface ICameraPresenter extends BasePresenter<ICameraView>{
        void startCamera();

        void savePicture();

        void updateGalery();

        void galeryClick();

        void settingsClick(int visibility);

        void clickColor();

        void clickFlashLight();

        void clickWhiteLvl();

        void clickSceneMode();

        void timerClick();
    }

    public interface ICameraListener {
        void initCamera();

        void releaseCamera();

        CameraPreview cameraStartPreview();

        void cameraSetOrientation(int orientation);

        ResizeModule cameraFitPreviewSize(Display display);

        void cameraGetTimerParams();

        List<String> cameraGetSceneParams();

        List<String> cameraGetWhiteParams();

        List<String> cameraGetColorParams();

        List<String> cameraGetFlashLightParams();

    }
}
