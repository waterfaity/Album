package com.waterfairy.album.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waterfairy.album.R;
import com.waterfairy.album.manger.DataInitManger;
import com.waterfairy.album.utils.ShareTool;
import com.waterfairy.utils.PermissionUtils;

/**
 * 欢迎页
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //检查权限(android 5.0  )
        requestPermission();
    }

    private void requestPermission() {
        if (PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_STORAGE)) {
            initData();
        }
    }

    private void initData() {
        DataInitManger.getInstance().init();
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//                if (!ShareTool.getInstance().isLogin()) {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
//                } else {
//                    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
//                }
                finish();
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.onRequestPermissionsResultForSDCard(permissions, grantResults)) {
            initData();
        } else {
            requestPermission();
        }
    }
}
