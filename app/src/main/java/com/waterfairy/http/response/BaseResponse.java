package com.waterfairy.http.response;

import java.util.ArrayList;

/**
 * Created by water_fairy on 2017/5/19.
 * 995637517@qq.com
 */

public class BaseResponse<T> {
    private String resultCode;//返回码
    private T record;//书本详情/用户信息
    private String message;//返回消息
    private ArrayList<T> list;//其他 list
    private ArrayList<T> classList;//班级信息列表
    private ArrayList<T> studentInfos;//学生信息列表
    private ArrayList<T> result;//查询教室带班级 绘本圈的筛选
    private int pageSize;
    private int totalPage;
    private int totalRow;
    private int pageNumber;
    private String filePath;
    private String fileName;
    private int isCollect;//书本收藏
    private int score;//老师查询学生作品得分
    //七牛 相关
    private String accessToken;//七牛token
    private String domainUrl;//链接拼接头部
    //勋章
    private String zipUrl;

    public String getZipUrl() {
        return zipUrl;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<T> getClassList() {
        return classList;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public ArrayList<T> getStudentInfos() {
        return studentInfos;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public ArrayList<T> getList() {
        return list;
    }

    public T getRecord() {
        return record;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<T> getResult() {
        return result;
    }

    public boolean isCollect() {
        if (isCollect == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getDomainUrl() {
        return domainUrl;
    }
}
