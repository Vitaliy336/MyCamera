package com.example.v_shevchyk.mycamera.Camera;

import com.example.v_shevchyk.mycamera.base.BasePresenter;
import com.example.v_shevchyk.mycamera.base.BaseView;

/**
 * Created by v_shevchyk on 09.03.18.
 */

public class CameraContract {
    public interface ICameraView extends BaseView{
        void takePicture();
    }

    public interface ICameraPresenter extends BasePresenter<ICameraView>{
        void takePictureClick();
    }
}
