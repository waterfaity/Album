package com.waterfairy.album.activity;

import android.content.Intent;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.waterfairy.album.R;
import com.waterfairy.album.http.HttpConfig;
import com.waterfairy.album.http.RetrofitService;
import com.waterfairy.album.utils.ShareTool;
import com.waterfairy.http.client.RetrofitHttpClient;
import com.waterfairy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    List<String> strings = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        for (int i = 0; i < 100; i++) {
            strings.add("hhh" + i);
        }
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        findViewById(R.id.fab).setOnClickListener(this);
    }

    RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(HomeActivity.this).inflate(android.R.layout.activity_list_item, parent, false)) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView
                    = holder.itemView.findViewById(android.R.id.text1);
            textView.setText(strings.get(position));

        }

        @Override
        public int getItemCount() {
            return strings.size();
        }
    };


    private void queryPhoto() {
        long userId = ShareTool.getInstance().getUserId();
        if (userId == 0) {
            ToastUtils.show("用户未登录,请重新登录");
            return;
        }
        RetrofitService retrofitService = RetrofitHttpClient.build(HttpConfig.BASE_URL).getRetrofit().create(RetrofitService.class);
        retrofitService.queryPhoto(userId);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            startActivity(new Intent(this, SelectFileActivity.class));
        } else if (v.getId() == R.id.quit) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
