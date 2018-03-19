package com.example.v_shevchyk.mycamera.Camera;

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
    private int zoom;


    public CameraPresenter(MyCamera myCamera) {
        this.camera = myCamera;
        maxZomm = camera.getMaxZoomSupported();
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

    }

    @Override
    public void uplyZoom() {
        camera.applyParameters(zoom);
    }


//    public void savePicture() {
//        Camera.PictureCallback callback = new Camera.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] bytes, Camera camera) {
//                File saveDir = new File("/sdcard/MyFolder/");
//                if (!saveDir.exists()) {
//                    saveDir.mkdir();
//                }
//
//                try {
//                    FileOutputStream os = new FileOutputStream(String.format("/sdcard/MyFolder/%d.jpg",
//                            System.currentTimeMillis()));
//                    os.write(bytes);
//                    os.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                camera.startPreview();
//            }
//        };
//        view.takePicture(callback);

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
