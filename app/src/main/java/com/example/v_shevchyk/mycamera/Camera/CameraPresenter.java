package com.example.v_shevchyk.mycamera.Camera;

/**
 * Created by v_shevchyk on 09.03.18.
 */

public class CameraPresenter implements CameraContract.ICameraPresenter {
    private CameraContract.ICameraView view;
    @Override
    public void attachView(CameraContract.ICameraView view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void takePictureClick() {
        view.takePicture();
    }
}
