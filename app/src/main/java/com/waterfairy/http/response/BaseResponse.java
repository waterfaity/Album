package com.waterfairy.http.response;

import java.util.ArrayList;

/**
 * Created by water_fairy on 2017/5/19.
 * 995637517@qq.com
 */

public class BaseResponse<T> {
    int code;
    String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
