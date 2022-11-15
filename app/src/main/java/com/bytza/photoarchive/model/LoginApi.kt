package com.bytza.photoarchive.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LoginApi {
        @GET("/login.php")
        fun login(@Query("u") u:String, @Query("p") p:String): Call<String>
}