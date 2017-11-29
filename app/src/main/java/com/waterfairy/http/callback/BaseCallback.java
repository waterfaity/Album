package com.waterfairy.http.callback;

import android.text.TextUtils;
import android.util.Log;

import com.waterfairy.http.exception.HttpExceptionUtils;
import com.waterfairy.http.response.BaseResponse;
import com.waterfairy.utils.GsonUtils;
import com.waterfairy.utils.ToastUtils;

import retrofit2.Call;

/**
 * Created by shui on 2017/4/9.
 */

public abstract class BaseCallback<T> implements retrofit2.Callback<T> {
    private final String TAG = "baseCallback";
    private boolean showToa = true;

    public BaseCallback(boolean showToa) {
        this.showToa = showToa;
    }

    public BaseCallback() {
    }

    public abstract void onSuccess(T t);

    public abstract void onFailed(int code, String message);

    @Override
    public void onResponse(Call<T> call, retrofit2.Response<T> response) {
        handleData(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        HttpExceptionUtils.ResponseThrowable responseThrowable = HttpExceptionUtils.handleException(throwable);
        if (showToa) ToastUtils.show(responseThrowable.message);
        onFailed(responseThrowable.code, responseThrowable.message);
        Log.i(TAG, "error: " + responseThrowable.code + " -- " + responseThrowable.message);
    }

    private void handleData(Call<T> call, retrofit2.Response<T> response) {
        int code = response.code();//200;  400;401
        String showMessage = null;
        if (code == 200) {
            T body = response.body();
            Log.i(TAG, "handleData: " + GsonUtils.objectToJson(body));
            BaseResponse baseResponse = null;
            if (body instanceof BaseResponse) {
                baseResponse = (BaseResponse) body;
            }
            if (baseResponse != null) {
                switch (baseResponse.getResultCode()) {
                    case HttpResultCode.HTTP_RESULT_OK:
                        onSuccess(response.body());
                        break;
                    case HttpResultCode.HTTP_RESULT_NO_DATA:
                        String message = baseResponse.getMessage();
                        showMessage = TextUtils.isEmpty(message) ? "没有数据" : message;
                        onFailed(Integer.parseInt(HttpResultCode.HTTP_RESULT_NO_DATA), showMessage);
                        break;
                    case HttpResultCode.HTTP_RESULT_ERROR:
                        showMessage = baseResponse.getMessage();
                        onFailed(Integer.parseInt(HttpResultCode.HTTP_RESULT_ERROR), showMessage);
                        break;
                }
            } else {
                onSuccess(response.body());
            }


        } else {
            Log.i(TAG, "handleData: " + code);
            showMessage = "没有获取到数据";
            onFailed(code, showMessage);
        }
        if (showToa && !TextUtils.isEmpty(showMessage)) {
            ToastUtils.show(showMessage);
        }
    }
}
