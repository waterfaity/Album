package com.waterfairy.album.http;

import com.waterfairy.album.bean.ImageBean;
import com.waterfairy.album.bean.UserBean;
import com.waterfairy.http.response.BaseResponse;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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


    @Multipart
    @POST("file/uploads/{id}")
    Call<BaseResponse> upload(@Path("id") long id,
                              @Query("address") String address,
                              @Part MultipartBody.Part file);

    @Multipart
    @POST("file/uploads/{id}")
    Call<BaseResponse> uploadMul(@Path("id") long id,
                                 @Query("address") String address,
                                 @Part List<MultipartBody.Part> file);

    @GET("get/photolist")
    Call<BaseResponse<List<ImageBean>>> queryImgList(@Query("userid") long userid);

    @GET("photo/delete")
    Call<BaseResponse> deleteImg(@Query("id") long id);

    @GET("get/userList")
    Call<BaseResponse<List<UserBean>>> queryUserAccount();

    @GET("user/delete")
    Call<BaseResponse> deleteAccount(@Query("id") long userId);
}
