package com.growin.silveryogaapp.data;

import android.net.Uri;

import java.io.Serializable;

public class Content {

    private int imgId;
    private String title;
    private Uri imgUri;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }



    //추가★
    private int idx;
    private int cnt;
    private String group;
    private String videoId;
    //추가☆
    private String imgPath;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String vedioId) {
        this.videoId = vedioId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
