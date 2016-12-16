package com.sth.camerabackgroundrecorder.util;

import android.os.Environment;

import com.sth.camerabackgroundrecorder.dao.VideoDBHelper;

public class Assist {
	public static int screenWidth = 0; //
	public static int screenHeight = 0;

	public static final int DIALOGCODE_EXITAPPLIANCE_BACKGROUNDCONTINUE = 40;

	public static final String IMAGE = "image";
	public static final String VIDEO = "video";

	public static final String TIMER = "timer";// 定时器
	public static final String START_RECORD = "start_record";// 录像开始
	public static final String STOP_RECORD = "stop_record"; // 录像停止
	public static final String STOP_TAKEPICTURE = "stop_takepicture"; // 录像拍照
	public static final String CRASH_STOP_RECORD = "crash_stop_record";
	public static final String TOAST = "Toast";
	public static final String FINISH = "finish";
	public static final String MAKE_VIDEO = "makeVideo";
	public static final String STOP_VIDEO = "stopVideo";
	public static final String FOREGROUND_TO_BACKGROUND = "foreground_To_Background";
	public static final String BACKGROUND_TO_FOREGROUND = "background_To_Foreground";
	public static final String CRASH = "crash";
	public static final String KEEP = "keep";
	public static final String SWITCH = "switch";// 开关
	public static final String CANCEL="cancel";//退出后台录像

	public static boolean isBackgroundWork = false;// 前台录像和后台录像的标志
	public static boolean isTake_Picture = false; // 拍照界面

	public static boolean isRecording = false;// 是否正在录像
	public static boolean isTake_Pictureing = false;// 是否正在拍照

	public static final String CONTINUE_RECORD = "continueRecord";

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;


	public static final String TABLENAME_VIDEO = "tablename_video";
	public static final String TABLENAME_IMAGE = "tablename_image";

	public static boolean crash = false;// 默认 碰撞类型
	public static boolean foreverSave = false;// 默认 永久保存
	public static VideoDBHelper videoDBHelper;
	public static final String BASE_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory() + "/sth";// 文件存储目录
	public static final String VIDEOFILE_STORAGE_DIRECTORY = BASE_STORAGE_DIRECTORY + "/video/";// 视频
	public static final String IMAGEFILE_STORAGE_DIRECTORY = BASE_STORAGE_DIRECTORY + "/image/";// 图片
	public static final String THUMBFILE_STORAGE_DIRECTORY = BASE_STORAGE_DIRECTORY + "/thumb/";// 缩略图

}
