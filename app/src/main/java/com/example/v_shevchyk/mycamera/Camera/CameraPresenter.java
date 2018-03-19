package com.example.v_shevchyk.mycamera.Camera;

import android.hardware.Camera;

import com.example.v_shevchyk.mycamera.ResizeModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraPresenter implements CameraContract.ICameraPresenter {
    private CameraContract.ICameraView view;
    private final int maxZomm;
    private MyCamera camera;
    private int zoom;


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
        view.openGalery();
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
                File saveDir = new File("/sdcard/MyFolder/");
                if (!saveDir.exists()) {
                    saveDir.mkdir();
                }

                try {
                    FileOutputStream os = new FileOutputStream(String.format("/sdcard/MyFolder/%d.jpg",
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
    public void uplyZoom() {
        camera.applyParameters(zoom);
    }

    @Override
    public void resizePreview() {
        view.fitPreview(camera.cameraFitPreviewSize());
    }


    private String[] convert(List<String> list){
        final String [] array = list.toArray(new String[list.size()]);
        return array;
    }

    public int getMaxZomm() {
        return maxZomm;
    }

    public void setZoomLvl(int zoomLvl){
        this.zoom = zoomLvl;
    }
}
