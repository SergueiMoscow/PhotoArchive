package com.bytza.photoarchive.model.photo

import com.bytza.photoarchive.model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface PhotoService {
    @GET("photoarchive/api/getall.php")
    fun getAll(): Call<List<PhotoRemote>>

    @POST("photoarchive/api/login.php")
    fun getSession(): Call<LoginResponse>

    @GET("photoarchive/api/login.php")
    fun login(@Query("u") u:String, @Query("p") p:String): Call<LoginResponse>

    @FormUrlEncoded @POST("photoarchive/api/update.php")
    fun updatePhoto(
        @Field("id") id: Int,
        @Field("descript") descript: String
    ): Call<ResponseBody>

    @Multipart @POST("photoarchive/api/update.php")
    fun insertPhoto(
        @Part("descript") descript: String,
        @Part file: MultipartBody.Part
    ) : Call<ResponseBody>
}