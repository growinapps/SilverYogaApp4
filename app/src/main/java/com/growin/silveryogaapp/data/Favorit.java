package com.growin.silveryogaapp.data;


//Favorit 테이블에 Data Insert, Delete 용도
//기본으로는 Content class를 써야함
public class Favorit {

    private String idx;
    private String img;
    private String mail;
    private String mail_video;
    private String name;
    private String video;


    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail_video() {
        return mail_video;
    }

    public void setMail_video(String mail_video) {
        this.mail_video = mail_video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
