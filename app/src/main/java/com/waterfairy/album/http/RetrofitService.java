package com.waterfairy.album.http;

import com.waterfairy.http.response.BaseResponse;

import java.security.PublicKey;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/11/29
 * @Description:
 */

public interface RetrofitService {
    @POST("login")
    @FormUrlEncoded
    Call<BaseResponse> login(
            @Field("userName") String userName,
            @Field("password") String password);
}
