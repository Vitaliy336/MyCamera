package com.example.v_shevchyk.mycamera;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.view.Display;


public class ResizeModule {
    private Display displaySize;
    private Camera camera;

    public ResizeModule(Display displaySize, Camera camera) {
        this.displaySize = displaySize;
        this.camera = camera;
    }

    public RectF calculate(boolean full) {
        boolean widthIsMax = displaySize.getWidth() > displaySize.getHeight();
        Camera.Size size = camera.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        rectDisplay.set(0, 0, displaySize.getWidth(), displaySize.getHeight());

        if (widthIsMax) {
            rectPreview.set(0, 0, size.width, size.height);
        } else {
            rectPreview.set(0, 0, size.height, size.width);
        }

        Matrix matrix = new Matrix();
        if (!full) {
            matrix.setRectToRect(rectPreview, rectDisplay, Matrix.ScaleToFit.START);
        } else {
            matrix.setRectToRect(rectDisplay, rectPreview, Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }

        matrix.mapRect(rectPreview);

        return rectPreview;
    }

}