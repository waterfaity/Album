package com.waterfairy.http.client;

import com.waterfairy.http.interceptor.RequestInterceptor;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by water_fairy on 2017/2/22.
 */

public class RetrofitHttpClient {

    private Retrofit retrofit;
    private RequestInterceptor requestInterceptor;

    private RetrofitHttpClient(String baseUrl, boolean hasGson) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                .addInterceptor(requestInterceptor)
                .build();
        Retrofit.Builder client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient);
        if (hasGson) {
            client.addConverterFactory(GsonConverterFactory.create());
        }
        retrofit = client.build();
    }

    private RetrofitHttpClient(String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                .addInterceptor(requestInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    public static RetrofitHttpClient build(String baseUrl) {
        return new RetrofitHttpClient(baseUrl);
    }

    public static RetrofitHttpClient build(String baseUrl, boolean hasGson) {
        return new RetrofitHttpClient(baseUrl, hasGson);
    }

    /**
     * 设置公公共参数
     *
     * @param popParams
     */
    public void setPopParams(HashMap<String, String> popParams) {
        requestInterceptor.setPopParams(popParams);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
