package com.waterfairy.album.bean;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/7
 * des  :
 */

public class UserBean {

    //    {"id":7,"password":"123456","tel":"13764312271","username":"water"}
    private int id;
    private String password;
    private String tel;
    private String username;
    private String address;
    private int pos;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
