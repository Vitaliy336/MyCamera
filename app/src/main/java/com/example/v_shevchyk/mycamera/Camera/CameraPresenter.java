package com.example.v_shevchyk.mycamera.Camera;

import android.hardware.Camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by v_shevchyk on 09.03.18.
 */

public class CameraPresenter implements CameraContract.ICameraPresenter {
    private CameraContract.ICameraView view;
    private int displayOrientation;

    @Override
    public void attachView(CameraContract.ICameraView view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }


    @Override
    public void startCamera() {
        view.startPreview();
        view.fitPreview();
    }

    @Override
    public void savePicture() {
        Camera.PictureCallback callback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                File saveDir = new File("/sdcard/MyFolder/");
                if(!saveDir.exists()){
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
            }
        };
        view.takePicture(callback);
    }

    @Override
    public void setDisplayOrientation(int displayOrientation) {
        this.displayOrientation = displayOrientation;
    }
}
