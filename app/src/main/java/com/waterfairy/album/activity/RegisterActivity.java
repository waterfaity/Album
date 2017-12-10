package com.waterfairy.album.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.waterfairy.album.R;
import com.waterfairy.album.database.UserDB;
import com.waterfairy.album.database.greendao.UserDBDao;
import com.waterfairy.album.http.HttpConfig;
import com.waterfairy.album.http.RetrofitService;
import com.waterfairy.album.manger.DataBaseManger;
import com.waterfairy.album.utils.ShareTool;
import com.waterfairy.http.callback.BaseCallback;
import com.waterfairy.http.client.RetrofitHttpClient;
import com.waterfairy.http.response.BaseResponse;
import com.waterfairy.utils.ToastUtils;

import java.util.List;

import retrofit2.Retrofit;

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

        String userName = ((TextView) findViewById(R.id.user_name)).getText().toString();
        String tel = ((TextView) findViewById(R.id.account)).getText().toString();
        String password = ((TextView) findViewById(R.id.password)).getText().toString();
        String ensurePassword = ((TextView) findViewById(R.id.ensure_password)).getText().toString();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.show("请输入帐号");
            return;
        }
        if (TextUtils.equals(userName, "admin")) {
            ToastUtils.show("帐号不能为admin(管理员账号)");
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            ToastUtils.show("请输入手机号");
            return;
        }
        if (tel.length() != 11) {
            ToastUtils.show("请输入11位手机号");
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
        if (!TextUtils.equals(password, ensurePassword)) {
            ToastUtils.show("两次密码输入不一致");
            return;
        }
        RetrofitService retrofitService = RetrofitHttpClient.build(HttpConfig.BASE_URL, true, true).getRetrofit().create(RetrofitService.class);
        retrofitService.regist(userName, password, tel).enqueue(new BaseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                ToastUtils.show("注册成功");
                finish();
            }

            @Override
            public void onFailed(int code, String message) {
                ToastUtils.show(message);
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
