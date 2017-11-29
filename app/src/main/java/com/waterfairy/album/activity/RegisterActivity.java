package com.waterfairy.album.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.waterfairy.album.R;
import com.waterfairy.album.database.UserDB;
import com.waterfairy.album.database.greendao.UserDBDao;
import com.waterfairy.album.manger.DataBaseManger;
import com.waterfairy.album.utils.ShareTool;
import com.waterfairy.utils.ToastUtils;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private UserDBDao userDBDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initData();
    }

    private void initData() {
        userDBDao = DataBaseManger.getInstance().getDaoSession().getUserDBDao();
    }

    public void register(View view) {

        String account = ((TextView) findViewById(R.id.account)).getText().toString();
        String password = ((TextView) findViewById(R.id.password)).getText().toString();
        String ensurePassword = ((TextView) findViewById(R.id.ensure_password)).getText().toString();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show("请输入帐号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(ensurePassword)) {
            ToastUtils.show("请输入确认密码");
            return;
        }

        List<UserDB> list = userDBDao.queryBuilder().where(UserDBDao.Properties.Account.eq(account)).list();
        if (list == null || list.size() == 0) {
            //可以注册
            if (!TextUtils.equals(password, ensurePassword)) {
                ToastUtils.show("两次密码输入不一致");
            } else {
                //注册 保存
                ShareTool.getInstance().saveAccount(account);
                userDBDao.insert(new UserDB(account, password));
                ToastUtils.show("注册成功!");
                finish();
            }
        } else {
            ToastUtils.show("该账号已经注册!");
        }
    }

    public void back(View view) {
        finish();
    }
}
