package com.waterfairy.album.manger;

import com.waterfairy.album.application.MyApp;
import com.waterfairy.album.database.greendao.DaoMaster;
import com.waterfairy.album.database.greendao.DaoSession;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/11/25
 * des  :数据库管理
 */

public class DataBaseManger {
    private static DataBaseManger DATA_BASE_MANGER;
    private DaoSession daoSession;

    private DataBaseManger() {

    }

    public static DataBaseManger getInstance() {
        if (DATA_BASE_MANGER == null) {
            DATA_BASE_MANGER = new DataBaseManger();
        }
        return DATA_BASE_MANGER;
    }

    public void initDataBase() {
        DaoMaster.DevOpenHelper devOpenHelper =
                new DaoMaster.DevOpenHelper(
                        MyApp.getApp(),
                        "reminder-db",
                        null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        if (daoSession == null) {
            initDataBase();
        }
        return daoSession;
    }
}
