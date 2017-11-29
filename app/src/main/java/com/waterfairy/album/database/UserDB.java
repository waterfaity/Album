package com.waterfairy.album.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/11/25
 * des  :账号
 */
@Entity
public class UserDB {
    public UserDB(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Generated(hash = 239582963)
    public UserDB(Long id, String account, String password, String email,
            String userName) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
        this.userName = userName;
    }

    @Generated(hash = 1312299826)
    public UserDB() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Id
    private Long id;

    private String account;
    private String password;
    private String email;
    private String userName;
}
