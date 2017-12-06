package com.waterfairy.album.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterfairy.album.R;
import com.waterfairy.utils.PictureSearchTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectFileActivity extends AppCompatActivity implements PictureSearchTool.OnSearchListener, View.OnClickListener {

    private static final String TAG = "selectFile";
    private RecyclerView recyclerView;
    private Dialog dialog;
    private PictureSearchTool pictureSearchTool;
    private TextView textView;
    private ImageView back, refresh;
    private int currentDeep;
    private List<String> deepList;
    private List<PictureSearchTool.ImgBean> imgList;
    private int widthImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        pictureSearchTool = PictureSearchTool.getInstance();
        pictureSearchTool.setDeep(3);
        pictureSearchTool.setOnSearchListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        back = findViewById(R.id.back);
        refresh = findViewById(R.id.refresh);
        back.setOnClickListener(this);
        refresh.setOnClickListener(this);
        deepList = new ArrayList<>();
        imgList = new ArrayList<>();
        widthImg = getResources().getDisplayMetrics().widthPixels / 3;

    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pictureSearchTool.stop();
                dialog.dismiss();
            }
        });
        builder.setTitle("搜索中...");
        builder.setView(textView = new TextView(this, null));
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin), 0, 0, 0);
        textView.setText("/storage/emulated/0/");
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onSearch(String path) {
        Log.i(TAG, "onSearch: " + path);
        textView.setText(path);
    }

    @Override
    public void onSearchSuccess(List<PictureSearchTool.ImgBean> fileList) {
        imgList = fileList;
        dialog.dismiss();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (currentDeep == 0) finish();
                break;
            case R.id.refresh:
                showSearchDialog();
                pictureSearchTool.start();
                break;
        }
    }

    private RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(SelectFileActivity.this).inflate(R.layout.item_img_folder, parent, false);
            return new RecyclerView.ViewHolder(inflate) {

            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RelativeLayout rootView = holder.itemView.findViewById(R.id.root_view);
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            layoutParams.width = widthImg;
            layoutParams.height = widthImg;
            rootView.setLayoutParams(layoutParams);


            PictureSearchTool.ImgBean imgBean = imgList.get(position);
            ImageView imageView = rootView.findViewById(R.id.image);
            TextView textView = rootView.findViewById(R.id.title);
            imageView.setPadding(5, 5, 5, 5);
            Glide.with(SelectFileActivity.this)
                    .load(new File(imgBean.getFirstImgPath()))
                    .centerCrop().into(imageView);
            textView.setText(new File(imgBean.getPath()).getName());
        }

        @Override
        public int getItemCount() {
            if (imgList == null) return 0;
            return imgList.size();
        }
    };
}
