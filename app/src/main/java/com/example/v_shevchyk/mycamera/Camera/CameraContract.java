package com.example.v_shevchyk.mycamera.Camera;

import android.hardware.Camera;

import com.example.v_shevchyk.mycamera.base.BasePresenter;
import com.example.v_shevchyk.mycamera.base.BaseView;


public class CameraContract {
    public interface ICameraView extends BaseView{
        void takePicture(Camera.PictureCallback callback);

        void startPreview();

        void fitPreview();
    }

    public interface ICameraPresenter extends BasePresenter<ICameraView>{

        void startCamera();

        void savePicture();

        void setDisplayOrientation(int displayOrientation);
    }
}
