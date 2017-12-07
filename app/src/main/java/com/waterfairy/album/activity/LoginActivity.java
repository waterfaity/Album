package com.waterfairy.album.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.waterfairy.album.R;
import com.waterfairy.album.bean.UserBean;
import com.waterfairy.album.database.greendao.UserDBDao;
import com.waterfairy.album.http.HttpConfig;
import com.waterfairy.album.http.RetrofitService;
import com.waterfairy.album.manger.DataBaseManger;
import com.waterfairy.album.utils.ShareTool;
import com.waterfairy.http.callback.BaseCallback;
import com.waterfairy.http.client.RetrofitHttpClient;
import com.waterfairy.http.response.BaseResponse;
import com.waterfairy.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity {
    private UserDBDao userDBDao;
    private TextView tvAccount, tvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        tvAccount = findViewById(R.id.account);
        tvPassword = findViewById(R.id.password);
        String account = ShareTool.getInstance().getAccount();
        String password = ShareTool.getInstance().getPassword();
        tvAccount.setText(account);
        tvPassword.setText(password);
    }

    private void initData() {
        userDBDao = DataBaseManger.getInstance().getDaoSession().getUserDBDao();

    }

    public void login(View view) {
        final String account = ((TextView) findViewById(R.id.account)).getText().toString();
        final String password = ((TextView) findViewById(R.id.password)).getText().toString();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show("请输入帐号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show("请输入密码");
            return;
        }
        RetrofitHttpClient.build(HttpConfig.BASE_URL, true, true)
                .getRetrofit().create(RetrofitService.class)
                .login(account, password).enqueue(new BaseCallback<BaseResponse<UserBean>>() {
            @Override
            public void onSuccess(BaseResponse<UserBean> baseResponse) {
                ToastUtils.show("登录成功");
                UserBean data = baseResponse.getData();
                ShareTool.getInstance().saveAccount(account);
                ShareTool.getInstance().savePassword(password);
                ShareTool.getInstance().saveUserId(data.getId());
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onFailed(int code, String message) {
                ToastUtils.show(message);
            }
        });
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
