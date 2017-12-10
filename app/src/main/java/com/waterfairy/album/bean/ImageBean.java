package com.waterfairy.album.bean;

import com.waterfairy.album.http.HttpConfig;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/10
 * des  :
 */

public class ImageBean {
    //    {"address":"","id":5.0,"photodate":"2017-12-09","photourl":"/resources/upload/blog/image\\23\\7image20171209170755518257.png","userid":7.0}
    private String address;
    private long id;
    private String photodate;
    private String photourl;
    private long userid;

    private int pos;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhotodate() {
        return photodate;
    }

    public void setPhotodate(String photodate) {
        this.photodate = photodate;
    }

    public String getPhotourl() {
        String photourl = HttpConfig.BASE_URL + this.photourl.replace("\\", "/");
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }
}
