package com.sth.camerabackgroundrecorder.util;

import android.hardware.Camera;

/**
 * Created by user on 2016/7/6.
 * Power by cly
 */
public class AppPara {
    private static AppPara appPara = null;
    // video分辨率
    private Video_Resolution_Ratio video_Resolution_Ratio = new Video_Resolution_Ratio();
    // image分辨率
    private Image_Resolution_Ratio image_Resolution_Ratio = new Image_Resolution_Ratio();
    // 曝光度
    private int exposureCompensation;
    // 临时文件夹大小 单位MB
    private int tempFolderSize;

    // 循环录影时长 分钟
    private int loopDuration;
    // 发生事故紧急联系电话
    private String telephone;
    // 快门声音
    private boolean shutterSound;
    // 是否录制声音
    private boolean RECsound;

    private String flashmode = Camera.Parameters.FLASH_MODE_OFF;// 这个状态不保存到文件中

    //当前摄像头
    private int currentCameraId;

    //文件存储路径
    private String savePath;

    //视频旋转的度数
    private int rotationAngle;

    public int getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(int rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public int getCurrentCameraId() {
        return currentCameraId;
    }

    public void setCurrentCameraId(int currentCameraId) {
        this.currentCameraId = currentCameraId;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    private AppPara() {
        // 私有构造
    }

    public String getFlashmode() {
        return flashmode;
    }

    public void setFlashmode(String flashmode) {
        this.flashmode = flashmode;
    }

    public static AppPara getInstance() {
        if (appPara == null) {
            appPara = new AppPara();
        }
        return appPara;
    }

    public Video_Resolution_Ratio getVideo_Resolution_Ratio() {
        return video_Resolution_Ratio;
    }

    //
    // public void setVideo_Resolution_Ratio(Video_Resolution_Ratio
    // video_Resolution_Ratio) {
    // this.video_Resolution_Ratio = video_Resolution_Ratio;
    // }

    public Image_Resolution_Ratio getImage_Resolution_Ratio() {
        return image_Resolution_Ratio;
    }

    // public void setImage_Resolution_Ratio(Image_Resolution_Ratio
    // image_Resolution_Ratio) {
    // this.image_Resolution_Ratio = image_Resolution_Ratio;
    // }

    public int getExposureCompensation() {
        return exposureCompensation;
    }

    public void setExposureCompensation(int exposureCompensation) {
        this.exposureCompensation = exposureCompensation;
    }

    public int getTempFolderSize() {
        return tempFolderSize;
    }

    public void setTempFolderSize(int tempFolderSize) {
        this.tempFolderSize = tempFolderSize;
    }

    public int getLoopDuration() {
        return loopDuration;
    }

    public void setLoopDuration(int loopDuration) {
        this.loopDuration = loopDuration;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isShutterSound() {
        return shutterSound;
    }

    public void setShutterSound(boolean shutterSound) {
        this.shutterSound = shutterSound;
    }

    public boolean isRECsound() {
        return RECsound;
    }

    public void setRECsound(boolean rECsound) {
        RECsound = rECsound;
    }

    public class Video_Resolution_Ratio {
        private int width;
        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return width + "x" + height;
        }
    }

    public class Image_Resolution_Ratio {
        private int width;
        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return width + "x" + height;
        }
    }
}
