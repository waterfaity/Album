package com.waterfairy.album.http;

import com.waterfairy.album.bean.UserBean;
import com.waterfairy.http.response.BaseResponse;

import java.security.PublicKey;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/11/29
 * @Description:
 */

public interface RetrofitService {
    @GET("login")
    Call<BaseResponse<UserBean>> login(@Query("userName") String userName,
                                       @Query("userPwd") String passWord);


    @GET("regist")
    Call<BaseResponse> regist(@Query("userName") String userName,
                              @Query("userPwd") String passWord,
                              @Query("tel") String tel);

    void queryPhoto(long userId);

    @Multipart
    @POST("file/uploads/{id}")
    Call<BaseResponse> upload(@Path("id") long id,
                              @Query("address") String address,
                              @Part("files") MultipartBody.Part file);
}
