package com.waterfairy.album.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.waterfairy.album.R;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

public class ImageShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        PhotoView view = findViewById(R.id.image);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String path = intent.getStringExtra("path");
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).into(view);
        } else if (!TextUtils.isEmpty(path)) {
            Glide.with(this).load(new File(path)).into(view);
        }

    }

    public void back(View view) {
        finish();
    }
}
