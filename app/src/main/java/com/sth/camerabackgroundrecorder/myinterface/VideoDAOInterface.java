package com.sth.camerabackgroundrecorder.myinterface;


import com.sth.camerabackgroundrecorder.util.VideoFileBean;

import java.util.List;

public interface VideoDAOInterface {
	/**
	 * 
	 * @param videoFileBean
	 * @return 1表示成功 0 表示失败
	 */
	long save(VideoFileBean videoFileBean);

	List<VideoFileBean> getAllVideoFileBean();

	/**
	 * 获取最新的文件
	 * 
	 * @return
	 */
	VideoFileBean getLatest();
	boolean del(VideoFileBean videoFileBean);

	void setUploadType(VideoFileBean videoFileBean, Boolean uploadSuccess);
	void setOnCrashType(VideoFileBean videoFileBean, Boolean onCrashType);
	void setForeverSave(VideoFileBean videoFileBean, Boolean foreverSave);
	
	double getTempVideoSize();
}