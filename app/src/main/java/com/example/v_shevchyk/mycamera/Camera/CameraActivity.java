package com.example.v_shevchyk.mycamera.Camera;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.v_shevchyk.mycamera.R;

import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private MyCamera myCamera;
    private CameraViews views;
    private FrameLayout preivew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        myCamera = new MyCamera(CameraActivity.this);
        views = new CameraViews(CameraActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initListener();
        initPresenter();

    }

    @Override
    protected void onPause() {
        super.onPause();
        myCamera.releaseCamera();
    }

    private void initPresenter() {
    }

    private void initListener() {

    }


//        zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if(mCamera.getParameters().isZoomSupported()){
//                    parameters.setZoom(i);
//                    mCamera.setParameters(parameters);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

    private void initView() {

        myCamera.initCamera();
        preivew = findViewById(R.id.preview);
        views.getPreview().addView(myCamera.cameraStartPreview());
        Display display = getWindowManager().getDefaultDisplay();

        views.getSettings();
    }


//    public void showGalery() {
//        Intent intent = new Intent();
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.setType("image/*");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

//    public void updateGaleryBroadcast() {
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, // update gallery after take a photo
//                Uri.parse("file://"
//                        + Environment.getExternalStorageDirectory())));
//    }



    private void optionsDialog(List<String> posibleOptions, final int id){
        final String [] options = posibleOptions.toArray(new String[posibleOptions.size()]);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CameraActivity.this);
        mBuilder.setTitle(R.string.option);
        mBuilder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
}
