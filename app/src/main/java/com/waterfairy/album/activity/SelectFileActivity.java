package com.waterfairy.album.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterfairy.album.R;
import com.waterfairy.album.http.HttpConfig;
import com.waterfairy.album.http.RetrofitService;
import com.waterfairy.album.utils.ShareTool;
import com.waterfairy.http.callback.BaseCallback;
import com.waterfairy.http.client.RetrofitHttpClient;
import com.waterfairy.http.response.BaseResponse;
import com.waterfairy.utils.PictureSearchTool;
import com.waterfairy.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SelectFileActivity extends AppCompatActivity implements PictureSearchTool.OnSearchListener, View.OnClickListener {

    private static final String TAG = "selectFile";
    private RecyclerView recyclerView;
    private Dialog dialog;
    private PictureSearchTool pictureSearchTool;
    private TextView textView;
    private ImageView back, upload;
    private int currentDeep;
    private List<String> deepList;
    private List<PictureSearchTool.ImgBean> imgPathList;
    private List<PictureSearchTool.ImgBean> dataList;
    private int widthImg;
    private boolean isPath = true;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        mTitle = findViewById(R.id.title);
        pictureSearchTool = PictureSearchTool.getInstance();
        pictureSearchTool.setDeep(3);
        pictureSearchTool.setOnSearchListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        back = findViewById(R.id.back);
        upload = findViewById(R.id.upload);
        upload.setVisibility(View.GONE);
        back.setOnClickListener(this);
        upload.setOnClickListener(this);
        deepList = new ArrayList<>();
        imgPathList = new ArrayList<>();
        widthImg = getResources().getDisplayMetrics().widthPixels / 3;

        //搜索
        showSearchDialog();
        pictureSearchTool.start();

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
        isPath = true;
        imgPathList = fileList;
        dataList = new ArrayList<>();
        dataList.addAll(imgPathList);
        dialog.dismiss();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (isPath) finish();
                else {
                    isPath = true;
                    dataList.removeAll(dataList);
                    dataList.addAll(imgPathList);
                    adapter.notifyDataSetChanged();
                    mTitle.setText("");
                    upload.setVisibility(View.GONE);
                }
                break;
            case R.id.upload:
                upload();
                break;
        }
    }

    private void upload() {
        if (selectHashMap.size() == 0) ToastUtils.show("还没有选择图片");
        else {
            Set<Integer> integers = selectHashMap.keySet();
            ArrayList<String> stringList = new ArrayList<>();
            for (Integer position : integers) {
                Boolean aBoolean = selectHashMap.get(position);
                if (aBoolean != null) {
                    stringList.add(dataList.get(position).getFirstImgPath());
                }
            }

            if (stringList.size() == 0) {
                ToastUtils.show("还没有选择图片");
            } else {

                Intent intent = new Intent();
                intent.putStringArrayListExtra("data", stringList);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private HashMap<Integer, Boolean> selectHashMap = new HashMap<>();
    private RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(SelectFileActivity.this).inflate(R.layout.item_img_folder, parent, false);
            return new RecyclerView.ViewHolder(inflate) {

            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            RelativeLayout rootView = holder.itemView.findViewById(R.id.root_view);
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            CheckBox checkBox = rootView.findViewById(R.id.checkbox);
            layoutParams.width = widthImg;
            layoutParams.height = widthImg;
            rootView.setLayoutParams(layoutParams);


            final PictureSearchTool.ImgBean imgBean = dataList.get(position);
            final ImageView imageView = rootView.findViewById(R.id.image);
            imageView.setPadding(5, 5, 5, 5);
            Glide.with(SelectFileActivity.this)
                    .load(new File(imgBean.getFirstImgPath()))
                    .centerCrop().into(imageView);
            TextView textView = rootView.findViewById(R.id.title);
            if (imgBean.isImg()) {
                textView.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);
                if (selectHashMap != null) {
                    Boolean aBoolean = selectHashMap.get(position);
                    checkBox.setOnCheckedChangeListener(null);
                    checkBox.setChecked(aBoolean == null ? false : aBoolean);
                    checkBox.setOnCheckedChangeListener(getListener());

                }
            } else {
                checkBox.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText(new File(imgBean.getPath()).getName());
            }

            checkBox.setTag(position);
            rootView.setTag(imgBean);
            checkBox.setOnCheckedChangeListener(getListener());
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureSearchTool.ImgBean imgBean1 = (PictureSearchTool.ImgBean) v.getTag();
                    if (imgBean1.isImg()) {
                        //show img
                        mTitle.setText("");
                        Intent intent = new Intent(SelectFileActivity.this, ImageShowActivity.class);
                        intent.putExtra("path", imgBean1.getFirstImgPath());
                        startActivity(intent);
                    } else {
                        mTitle.setText(new File(imgBean1.getPath()).getName());
                        handleFolderImg(imgBean1);
                        upload.setVisibility(View.VISIBLE);
                        Log.i(TAG, "onClick: " + imgBean1.getPath());
                    }
                }
            });

        }

        private CompoundButton.OnCheckedChangeListener getListener() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectHashMap.put((int) buttonView.getTag(), isChecked);
                }
            };
        }

        @Override
        public int getItemCount() {
            if (dataList == null) return 0;
            return dataList.size();
        }
    };

    private void handleFolderImg(PictureSearchTool.ImgBean imgBean) {
        selectHashMap = new HashMap<>();
        dataList.removeAll(dataList);
        dataList.addAll(pictureSearchTool.searchFolder(imgBean.getPath()));
        isPath = false;
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onClick(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
