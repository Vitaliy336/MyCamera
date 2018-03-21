package com.example.v_shevchyk.mycamera.camera;

import android.hardware.Camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraPresenter implements CameraContract.ICameraPresenter {
    private CameraContract.ICameraView view;
    private final int maxZomm;
    private MyCamera camera;
    private static final String DIR_PATH = "/sdcard/MyFolder/";
    private static final String IMAGE_PATH = "/sdcard/MyFolder/%d.jpg";

    public CameraPresenter(MyCamera myCamera, int orientation) {
        this.camera = myCamera;
        maxZomm = camera.getMaxZoomSupported();
        camera.cameraSetOrientation(orientation);
    }

    @Override
    public void attachView(CameraContract.ICameraView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void createPreview() {
        view.startPreview(camera.cameraStartPreview());
    }

    @Override
    public void clickFlashlight() {
        view.flasLight(convert(camera.cameraGetFlashLightParams()));
    }

    @Override
    public void galeryClick() {
        view.goToGallery();
    }

    @Override
    public void settingsClick(int visibility) {
        if (visibility == 4) {
            view.showSettings();
        } else {
            view.hideSettings();
        }
    }

    @Override
    public void savePicture() {
        Camera.PictureCallback callback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                File saveDir = new File(DIR_PATH);
                if (!saveDir.exists()) {
                    saveDir.mkdir();
                }

                try {
                    FileOutputStream os = new FileOutputStream(String.format(IMAGE_PATH,
                            System.currentTimeMillis()));
                    os.write(bytes);
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }
        };
        camera.takePicture(callback);
    }

    @Override
    public void uplyZoom(int i) {
        camera.applyZoom(i);
    }

    @Override
    public void resizePreview() {
        view.fitPreview(camera.cameraFitPreviewSize());
    }

    @Override
    public void clikWhitelvl() {
        view.whiteLvl(convert(camera.cameraGetWhiteParams()));
    }

    @Override
    public void clickScene() {
        view.scenes(convert(camera.cameraGetSceneParams()));
    }

    @Override
    public void clickColor() {
        view.colorEfects(convert(camera.cameraGetColorParams()));
    }

    @Override
    public void flash(String s) {
        camera.applyFlashLight(s);
    }

    @Override
    public void sceneMode(String mode) {
        camera.applyScene(mode);
    }

    @Override
    public void whiteLvl(String white) {
        camera.applyWhite(white);
    }

    @Override
    public void colorsOptions(String color) {
        camera.applyColors(color);
    }

    @Override
    public void timerClick() {
        view.startTimer();
    }

    @Override
    public void videoMode() {
        view.hideTimerBtn();
    }

    @Override
    public void pictureMode() {
        view.showPictureBtn();
        view.showTimerBtn();
    }

    @Override
    public void startVideoClick() {
        view.hideidePictureBtn();
        view.showStopBtn();
        view.hideGaleryBtn();
        view.hideSettingsBtn();
        view.hideSwitch();
        camera.startRecord();
    }

    @Override
    public void stopVideoClick() {
        view.hideStopBtn();
        view.showPictureBtn();
        view.showGaleryBtn();
        view.showSettingsBtn();
        view.showSwitch();
        camera.stopRecord();
    }


    private String[] convert(List<String> list){
        final String [] array = list.toArray(new String[list.size()]);
        return array;
    }

    public int getMaxZomm() {
        return maxZomm;
    }
}