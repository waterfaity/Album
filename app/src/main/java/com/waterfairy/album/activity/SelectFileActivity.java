package com.waterfairy.album.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterfairy.album.R;
import com.waterfairy.utils.PictureSearchTool;
import com.waterfairy.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SelectFileActivity extends AppCompatActivity implements PictureSearchTool.OnSearchListener, View.OnClickListener {

    private static final String TAG = "selectFile";
    private RecyclerView recyclerView;
    private Dialog dialog;
    private PictureSearchTool pictureSearchTool;
    private TextView textView;
    private ImageView back;
    private Button mBTEnsure;
    private List<String> deepList;
    private List<PictureSearchTool.ImgBean> imgPathList;
    private List<PictureSearchTool.ImgBean> dataList;
    private int widthImg;
    private boolean isPath = true;
    private TextView mTitle;
    private int maxNum = 9;
    private int widthNum = 3;
    private int deep = 3;//扫描深度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        findView();
        initView();
        initData();
    }

    private void findView() {
        mTitle = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recyclerView);
        mBTEnsure = findViewById(R.id.bt_ensure);
        back = findViewById(R.id.back);
    }

    private void initView() {
        maxNum = getIntent().getIntExtra("maxNum", maxNum);//最多选择张数
        widthNum = getIntent().getIntExtra("widthNum", widthNum);//n列
        recyclerView.setLayoutManager(new GridLayoutManager(this, widthNum));
        mBTEnsure.setOnClickListener(this);
        mBTEnsure.setText("未选择");
        back.setOnClickListener(this);
    }

    private void initData() {
        deepList = new ArrayList<>();
        imgPathList = new ArrayList<>();
        deep = getIntent().getIntExtra("deep", deep);
        widthImg = getResources().getDisplayMetrics().widthPixels / widthNum;
        pictureSearchTool = PictureSearchTool.getInstance();
        pictureSearchTool.setDeep(deep);
        pictureSearchTool.setOnSearchListener(this);
        showSearchDialog();   //搜索
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
                    mTitle.setText("文件夹");
                }
                break;
            case R.id.bt_ensure:
                onSelectOk();
                break;
        }
    }

    /**
     * 获取选择的路径
     *
     * @return
     */
    private ArrayList<String> getSelectPath() {
        Set<String> paths = selectImgBeanHashMap.keySet();
        ArrayList<String> stringList = new ArrayList<>();
        for (String imgPath : paths) {
            PictureSearchTool.ImgBean imgBean = selectImgBeanHashMap.get(imgPath);
            Boolean open = imgBean.isOpen();
            if (open) {
                stringList.add(imgBean.getFirstImgPath());
            }
        }
        return stringList;
    }

    private void onSelectOk() {
        ArrayList<String> stringList = getSelectPath();
        if (stringList.size() == 0) {
            ToastUtils.show("还没有选择图片");
        } else {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("data", stringList);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private HashMap<String, PictureSearchTool.ImgBean> selectImgBeanHashMap = new HashMap<>();

    private RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(SelectFileActivity.this).inflate(R.layout.item_img_folder, parent, false);
            return new RecyclerView.ViewHolder(inflate) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            //view
            RelativeLayout rootView = holder.itemView.findViewById(R.id.root_view);
            View foreGroundView = holder.itemView.findViewById(R.id.fore_ground);
            foreGroundView.setBackgroundResource(R.drawable.bg_fore_ground);//前景色
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            layoutParams.width = widthImg;
            layoutParams.height = widthImg;
            rootView.setLayoutParams(layoutParams);
            CheckBox checkBox = rootView.findViewById(R.id.checkbox);//checkBox
            final ImageView imageView = rootView.findViewById(R.id.image);//图片展示

            TextView textView = rootView.findViewById(R.id.title);//文件夹显示
            //监听
            checkBox.setOnCheckedChangeListener(null);
            //data
            final PictureSearchTool.ImgBean imgBean = dataList.get(position);
            imgBean.setPos(position);
            String imgPath = imgBean.getFirstImgPath();
            Glide.with(SelectFileActivity.this)
                    .load(new File(imgPath))
                    .centerCrop().into(imageView);
            if (imgBean.isImg()) {
                //图片,可以选择
                textView.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);
                //checkBox事件
                checkBox.setTag(imgBean);
                boolean open = false;
                if (selectImgBeanHashMap != null) {
                    PictureSearchTool.ImgBean selectImageBean = selectImgBeanHashMap.get(imgPath);
                    open = (selectImageBean != null && selectImageBean.isOpen());
                }
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(open);
                checkBox.setOnCheckedChangeListener(getCheckListener());
            } else {
                //文件夹,不可以选择
                checkBox.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                String name = new File(imgBean.getPath()).getName();
                switch (name) {
                    case "DCIM":
                        name = "相册(DCIM)";
                        break;
                    case "Camera":
                        name = "相机(Camera)";
                        break;
                    case "Download":
                        name = "下载(Download)";
                        break;
                    case "Screenshots":
                        name = "截图(Screenshots)";
                        break;
                }
                textView.setText(name);
            }
            //点击事件
            rootView.setTag(imgBean);
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
                        String name = new File(imgBean1.getPath()).getName();
                        mTitle.setText(name);
                        handleFolderImg(imgBean1);
                        Log.i(TAG, "onClick: " + imgBean1.getPath());
                    }
                }
            });
        }

        private CompoundButton.OnCheckedChangeListener getCheckListener() {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int selectSize = getSelectPath().size();
                    if (isChecked && selectSize == maxNum) {
                        buttonView.setOnCheckedChangeListener(null);
                        buttonView.setChecked(false);
                        buttonView.setOnCheckedChangeListener(getCheckListener());
                        ToastUtils.show("最多选择" + maxNum + "张图片");
                    } else {
                        PictureSearchTool.ImgBean imgBean = (PictureSearchTool.ImgBean) buttonView.getTag();
                        imgBean.setOpen(isChecked);
                        selectImgBeanHashMap.put((imgBean).getFirstImgPath(), imgBean);
                        setEnsureButton();
                    }
                }
            };
        }

        @Override
        public int getItemCount() {
            if (dataList == null) return 0;
            return dataList.size();
        }
    };

    private void setEnsureButton() {
        ArrayList<String> ensurePath = getSelectPath();
        if (ensurePath.size() == 0) {
            mBTEnsure.setText("未选择");
        } else {
            String text = "完成(" + ensurePath.size() + "/" + maxNum + ")";
            mBTEnsure.setText(text);
        }
    }

    private void handleFolderImg(PictureSearchTool.ImgBean imgBean) {
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
