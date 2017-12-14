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
        running=true;
        fileList.removeAll(fileList);
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

    public List<ImgBean> searchFolder(String path) {
        List<ImgBean> imgBeans = new ArrayList<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String childPath = file.getAbsolutePath();
                for (String anExtension : extension) {
                    if (childPath.endsWith(anExtension)) {
                        imgBeans.add(new ImgBean(null, childPath, true));
                    }
                }
            }
        }
        return imgBeans;
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

        public ImgBean(String path, String firstImgPath, boolean isImg) {
            this.path = path;
            this.firstImgPath = firstImgPath;
            this.isImg = isImg;
        }

        private boolean open;
        private int pos;
        private boolean isImg;
        private String path;
        private String firstImgPath;

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFirstImgPath() {
            return firstImgPath;
        }

        public boolean isImg() {
            return isImg;
        }

        public void setImg(boolean img) {
            isImg = img;
        }

        public void setFirstImgPath(String firstImgPath) {

            this.firstImgPath = firstImgPath;
        }
    }

    public boolean isRunning(){
        return isRunning();
    }
}
