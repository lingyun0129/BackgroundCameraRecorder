package com.sth.camerabackgroundrecorder.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sth.camerabackgroundrecorder.myinterface.BackgroundMakeVideoInterface;
import com.sth.camerabackgroundrecorder.util.Assist;
import com.sth.camerabackgroundrecorder.util.CameraHelper;
import com.sth.camerabackgroundrecorder.util.MyNotification;
import com.sth.camerabackgroundrecorder.util.Tools;

import java.io.IOException;

/**
 * 后台录像服务
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class BackgroundWorkService extends Service implements BackgroundMakeVideoInterface,TextureView.SurfaceTextureListener{
    private CameraHelper myCamera=null;
    private Context mContext=this;
    private MyNotification myNotification;
    //Texture View
    private TextureView mTextureView;
    //Windows manager dynamic
    private WindowManager mWindowManager;
    //Inflater
    public LayoutInflater minflater;

    public BackgroundWorkService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("cai", "BackGroundWorkService ---->onCreate()");
        //createSurfaceTexture();
        myCamera=new CameraHelper(mContext);
        IntentFilter filter=new IntentFilter();
        filter.addAction(Assist.MAKE_VIDEO);
        filter.addAction(Assist.STOP_VIDEO);
        filter.addAction(Assist.FOREGROUND_TO_BACKGROUND);
        filter.addAction(Assist.BACKGROUND_TO_FOREGROUND);
        filter.addAction(Assist.TOAST);
        filter.addAction(Assist.CANCEL);
        registerReceiver(mReceiver, filter);
       Tools.sendBroadcast(this, Assist.FOREGROUND_TO_BACKGROUND, true);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("cai","BackGroundService onStartCommand ---->called");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("cai","BackGroundService onDestroy ---->called");
        unregisterReceiver(mReceiver);
        if (myCamera!=null&&myCamera.getmCamera()!=null){
            myCamera.closeCamera();
        }
        if (myNotification != null && myNotification.isShowing()) {
            myNotification.cancelNotification();
        }

    }
    /*
* Create surface and initial variables
* */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void createSurfaceTexture() {

        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        minflater = (LayoutInflater)getSystemService (LAYOUT_INFLATER_SERVICE);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        FrameLayout mParentView = new FrameLayout(getApplicationContext());
        final WindowManager.LayoutParams param = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                PixelFormat.TRANSLUCENT);
        param.width = 352;
        param.height = 288;
        mWindowManager.addView(mParentView, param);
        mParentView.addView(mTextureView);
    }
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Assist.FOREGROUND_TO_BACKGROUND.equals(action)) {
                Boolean b = intent.getExtras().getBoolean(Assist.CONTINUE_RECORD);
                BackgroundWorkService.this.foregroundToBackground(b);
            } else if (Assist.BACKGROUND_TO_FOREGROUND.equals(action)) {
                BackgroundWorkService.this.backgroundToForeground();
            } else if (Assist.STOP_VIDEO.equals(action)) {
                BackgroundWorkService.this.stopVideo();
            } else if (Assist.MAKE_VIDEO.equals(action)) {
                BackgroundWorkService.this.makeVideo();
            } else if (Assist.TOAST.equals(action)) {
                String text = intent.getExtras().getString(Assist.TOAST);
                //MyToast.show(context, text);
                Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
            }else if (Assist.CANCEL.equals(action)){
                BackgroundWorkService.this.stopVideo();
                stopSelf();
            }
        }
    };
    @Override
    public void foregroundToBackground(final Boolean continue_Record) {
        Assist.isBackgroundWork = true;

        //createNetTraffic();
        boolean b=myCamera.openCamera();
        if (b=true&&myCamera.getmCamera()!=null){
            //Setting texture
            try {
                myCamera.getmCamera().setPreviewTexture(new SurfaceTexture(1));
            } catch (IOException e) {
                e.printStackTrace();
            }
            myCamera.getmCamera().startPreview();
        }else{
            Toast.makeText(mContext,"相机打开失败!",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("cai", "background service open camera success " + b);
        // 这里要等相机完全打开后才能开始录像，不然camer是null
        new Thread() {
            @Override
            public void run() {
                try {
                    while (myCamera.getmCamera() == null) {
                        Thread.sleep(1000);
                        System.gc();
                    }
                    if (continue_Record)
                        makeVideo();
                    myNotification = new MyNotification(mContext);
                    myNotification.showNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void backgroundToForeground() {

    }

    @Override
    public void makeVideo() {

        if (myCamera.getmCamera() != null) {
            try {
                Log.i("cai","makeVideo---->开始录像");
                myCamera.startRecording();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopVideo() {
        if (Assist.isBackgroundWork && myCamera != null) {
            Log.i("cai","backgroundWorkService--->停止录像");
            myCamera.stopRecording();
            //myCamera.releaseCamera();
        }
    }

    @Override
    public void exit() {
        if (Assist.isBackgroundWork && myCamera != null) {
            Log.i("cai","backgroundWorkService--->退出");
            myCamera.stopRecording();
            //myCamera.releaseCamera();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if(myCamera!=null){
            myCamera.closeCamera();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
