package com.sth.camerabackgroundrecorder.myinterface;

/**
 * Created by user on 2016/7/7.
 * Power by cly
 */
public interface MakeVideoInterface {
    /**
     * 开始录像
     */
    void startRecording();

    /**
     * 停止录像
     */
    void stopRecording();

    /**
     * 释放资源
     */
    void release();
}
