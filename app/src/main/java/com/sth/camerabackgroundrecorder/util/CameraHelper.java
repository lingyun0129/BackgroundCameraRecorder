package com.sth.camerabackgroundrecorder.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.sth.camerabackgroundrecorder.dao.VideoDBHelper;
import com.sth.camerabackgroundrecorder.myinterface.MakeVideoInterface;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2016/7/7.
 * Power by cly
 */
public class CameraHelper {
    private Camera mCamera = null;
    private  Context mContext;
    private Timer timer = new Timer();
    private MovieRecorder movieRecorder = new MovieRecorder();

    //单例模式
/*    private static volatile CameraHelper instance = null;
    private CameraHelper(){}
    public static CameraHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (CameraHelper.class) {
                if (instance == null) {
                    instance = new CameraHelper(context);
                    //mContext=context;

                }
            }
        }
        return instance;
    }*/
    /**
     * @param context
     */
    public CameraHelper(Context context) {
        mContext = context;
        if (Assist.videoDBHelper == null) {
            Assist.videoDBHelper = new VideoDBHelper(context);
        }

        init(context);
    }

    private void init(final Context context) {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (movieRecorder.videoFileBean != null) {
                    if (movieRecorder.videoFileBean.getStarttime() != 0) {
                        Intent intent = new Intent(Assist.TIMER);
                        long time = System.currentTimeMillis() - movieRecorder.videoFileBean.getStarttime();// 已经录制的时长
                        intent.putExtra(Assist.TIMER, Tools.getTimeString(time));
                        context.sendBroadcast(intent);
                        if (time >= AppPara.getInstance().getLoopDuration()*60*1000) {
                            // 停止录像然后再开启
                            Log.i("cai", "timer is running");
                            takePicture();
                            takePicture();
                        }

                    }
                }
            }
        }, 1000, 1000);
    }

    /**
     * @return
     */
    public Camera getmCamera() {
        return mCamera;
    }

    /**
     * 打开照相机
     *
     * @return 开启相机是否成功
     */
    public boolean openCamera() {
        boolean openCameraSuccess = false;
        try {
            if (mCamera != null) {
                releaseCamera();
            }
            if (mCamera == null) {
                mCamera = Camera.open(AppPara.getInstance().getCurrentCameraId());
                if (CameraSupportedParameters.getInstance() == null) {
                    CameraSupportedParameters.init(mCamera);
                }
                openCameraSuccess = true;
            }
        } catch (RuntimeException e) {
            Log.i("cai","open camera failed:"+e.getMessage());
            mCamera = null;
            openCameraSuccess = false;
            Toast.makeText(mContext, "打开相机失败", Toast.LENGTH_SHORT).show();
        }
        //设置参数
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setZoom(0);

                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF/*AppPara.getInstance().getFlashmode()*/);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {// sdk17以上才有这个功能
                    parameters.setRecordingHint(true);// 4.0这样有助于减少启动采集所需要的时间
                    /**
                     * Android 4.0.3引入可以使图像稳定化
                     */
                    if (parameters.isVideoStabilizationSupported())
                        parameters.setVideoStabilization(true);
                    mCamera.enableShutterSound(false/*AppPara.getInstance().isShutterSound()*/);// 控制声音

                }
                List<String> scene_modes = parameters.getSupportedSceneModes();
                String scene_mode = setupValuesPref(scene_modes, "preference_scene_mode", Camera.Parameters.SCENE_MODE_AUTO);
                if (scene_mode != null) {
                    parameters.setSceneMode(scene_mode);
                }


                //parameters.setPictureFormat(ImageFormat.JPEG);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//
                parameters.setExposureCompensation(AppPara.getInstance().getExposureCompensation());
                // 曝光度
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return openCameraSuccess;
    }

    private String setupValuesPref(List<String> values, String key, String default_value) {
        if (values != null && values.size() > 0) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String value = sharedPreferences.getString(key, default_value);
            if (!values.contains(value)) {
                if (values.contains(default_value))
                    value = default_value;
                else
                    value = values.get(0);
            }

            // now save, so it's available for PreferenceActivity
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();

            return value;
        } else {
            return null;
        }
    }

    /**
     * 关闭相机
     */
    public void closeCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);

            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            System.gc();
        }
    }

    /**
     * 拍照 录像 (前台录像用)
     */

    public void takePicture() {

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        System.out.println("profile.videoFrameRate:" + profile.videoFrameRate);

        System.out.println("高" + Assist.screenHeight);
        System.out.println("kuan" + Assist.screenWidth);

        if (!Assist.isTake_Pictureing) {// 拍照时候先聚焦，聚焦成功拍照，
            // getSupportedPreviewSizes(mCamera.getParameters());
            if (Assist.isTake_Picture && !Assist.isRecording) {
                // 拍照暂时不处理
/*                Assist.isTake_Pictureing = true;
                mCamera.enableShutterSound(AppPara.getInstance().isShutterSound());// 控制声音
                mCamera.autoFocus(myAutoFocusCallback);*/

            } else if (!Assist.isTake_Picture) { // 录像
                if (movieRecorder != null) {
                    if (Assist.isRecording) {
                        //movieRecorder.stopRecording();
                        stopRecording();
                    } else {
                        if (Tools.getFreeMemory() > 100) {
                            //movieRecorder.startRecording();
                            startRecording();
                        } else {
                            Toast.makeText(mContext,"内存容量不足,停止录像",Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                }
            }
        }
    }

    /**
     * 后台录像调用这个
     */
    public void startRecording() {
        if (movieRecorder != null && !Assist.isRecording) {
            setAudioMute();
            movieRecorder.startRecording();
            restoreUnmuteState();
        }
    }

    /**
     * 后台录像调用这个
     */
    public void stopRecording() {
        Log.i("cai","stopRecording----->"+Assist.isRecording);
        if (Assist.isRecording) {
            setAudioMute();
            movieRecorder.stopRecording();
            restoreUnmuteState();
        }
    }

    public void releaseCamera(){
        if (mCamera!=null){
            Log.i("cai","releaseCamera----->");
            mCamera.release();
            mCamera=null;
        }
    }
    private Handler handler = new Handler();

    public void setAudioMute() {
        AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        //mgr.adjustStreamVolume(AudioManager.STREAM_SYSTEM,AudioManager.ADJUST_MUTE,0);
        mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
    }

    public void restoreUnmuteState() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                        //mgr.adjustStreamVolume(AudioManager.STREAM_SYSTEM,AudioManager.ADJUST_UNMUTE,0);
                        mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                    }
                });
            }
        }, 1000);
    }

    /**
     * 录像
     *
     * @author Administrator
     */
    class MovieRecorder implements MakeVideoInterface {
        private MediaRecorder mediarecorder = null;
        // public boolean recording = false;
        private VideoFileBean videoFileBean;

        private boolean onCrash;
        private boolean foreverSave;

        public MovieRecorder() {
            super();
        }

        /**
         * 初始化数据参数
         */
        void init() {
        }

        private void delTempVideo() {
            Log.i("cai","删除视频");
            List<VideoFileBean> videoFileBeans = Assist.videoDBHelper.getAllVideoFileBean();
            for (VideoFileBean videoFileBean : videoFileBeans) {
                if (!videoFileBean.isForeverSave()) {
                    if (FileUtils.deleteQuietly(new File(videoFileBean.getPath()))) {
                        Assist.videoDBHelper.del(videoFileBean);
                    }
                }
            }
        }

        @Override
        public void startRecording() {
            // 检测文件临时文件是否超出，并处理
            if (Assist.videoDBHelper == null)
                Log.i("cailingyun", "videoDBHelper is null");
            if (Tools.getFreeMemory() < 100 /*|| Assist.videoDBHelper.getTempVideoSize() >= AppPara.getInstance().getTempFolderSize()*/) {
/*                delTempVideo();
                if (Tools.getFreeMemory() < 100) {
                    Tools.sendBroadcast(mContext, Assist.TOAST, "内存卡容量不足");
                    return;
                }*/
                Log.i("cai","show low mememory");
                Tools.sendBroadcast(mContext, Assist.TOAST, "内存卡容量不足");
                Tools.sendBroadcast(mContext,Assist.CANCEL);
                return;
            }
            if (mediarecorder == null) {
                mediarecorder = new MediaRecorder();// 创建mediarecorder对象
            }
            videoFileBean = new VideoFileBean();
            // Step 1: Unlock and set camera to MediaRecorder
            if (mCamera != null)
                mCamera.unlock();
            mediarecorder.setCamera(mCamera);

            //if (AppPara.getInstance().isRECsound()) {
            //mediarecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// 声音
            //}

            // Step 2: Set sources
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
            mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            int width = AppPara.getInstance().getVideo_Resolution_Ratio().getWidth();
            int height = AppPara.getInstance().getVideo_Resolution_Ratio().getHeight();
            mediarecorder.setVideoSize(width, height);
            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mediarecorder.setVideoFrameRate(profile.videoFrameRate);
/*            if (AppPara.getInstance().getCurrentCameraId() == 0) {
                mediarecorder.setOrientationHint(90);
            }else{
                mediarecorder.setOrientationHint(270);
            }*/
            mediarecorder.setOrientationHint(AppPara.getInstance().getRotationAngle());
            mediarecorder.setVideoEncodingBitRate(profile.videoBitRate);
            //mediarecorder.setPreviewDisplay(Preview.this.getHolder().getSurface());

            long time = System.currentTimeMillis();
            String filename = Tools.getFileName(time);
            videoFileBean.setName(filename);
            videoFileBean.setResolution_ratio(width + "x" + height);
            videoFileBean.setShowName(Tools.getShowName(time));
            videoFileBean.setStarttime(time);
            videoFileBean.setPath(AppPara.getInstance().getSavePath() + "/" + filename + ".mp4");

            mediarecorder.setOutputFile(videoFileBean.getPath());
            // 准备录制
            try {
                mediarecorder.setOnInfoListener(null);
                mediarecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                    @Override
                    public void onError(MediaRecorder mr, int what, int extra) {
                        Log.e("cai","record video error what="+what+" extra="+extra);
                        //releaseMediaRecorder();
                    }
                });

                mediarecorder.prepare();
                mediarecorder.start();
                Assist.isRecording = true;
                Log.i("cai", "设置 Assist.isRecording=true");
            } catch (IllegalStateException e){
                e.printStackTrace();
                releaseMediaRecorder();
            }
            catch (IOException e) {
                e.printStackTrace();
                releaseMediaRecorder();
            }

        }

        // 正真停止录像，
        @Override
        public void stopRecording() {
            if (mediarecorder != null && Assist.isRecording) {
//                try{

                    mediarecorder.stop();
                    releaseMediaRecorder();
/*                }catch (RuntimeException e){
                    //delete file
                    try {
                        FileUtils.forceDelete(new File(videoFileBean.getPath()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }finally {*/

                    //releaseMediaRecorder();
                    //mediarecorder.release();
                   // mediarecorder = null;// stopRecording
//                }
                videoFileBean.setEndtime(System.currentTimeMillis());

                String thumbpath = Assist.THUMBFILE_STORAGE_DIRECTORY + MD5.getMD5String(videoFileBean.getName());
                MyFileUtils.createVideoThumbFile(thumbpath, videoFileBean.getPath());

                videoFileBean.setThumbpath(thumbpath);
                videoFileBean.setSize(Tools.getFileSizesTostring(videoFileBean.getPath()));
                onCrash = Assist.crash;
                Assist.crash = false;
                foreverSave = Assist.foreverSave;
                Assist.foreverSave = false;
                // videoFileBean.setOnCrash(true);
                videoFileBean.setOnCrash(onCrash);
                videoFileBean.setForeverSave(foreverSave);
                long i = Assist.videoDBHelper.save(videoFileBean);
                System.out.println(i == 1L ? "存入数据库成功" : "存入数据库失败");
                videoFileBean = null;
                Assist.isRecording = false;
                System.gc();
            }
        }

        // 停止录像，可能还需要继续录像
        @Override
        public void release() {
            Tools.sendBroadcast(mContext, Assist.STOP_RECORD);
            if (mediarecorder != null) {
                mediarecorder.stop();
                mediarecorder.release();
                videoFileBean.setEndtime(System.currentTimeMillis());
                videoFileBean = null;
                System.gc();
            }
        }

        private void releaseMediaRecorder() {
            if (mediarecorder != null) {
                Log.i("cai","releaseMediaRecorder----->");
                mediarecorder.reset();   // clear recorder configuration
                mediarecorder.release(); // release the recorder object
                mediarecorder = null;
                if (mCamera!=null)
                mCamera.lock();// lock camera for later use
            }
        }
    }
}
