package com.sth.camerabackgroundrecorder.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images.Thumbnails;

public class MyFileUtils {
	private static final double KB = 1024.0;
	private static final double MB = KB * KB;
	private static final double GB = KB * KB * KB;

	/** 如果不存在就创建 */
	public static boolean createIfNoExists(String path) {
		File file = new File(path);
		boolean mk = false;
		if (!file.exists()) {
			mk = file.mkdirs();
		}
		return mk;
	}

	/** 显示SD卡剩余空间 */
	public static String showFileAvailable() {
		String result = "";
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long blockSize = sf.getBlockSize();
			long blockCount = sf.getBlockCount();
			long availCount = sf.getAvailableBlocks();
			return showFileSize(availCount * blockSize) + " / " + showFileSize(blockSize * blockCount);
		}
		return result;
	}

	public static String showFileSize(long size) {
		String fileSize;
		if (size < KB)
			fileSize = size + "B";
		else if (size < MB)
			fileSize = String.format("%.1f", size / KB) + "KB";
		else if (size < GB)
			fileSize = String.format("%.1f", size / MB) + "MB";
		else
			fileSize = String.format("%.1f", size / GB) + "GB";

		return fileSize;
	}

	/**
	 * 创建视频文件缩略图
	 * 
	 * @param thumbpath
	 *            缩略图路径
	 * @param srcpath
	 *            视频路径
	 * @return
	 */
	public static boolean createVideoThumbFile(String thumbpath, String srcpath) {
		Bitmap bitmap = Tools.getVideoThumbnail(srcpath, 200, 140, Thumbnails.MINI_KIND);

		FileOutputStream out;
		try {
			out = new FileOutputStream(thumbpath);
			if (bitmap != null) {
				return bitmap.compress(CompressFormat.JPEG, 100, out);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 创建图片文件缩略图
	 * 
	 * @param thumbpath
	 *            缩略图路径
	 * @param path
	 *            原始图片路径
	 * @return 是否创建成功
	 */
	public static boolean createImageThumbFile(String thumbpath, String path) {
		Bitmap bitmap = Tools.getImageThumbnail(path, 200, 140);

		FileOutputStream out;
		try {
			out = new FileOutputStream(thumbpath);
			if (bitmap != null) {
				return bitmap.compress(CompressFormat.JPEG, 100, out);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 检测缩略图是否存在
	 * 
	 * @param thumbpath
	 * @param srcpath
	 */
	public static void checkVideoThumb(String thumbpath, String srcpath) {
		File thumbfile = new File(thumbpath);
		if (!thumbfile.exists()) {
			createVideoThumbFile(thumbpath, srcpath);
		}
	}

	/**
	 * 检测缩略图是否存在
	 * 
	 * @param thumbpath
	 * @param srcpath
	 */
	public static void checkImageThumb(String thumbpath, String srcpath) {
		File thumbfile = new File(thumbpath);
		if (!thumbfile.exists()) {
			createImageThumbFile(thumbpath, srcpath);
		}
	}

	public static boolean checkFileExists(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}
}
