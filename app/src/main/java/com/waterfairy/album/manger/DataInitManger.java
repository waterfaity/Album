package com.waterfairy.album.manger;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/11/28
 * @Description:
 */

public class DataInitManger {
    private static DataInitManger dataInitManger;

    private DataInitManger() {
    }

    public static DataInitManger getInstance() {
        if (dataInitManger == null) {
            dataInitManger = new DataInitManger();
        }
        return dataInitManger;
    }

    public void init() {
        //数据库
        DataBaseManger.getInstance().initDataBase();

    }

}
