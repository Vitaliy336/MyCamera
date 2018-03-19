package com.example.v_shevchyk.mycamera.Camera;

import android.hardware.Camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraPresenter implements CameraContract.ICameraPresenter {
    private CameraContract.ICameraView view;
    MyCamera camera;

    public CameraPresenter(MyCamera myCamera) {
        this.camera = myCamera;
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


//    public void settingsClick(int visibility) { //Visible 0, Invisible 4
//        if (visibility == 4) {
//            view.showSettings();
//        } else {
//            view.hideSettings();
//        }



}
