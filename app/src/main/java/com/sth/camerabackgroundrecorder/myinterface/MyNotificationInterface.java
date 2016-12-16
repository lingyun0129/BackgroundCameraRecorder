package com.sth.camerabackgroundrecorder.myinterface;

public interface MyNotificationInterface {
	/**
	 * 发生通知
	 */
	void showNotification();

	/**
	 * 取消通知
	 */
	void cancelNotification();

	/**
	 * 通知是否显示
	 * 
	 * @return
	 */
	boolean isShowing();
}
