package com.waterfairy.album.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterfairy.album.R;
import com.waterfairy.album.bean.ImageBean;
import com.waterfairy.album.dialog.LoadingDialog;
import com.waterfairy.album.http.HttpConfig;
import com.waterfairy.album.http.RetrofitService;
import com.waterfairy.album.utils.ShareTool;
import com.waterfairy.http.callback.BaseCallback;
import com.waterfairy.http.client.RetrofitHttpClient;
import com.waterfairy.http.response.BaseResponse;
import com.waterfairy.retrofit2.upload.BaseProgress;
import com.waterfairy.retrofit2.upload.UploadRequestBody;
import com.waterfairy.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, BaseProgress {
    private static final String TAG = "home";


    private RecyclerView mRecyclerView;
    private LoadingDialog loadingDialog;
    private int currentNum;
    private int totalNum;
    Call<BaseResponse> baseResponseCall;
    private List<ImageBean> mDataList;
    private int itemWith;
    private SwipeRefreshLayout mSwipeRefresh;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userId = ShareTool.getInstance().getUserId();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        itemWith = getResources().getDisplayMetrics().widthPixels / 3;
        mDataList = new ArrayList<>();
        mRecyclerView.setAdapter(adapter);
        if (userId == 0) {
            findViewById(R.id.fab).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.quit)).setText("关闭");
        } else {
            findViewById(R.id.fab).setOnClickListener(this);
        }
        mSwipeRefresh = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefresh.setColorSchemeColors(new int[]{getResources().getColor(R.color.colorMain), getResources().getColor(R.color.colorAccent)});
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPhoto();
            }
        });
        queryPhoto();
    }

    RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(HomeActivity.this)
                    .inflate(R.layout.item_net_show, parent, false)) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView imageView = holder.itemView.findViewById(R.id.img);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = itemWith;
            layoutParams.width = itemWith;
            imageView.setLayoutParams(layoutParams);
            final ImageBean imageBean = mDataList.get(position);
            imageBean.setPos(position);
            Glide.with(HomeActivity.this).load(imageBean.getPhotourl()).centerCrop().into(imageView);
            Log.i(TAG, "onBindViewHolder: " + position);
            holder.itemView.setTag(imageBean);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, ImageShowActivity.class);
                    intent.putExtra("url", ((ImageBean) v.getTag()).getPhotourl());
                    startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final ImageBean imageBean1 = (ImageBean) v.getTag();
                    AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).setTitle("是否删除该图片?")
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
                                    delete(imageBean1.getId(), imageBean1.getPos());
                                }
                            }).create();
                    alertDialog.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mDataList != null) return mDataList.size();
            return 0;
        }
    };


    private void delete(long imgId, final int pos) {
        RetrofitService retrofitService = RetrofitHttpClient.build(HttpConfig.BASE_URL, true, true)
                .getRetrofit().create(RetrofitService.class);
        retrofitService.deleteImg(imgId).enqueue(new BaseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                mDataList.remove(pos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(int code, String message) {
                ToastUtils.show(message);
            }
        });
    }

    private void queryPhoto() {
        RetrofitService retrofitService = RetrofitHttpClient.build(HttpConfig.BASE_URL, true, true)
                .getRetrofit().create(RetrofitService.class);
        Call<BaseResponse<List<ImageBean>>> responseCall = retrofitService.queryImgList(userId);
        responseCall.enqueue(new BaseCallback<BaseResponse<List<ImageBean>>>() {
            @Override
            public void onSuccess(BaseResponse<List<ImageBean>> baseResponse) {
                mDataList.removeAll(mDataList);
                List<ImageBean> data = baseResponse.getData();
                Collections.reverse(data);
                mDataList.addAll(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailed(int code, String message) {
                ToastUtils.show("图片获取失败");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            startActivityForResult(new Intent(this, SelectFileActivity.class), 1);
        } else if (v.getId() == R.id.quit) {
            if (userId != 0) {
                ShareTool.getInstance().savePassword("");
                ShareTool.getInstance().saveAccount("");
                ShareTool.getInstance().saveUserId(0);
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            upload(data);
        }
    }

    private void upload(Intent data) {
        //上传文件
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            baseResponseCall.cancel();
                            ToastUtils.show("取消上传");
                        }
                    });
                }
            });
            loadingDialog.setTitle("上传中");
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        loadingDialog.show();
        if (data == null) return;
        List<String> dataList = data.getStringArrayListExtra("data");
        if (dataList == null) return;
        List<MultipartBody.Part> mulPart = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            File file = new File(dataList.get(i));
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"), file);
            UploadRequestBody uploadRequestBody = new UploadRequestBody(requestBody, this);
            MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), uploadRequestBody);
            mulPart.add(body);
        }
        totalNum = mulPart.size();
        currentNum = 0;

        //开始上传
        RetrofitService retrofitService = RetrofitHttpClient.build(HttpConfig.BASE_URL).getRetrofit().create(RetrofitService.class);
        ShareTool shareTool = ShareTool.getInstance();
        baseResponseCall = retrofitService.uploadMul(shareTool.getUserId(), shareTool.getAddress(), mulPart);
        baseResponseCall.enqueue(new BaseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                loadingDialog.dismiss();
                Log.i(TAG, "onSuccess: ");
                ToastUtils.show("上传成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        queryPhoto();
                    }
                });
            }

            @Override
            public void onFailed(int code, String message) {
                Log.i(TAG, "onFailed: ");
                loadingDialog.dismiss();
                ToastUtils.show("上传失败");
            }
        });
    }

    @Override
    public void onDownloading(boolean upload, long contentLength, long bytesWritten) {
        if (upload) {
            currentNum++;
        }
        final int ratio = (int) ((bytesWritten / ((float) contentLength)) * 100);
        Log.i(TAG, "onDownloading: " + upload + "--" + ((int) ((bytesWritten / ((float) contentLength)) * 100)));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.setText("第" + (currentNum) + "/" + totalNum + "张:" + ratio + "%");
            }
        });
    }
}
