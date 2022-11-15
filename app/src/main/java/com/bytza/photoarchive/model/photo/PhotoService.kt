package com.bytza.photoarchive.model.photo

import retrofit2.Call
import retrofit2.http.GET

interface PhotoService {
    @GET("/getall.php")
    fun getAll(): Call<List<PhotoRemote>>
}