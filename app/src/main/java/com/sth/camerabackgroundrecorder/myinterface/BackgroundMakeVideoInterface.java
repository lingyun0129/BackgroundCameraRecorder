package com.sth.camerabackgroundrecorder.myinterface;

public interface BackgroundMakeVideoInterface {
	/**
	 * 开始录像
	 * 
	 * @throws
	 */
	void makeVideo();

	/**
	 * 停止录像
	 */
	void stopVideo();

	/**
	 * 退出
	 */
	void exit();

	/**
	 * 前台切换到后台
	 */
	void foregroundToBackground(Boolean continue_Record);

	/**
	 * 后台切换到前台
	 */
	void backgroundToForeground();
}
