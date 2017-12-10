package com.waterfairy.album.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waterfairy.album.R;
import com.waterfairy.album.bean.UserBean;
import com.waterfairy.album.http.HttpConfig;
import com.waterfairy.album.http.RetrofitService;
import com.waterfairy.album.utils.ShareTool;
import com.waterfairy.http.callback.BaseCallback;
import com.waterfairy.http.client.RetrofitHttpClient;
import com.waterfairy.http.response.BaseResponse;
import com.waterfairy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    private List<UserBean> accountList;
    private int paddingTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        paddingTop = (int) (getResources().getDisplayMetrics().density * 10);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountList = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        queryUserAccount();
    }

    private RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(
                    LayoutInflater.from(ManagerActivity.this).inflate(R.layout.item_account, null, false)) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = holder.itemView.findViewById(R.id.text);
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            textView.setLayoutParams(layoutParams);
            final UserBean userBean = accountList.get(position);
            textView.setText(position + 1 + "." + userBean.getUsername());
            userBean.setPos(position);
            textView.setTag(userBean);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final UserBean userBean1 = (UserBean) v.getTag();
                    AlertDialog alertDialog = new AlertDialog.Builder(ManagerActivity.this).setTitle("是否删除" + userBean1.getUsername() + "这个账号?")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    deleteUser(userBean1);
                                }
                            }).create();
                    alertDialog.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            if (accountList != null) return accountList.size();
            return 0;
        }
    };

    private void deleteUser(final UserBean userBean) {
        RetrofitService retrofitService = RetrofitHttpClient.
                build(HttpConfig.BASE_URL, true, true).
                getRetrofit().
                create(RetrofitService.class);
        retrofitService.deleteAccount(userBean.getId()).enqueue(new BaseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                accountList.remove(userBean.getPos());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String message) {
                ToastUtils.show(message);
            }
        });
    }

    private void queryUserAccount() {
        RetrofitService retrofitService = RetrofitHttpClient.
                build(HttpConfig.BASE_URL, true, true).
                getRetrofit().
                create(RetrofitService.class);
        retrofitService.queryUserAccount().enqueue(new BaseCallback<BaseResponse<List<UserBean>>>() {
            @Override
            public void onSuccess(final BaseResponse<List<UserBean>> listBaseResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        accountList.removeAll(accountList);
                        accountList.addAll(listBaseResponse.getData());
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailed(int code, String message) {
                ToastUtils.show(message);
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.quit) {
            ShareTool.getInstance().savePassword("");
            ShareTool.getInstance().saveAccount("");
            ShareTool.getInstance().saveUserId(0);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
