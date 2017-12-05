package com.waterfairy.album.http;

import com.waterfairy.http.response.BaseResponse;

import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/11/29
 * @Description:
 */

public interface RetrofitService {
    @GET("login")
    Call<BaseResponse> login(@Query("userName") String userName,
                             @Query("userPwd") String passWord);


    @GET("regist")
    Call<BaseResponse> regist(@Query("userName") String userName,
                              @Query("userPwd") String passWord,
                              @Query("tel") String tel);
}
