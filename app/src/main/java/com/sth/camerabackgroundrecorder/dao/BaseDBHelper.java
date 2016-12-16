package com.sth.camerabackgroundrecorder.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sth.camerabackgroundrecorder.util.Assist;

/**
 * Created by user on 2016/7/7.
 * Power by cly
 */
public class BaseDBHelper extends SQLiteOpenHelper{
    private static final SQLiteDatabase.CursorFactory factory = null;

    private static final int version = 1;

    public BaseDBHelper(Context context) {
        super(context, "sth_background_record.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Assist.TABLENAME_VIDEO + " (_id integer primary key ,starttime,endtime,path,name,thumbpath,size,showName,resolution_ratio,onuploadsuccess,oncrash,foreversave)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void dellAll() {
        this.getWritableDatabase().close();
    }
}
