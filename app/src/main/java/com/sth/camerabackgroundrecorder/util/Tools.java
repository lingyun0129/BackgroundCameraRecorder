package com.sth.camerabackgroundrecorder.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.sth.camerabackgroundrecorder.dao.VideoDBHelper;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by user on 2016/7/4.
 * Power by cly
 */
public final class Tools {
    /**
     *
     * @param context
     * @return Imei
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
    /**
     * 网络连接检查
     *
     * @param context
     * @return
     */
    public static boolean detectionNetwork(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            // 当前网络不可用
            Toast.makeText(context.getApplicationContext(), "请先连接网络！", Toast.LENGTH_SHORT).show();
            return false;
        }
        //boolean wifi =con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean wifi=networkinfo.getType()==ConnectivityManager.TYPE_WIFI;
        if (!wifi) { // 提示使用wifi
            Toast.makeText(context.getApplicationContext(), "建议您使用WIFI以减少流量！", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    static MySharedPreference dPreference;
    public static void initAppPara(Context context){
        if (dPreference == null) {
            dPreference = new MySharedPreference(context);
        }
        AppPara appPara = AppPara.getInstance();
        appPara.setLoopDuration(dPreference.getInt("loopDuration", 1));
        appPara.setRECsound(dPreference.getBoolean("RECsound", true));
        appPara.setTempFolderSize(dPreference.getInt("tempFolderSize", 300));
        appPara.setCurrentCameraId(dPreference.getInt("cameraId",0));
        appPara.setSavePath(dPreference.getString("savePath",Assist.VIDEOFILE_STORAGE_DIRECTORY));
        appPara.setRotationAngle(dPreference.getInt("rotationAngle",90));
        appPara.setPassword(dPreference.getString("password",""));
        appPara.getVideo_Resolution_Ratio().setWidth(dPreference.getInt("video_Resolution_Ratio.width", 1280));
        appPara.getVideo_Resolution_Ratio().setHeight(dPreference.getInt("video_Resolution_Ratio.height", 720));
        //back camera ratio
        appPara.getBack_ratio().setWidth(dPreference.getInt("back_ratio.width", 1280));
        appPara.getBack_ratio().setHeight(dPreference.getInt("back_ratio.height", 720));
        //front camera ratio
        appPara.getFront_ratio().setWidth(dPreference.getInt("front_ratio.width", 1280));
        appPara.getFront_ratio().setHeight(dPreference.getInt("front_ratio.height", 720));

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        Assist.screenWidth = dm.widthPixels;
        Assist.screenHeight = dm.heightPixels;
        /**
         * 创建目录
         */
        // File_Storage_Manage.create_File_Storage_Directory();
        // File_Storage_Manage.create_PicFile_Storage_Directory();
        // File_Storage_Manage.create_VideoFile_Storage_Directory();

        MyFileUtils.createIfNoExists(Assist.BASE_STORAGE_DIRECTORY);
        MyFileUtils.createIfNoExists(Assist.VIDEOFILE_STORAGE_DIRECTORY);
        MyFileUtils.createIfNoExists(Assist.IMAGEFILE_STORAGE_DIRECTORY);
        MyFileUtils.createIfNoExists(Assist.THUMBFILE_STORAGE_DIRECTORY);

        Assist.videoDBHelper = new VideoDBHelper(context);
        //Assist.imageDBHelper = new ImageDBHelper(context);
    }
    public static void saveAppPara(AppPara appPara, Context context) {
        if (dPreference == null) {
            dPreference = new MySharedPreference(context);
        }
//        dPreference.putIntAndCommit("exposureCompensation", appPara.getExposureCompensation());
//        dPreference.putIntAndCommit("tempFolderSize", appPara.getTempFolderSize());
        dPreference.putIntAndCommit("loopDuration", appPara.getLoopDuration());
//        dPreference.putStringAndCommit("telephone", appPara.getTelephone());
//        dPreference.putBooleanAndCommit("shutterSound", appPara.isShutterSound());
        dPreference.putBooleanAndCommit("RECsound", appPara.isRECsound());
        dPreference.putIntAndCommit("cameraId",appPara.getCurrentCameraId());

        dPreference.putStringAndCommit("savePath",appPara.getSavePath());
        dPreference.putIntAndCommit("rotationAngle",appPara.getRotationAngle());
        dPreference.putStringAndCommit("password",appPara.getPassword());
        dPreference.putIntAndCommit("video_Resolution_Ratio.width", appPara.getVideo_Resolution_Ratio().getWidth());
        dPreference.putIntAndCommit("video_Resolution_Ratio.height", appPara.getVideo_Resolution_Ratio().getHeight());
        //back camera ratio
        dPreference.putIntAndCommit("back_ratio.width", appPara.getBack_ratio().getWidth());
        dPreference.putIntAndCommit("back_ratio.height", appPara.getBack_ratio().getHeight());
        //front camera ratio
        dPreference.putIntAndCommit("front_ratio.width", appPara.getFront_ratio().getWidth());
        dPreference.putIntAndCommit("front_ratio.height", appPara.getFront_ratio().getHeight());


//        dPreference.putIntAndCommit("image_Resolution_Ratio.width", appPara.getImage_Resolution_Ratio().getWidth());
//        dPreference.putIntAndCommit("image_Resolution_Ratio.height", appPara.getImage_Resolution_Ratio().getHeight());
    }
    /**
     *
     * @return剩余存储空间
     */
    public static long getFreeMemory() { // return free memory in MB
        try {
            StatFs statFs = new StatFs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
            long blocks = statFs.getAvailableBlocks();
            long size = statFs.getBlockSize();
            long free = (blocks * size) / 1048576;
            return free;
        } catch (IllegalArgumentException e) {
            // can fail on emulator, at least!
            return -1;
        }
    }
    /**
     * 时间差 转成要显示的时间 字符串
     *
     * @param time
     *            时间差
     * @return
     */
    public static String getTimeString(long time) {
        long second = time / 1000; // 秒
        long minute = second / 60; // 秒
        second = second % 60;
        return (minute <= 9 ? "0" + minute : "" + minute) + ":" + (second <= 9 ? "0" + second : "" + second);
    }
    public static void sendBroadcast(Context context, String action) {
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String action, Boolean extra) {
        Intent intent = new Intent(action);
        intent.putExtra(Assist.CONTINUE_RECORD, extra);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String action, String text) {
        Intent intent = new Intent(action);
        intent.putExtra(action, text);
        context.sendBroadcast(intent);
    }
    /*
	 * 获取控件宽
	 */
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }

    /*
     * 获取控件高
     */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }

    /**
     * 当前 时间戳
     *
     * @param time
     * @return
     */
    public static String getFileName(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy_MM_dd_HH_mm_ss");// 文件名不能有:
        return sdf.format(time);
    }

    /**
     * 文件管理中listview显示的字符串名称
     *
     * @param time
     * @return
     */
    public static String getShowName(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy年MM月dd日HH:mm:ss");//
        return sdf.format(time);
    }

    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        try {
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
            System.out.println("w" + bitmap.getWidth());
            System.out.println("h" + bitmap.getHeight());
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取文件 --大小
     *
     * @param filepath
     *            文件路径
     * @return
     * @throws Exception
     */
    public static String getFileSizesTostring(String filepath) {
        try {
            FileInputStream fis = new FileInputStream(filepath);
            double size = fis.available();
            System.out.println(size);
            double size_MB = size / 1024 / 1024;
            DecimalFormat df = new DecimalFormat("#.##");// # api
            String st = df.format(size_MB);// 10.99m

            return st + "M";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0 + "M";
    }

    public static int getFilesize(String filepath) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(filepath);
            int size = fis.available();
            return size;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
