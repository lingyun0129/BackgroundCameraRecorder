package com.sth.camerabackgroundrecorder.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sth.camerabackgroundrecorder.myinterface.VideoDAOInterface;
import com.sth.camerabackgroundrecorder.util.Assist;
import com.sth.camerabackgroundrecorder.util.VideoFileBean;


public class VideoDBHelper implements VideoDAOInterface {
	private BaseDBHelper databaseHelper;

	public VideoDBHelper(Context context) {

		databaseHelper = new BaseDBHelper(context);
	}

	/**
	 * @param videoFileBean
	 * @return 1表示成功 0 表示失败
	 */
	@Override
	public long save(VideoFileBean videoFileBean) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		try {
			db.execSQL("insert into " + Assist.TABLENAME_VIDEO + " (" + //
					"starttime, " + // 开始时间
					"endtime," + // 接受时间
					"path ," + // 路径
					"name," + // 名称
					"thumbpath," + // 缓存缩略图图片
					"size," + // 缓存缩略图图片
					"showName," + // 缓存缩略图图片
					"resolution_ratio," + // 分辨率
					"onuploadsuccess," + // 是否上传
					"oncrash," + // 发生碰撞
					"foreversave" + // 永久保存foreversave
					") " + " values(?,?,?,?,?,?,?,?,?,?,?)", //
					new Object[] { videoFileBean.getStarttime(),//
							videoFileBean.getEndtime(),//
							videoFileBean.getPath(),//
							videoFileBean.getName(),//
							videoFileBean.getThumbpath(), //
							videoFileBean.getSize(), //
							videoFileBean.getShowName(), //
							videoFileBean.getResolution_ratio(), //
							videoFileBean.isOnUploadSuccess(), //
							videoFileBean.isOnCrash(), //
							videoFileBean.isForeverSave() //
					});
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<VideoFileBean> getAllVideoFileBean() {
		List<VideoFileBean> lists = new ArrayList<VideoFileBean>();
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		long starttime;
		long endtime;
		String path;
		String name;
		String thumbpath;
		String size;
		String showName;
		String resolution_ratio;
		boolean onuploadsuccess;
		boolean oncrash;
		boolean foreverSave;

		Cursor cursor = db.rawQuery("select * from " + Assist.TABLENAME_VIDEO + " order by _id desc ", null);
		while (cursor.moveToNext()) {
			starttime = cursor.getLong(cursor.getColumnIndex("starttime"));
			endtime = cursor.getLong(cursor.getColumnIndex("endtime"));
			path = cursor.getString(cursor.getColumnIndex("path"));
			name = cursor.getString(cursor.getColumnIndex("name"));
			thumbpath = cursor.getString(cursor.getColumnIndex("thumbpath"));
			size = cursor.getString(cursor.getColumnIndex("size"));
			showName = cursor.getString(cursor.getColumnIndex("showName"));
			resolution_ratio = cursor.getString(cursor.getColumnIndex("resolution_ratio"));
			onuploadsuccess = cursor.getInt(cursor.getColumnIndex("onuploadsuccess")) > 0;
			oncrash = cursor.getInt(cursor.getColumnIndex("oncrash")) > 0;
			foreverSave = cursor.getInt(cursor.getColumnIndex("foreversave")) > 0;

			VideoFileBean videoFileBean = new VideoFileBean(starttime, endtime, path, name, thumbpath, size, resolution_ratio, showName);
			videoFileBean.setOnUploadSuccess(onuploadsuccess);
			videoFileBean.setOnCrash(oncrash);
			videoFileBean.setForeverSave(foreverSave);
			lists.add(videoFileBean);
		}
		db.close();
		return lists;
	}

	@Override
	public VideoFileBean getLatest() {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		long starttime;
		long endtime;
		String path;
		String name;
		String thumbpath;
		String size;
		String showName;
		String resolution_ratio;

		Cursor cursor = db.rawQuery("select * from " + Assist.TABLENAME_VIDEO + " order by _id desc ", null);
		if (cursor.moveToNext()) {
			starttime = cursor.getLong(cursor.getColumnIndex("starttime"));
			endtime = cursor.getLong(cursor.getColumnIndex("endtime"));
			path = cursor.getString(cursor.getColumnIndex("path"));
			name = cursor.getString(cursor.getColumnIndex("name"));
			thumbpath = cursor.getString(cursor.getColumnIndex("thumbpath"));
			size = cursor.getString(cursor.getColumnIndex("size"));
			showName = cursor.getString(cursor.getColumnIndex("showName"));
			resolution_ratio = cursor.getString(cursor.getColumnIndex("resolution_ratio"));

			cursor.close();
			db.close();

			VideoFileBean videoFileBean = new VideoFileBean(starttime, endtime, path, name, thumbpath, size, resolution_ratio, showName);

			return videoFileBean;
		}
		return null;
	}

	/**
	 * 删除一个VideoFileBean
	 */
	@Override
	public boolean del(VideoFileBean videoFileBean) {
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		int i = db.delete(Assist.TABLENAME_VIDEO, "path= '" + videoFileBean.getPath() + "'", null);
		db.close();
		return i > 0;
	}

	/**
	 * 上传成功后修改数据库
	 * 
	 * @param
	 * @param
	 */
	@Override
	public void setUploadType(VideoFileBean videoFileBean, Boolean onUploadSuccess) {
		try {
			SQLiteDatabase db = databaseHelper.getReadableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("onuploadsuccess", onUploadSuccess);
			db.update(Assist.TABLENAME_VIDEO, contentValues, "path= '" + videoFileBean.getPath() + "'", null);// 特别注意
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发生碰撞后
	 * 
	 * @param
	 * @param
	 */
	@Override
	public void setOnCrashType(VideoFileBean videoFileBean, Boolean oncrash) {
		try {
			SQLiteDatabase db = databaseHelper.getReadableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("oncrash", oncrash);
			db.update(Assist.TABLENAME_VIDEO, contentValues, "path= '" + videoFileBean.getPath() + "'", null);// 特别注意
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setForeverSave(VideoFileBean videoFileBean, Boolean foreverSave) {
		try {
			SQLiteDatabase db = databaseHelper.getReadableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put("foreverSave", foreverSave);
			db.update(Assist.TABLENAME_VIDEO, contentValues, "path= '" + videoFileBean.getPath() + "'", null);// 特别注意
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getTempVideoSize() {
		double allTempSize = 0;
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select size from " + Assist.TABLENAME_VIDEO + " where foreversave <= 0  order by _id desc ", null);

		while (cursor.moveToNext()) {
			String size = cursor.getString(cursor.getColumnIndex("size"));
			try {
				allTempSize += Double.parseDouble(size.replace("M", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return allTempSize;
	}
}
