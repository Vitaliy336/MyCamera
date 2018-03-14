package com.example.v_shevchyk.mycamera.Camera;

import android.hardware.Camera;

import com.example.v_shevchyk.mycamera.base.BasePresenter;
import com.example.v_shevchyk.mycamera.base.BaseView;


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
}
