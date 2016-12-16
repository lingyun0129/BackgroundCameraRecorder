package com.sth.camerabackgroundrecorder.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by user on 2016/7/5.
 * Power by cly
 */
public class MySharedPreference {
    private static final String PREFERENCE_NAME = "preference.db";
    public static final String  KEY_REGISTERED="registered";
    private SharedPreferences mPreference;
    public MySharedPreference(Context context) {
        mPreference=context.getSharedPreferences(PREFERENCE_NAME,context.MODE_PRIVATE);
    }
    public boolean putStringAndCommit(String key, String value) {
        return mPreference.edit().putString(key, value).commit();
    }

    public boolean putIntAndCommit(String key, int value) {
        return mPreference.edit().putInt(key, value).commit();
    }

    public boolean putBooleanAndCommit(String key, boolean value) {
        return mPreference.edit().putBoolean(key, value).commit();
    }

    public boolean putIntAndCommit(ContentValues values) {
        SharedPreferences.Editor editor = mPreference.edit();
        for (Map.Entry<String, Object> value : values.valueSet()) {
            editor.putString(value.getKey(), value.getValue().toString());
        }
        return editor.commit();
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defValue) {
        return mPreference.getString(key, defValue);
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defValue) {
        return mPreference.getInt(key, defValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPreference.getBoolean(key, defValue);
    }
}
