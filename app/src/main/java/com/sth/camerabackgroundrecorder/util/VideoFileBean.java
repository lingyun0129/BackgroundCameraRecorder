package com.sth.camerabackgroundrecorder.util;

public class VideoFileBean {

    private long starttime;
    private long endtime;
    private String path;
    private String name;

    private String thumbpath;
    private String size;
    private String resolution_ratio;
    private boolean onUploadSuccess;
    private boolean onCrash;
    private boolean foreverSave;

    public boolean isForeverSave() {
        return foreverSave;
    }

    public void setForeverSave(boolean foreverSave) {
        this.foreverSave = foreverSave;
    }

    public VideoFileBean() {
        super();
    }

    public VideoFileBean(long starttime, long endtime, String path, String name, String thumbpath, String size, String resolution_ratio, String showName) {
        super();
        this.starttime = starttime;
        this.endtime = endtime;
        this.path = path;
        this.name = name;
        this.thumbpath = thumbpath;
        this.size = size;
        this.resolution_ratio = resolution_ratio;
        this.showName = showName;
    }

    public boolean isOnCrash() {
        return onCrash;
    }

    public void setOnCrash(boolean onCrash) {
        this.onCrash = onCrash;
    }

    public boolean isOnUploadSuccess() {
        return onUploadSuccess;
    }

    public void setOnUploadSuccess(boolean onUploadSuccess) {
        this.onUploadSuccess = onUploadSuccess;
    }

    public String getResolution_ratio() {
        return resolution_ratio;
    }

    public void setResolution_ratio(String resolution_ratio) {
        this.resolution_ratio = resolution_ratio;
    }

    private String showName;

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThumbpath() {
        return thumbpath;
    }

    public void setThumbpath(String thumbpath) {
        this.thumbpath = thumbpath;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
