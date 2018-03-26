package com.example.v_shevchyk.mycamera.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.v_shevchyk.mycamera.R;
import com.example.v_shevchyk.mycamera.camera.preview.CameraPreview;
import com.example.v_shevchyk.mycamera.modules.DrawModule;
import com.example.v_shevchyk.mycamera.modules.ResizeModule;

import java.util.Timer;
import java.util.TimerTask;

public class CameraViews implements View.OnClickListener, CameraContract.ICameraView, SeekBar.OnSeekBarChangeListener {

    private Activity activity;
    private FrameLayout preview;
    private SeekBar zoom;
    private LinearLayout settingsLayout;
    private FloatingActionButton pictureBtn, galery, settings, stopBtn;
    private ImageButton flashLight, timer, colorEfects, whitelvl, sceneMode;
    private CameraPresenter presenter;
    private SwitchCompat changeMode;
    private Chronometer counter;


    public CameraViews(Activity activity, CameraPresenter presenter) {
        this.activity = activity;
        this.presenter = presenter;
        initViews();
        initListeners();
        initPresenter();
    }

    private void initPresenter() {
        presenter.attachView(this);
        presenter.createPreview();
        presenter.resizePreview();
    }

    private void initViews() {
        zoom = activity.findViewById(R.id.zoom);
        settingsLayout = activity.findViewById(R.id.settingslayout);
        pictureBtn = activity.findViewById(R.id.picture_btn);
        galery = activity.findViewById(R.id.gal);
        settings = activity.findViewById(R.id.settings);
        flashLight = activity.findViewById(R.id.flash_light);
        timer = activity.findViewById(R.id.timer);
        whitelvl = activity.findViewById(R.id.white_level);
        sceneMode = activity.findViewById(R.id.scene);
        colorEfects = activity.findViewById(R.id.color_efects);
        preview = activity.findViewById(R.id.preview);
        zoom.setMax(presenter.getMaxZomm());
        changeMode = activity.findViewById(R.id.switch_mode);
        stopBtn = activity.findViewById(R.id.stop_r);
        counter = activity.findViewById(R.id.counter);


    }

    private void initListeners() {
        pictureBtn.setOnClickListener(this);
        galery.setOnClickListener(this);
        flashLight.setOnClickListener(this);
        timer.setOnClickListener(this);
        whitelvl.setOnClickListener(this);
        sceneMode.setOnClickListener(this);
        colorEfects.setOnClickListener(this);
        settings.setOnClickListener(this);
        zoom.setOnSeekBarChangeListener(this);
        stopBtn.setOnClickListener(this);

        changeMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (changeMode.isChecked()) {
                    presenter.videoMode();
                    pictureBtn.setImageResource(R.drawable.ic_videocam_black_24dp);
                } else {
                    presenter.pictureMode();
                    pictureBtn.setImageResource(R.drawable.ic_camera_black_24dp);
                }
            }
        });

//        preview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//
//                createRect((int)motionEvent.getX(), (int)motionEvent.getY());
//                //tHolder.addCallback(callback);
//                int l = (int)motionEvent.getX() - 10;
//                int r = (int)motionEvent.getX() + 10;
//                int u = (int)motionEvent.getY() - 10;
//                int d = (int)motionEvent.getY() + 10;
//
//
//                //drawFocusRect(new Rect(l,u,d,r), Color.BLUE);
//                return false;
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flash_light:
                presenter.clickFlashlight();
                break;
            case R.id.timer:
                presenter.timerClick();
                break;
            case R.id.white_level:
                presenter.clikWhitelvl();
                break;
            case R.id.scene:
                presenter.clickScene();
                break;
            case R.id.settings:
                presenter.settingsClick(settingsLayout.getVisibility());
                break;
            case R.id.picture_btn:
                if(changeMode.isChecked()) {
                    presenter.videoMode();
                    counter.setBase(SystemClock.elapsedRealtime());
                    counter.start();
                    presenter.startVideoClick();
                } else {
                    presenter.savePicture();
                }
                break;
            case R.id.color_efects:
                presenter.clickColor();
                break;
            case R.id.gal:
                presenter.galeryClick();
                break;
            case R.id.stop_r:
                counter.stop();
                presenter.stopVideoClick();
                break;
        }
    }

    @Override
    public void startPreview(CameraPreview cp) {
        preview.addView(cp);
    }

    @Override
    public void showSettings() {
        settingsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSettings() {
        settingsLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void flasLight(String[] modes) {
        buildDialog(modes, flashLight.getId());
    }

    @Override
    public void fitPreview(ResizeModule resizeModule) {
        preview.getLayoutParams().height = (int) (resizeModule.calculate(true).bottom);
        preview.getLayoutParams().width = (int) (resizeModule.calculate(true).right);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        presenter.uplyZoom(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void buildDialog(final String[] options, final int id) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle(R.string.option);
        mBuilder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (id) {
                    case R.id.flash_light:
                        presenter.flash(options[i]);
                        break;
                    case R.id.timer:
                        final Handler handler = new Handler();
                        Timer timer = new Timer(false);
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        presenter.savePicture();
                                    }
                                });
                            }
                        };
                        timer.schedule(timerTask, Integer.parseInt(options[i]) * 1000);
                        break;
                    case R.id.white_level:
                        presenter.whiteLvl(options[i]);
                        break;
                    case R.id.scene:
                        presenter.sceneMode(options[i]);
                        break;
                    case R.id.color_efects:
                        presenter.colorsOptions(options[i]);
                        break;
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void createRect(int x, int y){
        Rect rect = new Rect(x - 100, y - 100, x + 100, y + 100);
        int left = rect.left * 2000 / preview.getWidth() - 1000;
        int right = rect.right * 2000 / preview.getWidth() - 1000;
        int top = rect.top * 2000 / preview.getHeight() - 1000;
        int bottom = rect.bottom * 2000 / preview.getHeight() - 1000;

        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;

        presenter.getRectArea(new Rect(left, top, right, bottom));
    }



    @Override
    public void goToGallery() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void colorEfects(String[] efects) {
        buildDialog(efects, colorEfects.getId());
    }

    @Override
    public void whiteLvl(String[] lvl) {
        buildDialog(lvl, whitelvl.getId());
    }

    @Override
    public void scenes(String[] scenes) {
        buildDialog(scenes, sceneMode.getId());
    }

    @Override
    public void startTimer() {
        buildDialog(activity.getResources().getStringArray(R.array.seconds), timer.getId());
    }

    @Override
    public void showPictureBtn() {
        pictureBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideidePictureBtn() {
        pictureBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showStopBtn() {
        stopBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideStopBtn() {
        stopBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showSettingsBtn() {
        settings.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSettingsBtn() {
        settings.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showGaleryBtn() {
        galery.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGaleryBtn() {
        galery.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showSwitch() {
        changeMode.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSwitch() {
        changeMode.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showTimerBtn() {
        timer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTimerBtn() {
        timer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideCounter() {
        counter.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showCounter() {
        counter.setVisibility(View.VISIBLE);
    }
}