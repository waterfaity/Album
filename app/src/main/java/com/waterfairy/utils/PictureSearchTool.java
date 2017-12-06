package com.waterfairy.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/6
 * des  :
 */

public class PictureSearchTool {
    private static final String TAG = "pictureSearchTool";
    private List<ImgBean> fileList = new ArrayList<>();
    private String extension[] = new String[]{".png", ".jpg"};
    private boolean running;
    private int deep = 1;
    private static final PictureSearchTool PICTURE_SEARCH_TOOL = new PictureSearchTool();
    private OnSearchListener onSearchListener;


    private PictureSearchTool() {

    }

    public static PictureSearchTool getInstance() {
        return PICTURE_SEARCH_TOOL;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public void start() {
        startAsyncTask();

    }

    private void startAsyncTask() {
        AsyncTask<Void, String, List<ImgBean>> asyncTask = new AsyncTask<Void, String, List<ImgBean>>() {

            @Override
            protected List<ImgBean> doInBackground(Void... voids) {
                search(Environment.getExternalStorageDirectory(), 0, new OnSearchListener() {
                    @Override
                    public void onSearch(String path) {
                        publishProgress(path);
                    }

                    @Override
                    public void onSearchSuccess(List<ImgBean> fileList) {

                    }
                });
                return fileList;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (onSearchListener != null) onSearchListener.onSearch(values[0]);
            }

            @Override
            protected void onPostExecute(List<ImgBean> strings) {
                if (onSearchListener != null) onSearchListener.onSearchSuccess(strings);
            }
        };
        asyncTask.execute();
    }

    public void stop() {
        running = false;
    }


    private void search(File file, int deep, OnSearchListener onSearchListener) {
        Log.i(TAG, "search: " + deep);
        if (file.exists() && deep < this.deep) {
            File[] list = file.listFiles();
            if (list != null) {
                boolean jump = false;
                for (File childFile : list) {
                    if (childFile.isDirectory()) {
                        search(childFile, deep + 1, onSearchListener);
                    } else if (!jump) {
                        String childAbsolutePath = childFile.getAbsolutePath();
                        for (String anExtension : extension) {
                            if (childAbsolutePath.endsWith(anExtension)) {
                                fileList.add(new ImgBean(file.getAbsolutePath(), childAbsolutePath));
                                jump = true;
                                break;
                            }
                        }
                        if (jump) {
                            if (onSearchListener != null)
                                onSearchListener.onSearch(childAbsolutePath);
                        }
                    }
                }
            }
        }
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public interface OnSearchListener {
        void onSearch(String path);

        void onSearchSuccess(List<ImgBean> fileList);
    }

    public class ImgBean {
        public ImgBean(String path, String firstImgPath) {
            this.path = path;
            this.firstImgPath = firstImgPath;
        }

        private String path;
        private String firstImgPath;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFirstImgPath() {
            return firstImgPath;
        }

        public void setFirstImgPath(String firstImgPath) {
            this.firstImgPath = firstImgPath;
        }
    }
}