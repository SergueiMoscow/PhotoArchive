package com.bytza.photoarchive.model.photo

import com.bytza.photoarchive.model.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface PhotoService {
    @GET("photoarchive/api/getall.php")
    fun getAll(): Call<List<PhotoRemote>>

    @POST("photoarchive/api/login.php")
    fun getSession(): Call<LoginResponse>
}