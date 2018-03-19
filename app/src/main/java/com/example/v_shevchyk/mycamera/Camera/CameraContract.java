package com.example.v_shevchyk.mycamera.Camera;

import android.hardware.Camera;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.ResizeModule;
import com.example.v_shevchyk.mycamera.base.BasePresenter;
import com.example.v_shevchyk.mycamera.base.BaseView;

import java.util.List;


public class CameraContract {

    public interface ICameraView extends BaseView {
        void startPreview(CameraPreview cp);

        void showSettings();

        void hideSettings();

        void flasLight(String[] modes);

        void fitPreview(ResizeModule resizeModule);

        void goToGallery();

        void colorEfects(String[] efects);

        void whiteLvl(String[] lvl);

        void scenes(String[] scenes);
    }

    public interface ICameraPresenter extends BasePresenter<ICameraView>{
        void createPreview();

        void clickFlashlight();

        void galeryClick();

        void settingsClick(int visibility);

        void savePicture();

        void uplyZoom(int i);

        void resizePreview();

        void clikWhitelvl();

        void clickScene();

        void clickColor();

        void flash(String s);

        void sceneMode(String mode);

        void whiteLvl(String white);

        void colorsOptions(String color);
    }

    public interface ICameraListener {

        void takePicture(Camera.PictureCallback callback);

        void releaseCamera();

        ResizeModule cameraFitPreviewSize();

        CameraPreview cameraStartPreview();

        void cameraSetOrientation(int orientation);

        void cameraGetTimerParams();

        List<String> cameraGetSceneParams();

        List<String> cameraGetWhiteParams();

        List<String> cameraGetColorParams();

        List<String> cameraGetFlashLightParams();

        int getMaxZoomSupported();

        void applyZoom(int zoom);

        void applyScene(String scene);
        void applyWhite(String white);
        void applyColors(String colors);
        void applyFlashLight(String flasLIght);

    }
}
