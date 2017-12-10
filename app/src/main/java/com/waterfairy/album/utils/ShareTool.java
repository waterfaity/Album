package com.waterfairy.album.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.waterfairy.album.application.MyApp;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/11/25
 * des  :
 */

public class ShareTool {
    private static ShareTool shareTool;
    public static final String IS_LOGIN = "isLogin";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String USER_ID = "userId";
    public static final String ADDRESS = "address";
    public static final String SETTING = "setting";
    private SharedPreferences sharedPreferences;

    private ShareTool() {
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static ShareTool getInstance() {
        if (shareTool == null) {
            shareTool = new ShareTool();

        }
        return shareTool;
    }

    public void initShare() {
        if (sharedPreferences == null) {
            sharedPreferences = MyApp.getApp().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        }
    }

    public void saveLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean(IS_LOGIN, isLogin).apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void saveAccount(String account) {
        sharedPreferences.edit().putString(ACCOUNT, account).apply();
        saveLogin(true);
    }

    public void savePassword(String password) {
        sharedPreferences.edit().putString(PASSWORD, password).apply();
    }
    public void saveAddress(String address) {
        sharedPreferences.edit().putString(ADDRESS, address).apply();
    }

    public String getAccount() {
        return sharedPreferences.getString(ACCOUNT, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public long getUserId() {
        return sharedPreferences.getInt(USER_ID, 0);

    }

    public String getAddress() {
        return sharedPreferences.getString(ADDRESS, "");

    }

    public void saveUserId(int userId) {
        sharedPreferences.edit().putInt(USER_ID, userId).apply();
    }
}
