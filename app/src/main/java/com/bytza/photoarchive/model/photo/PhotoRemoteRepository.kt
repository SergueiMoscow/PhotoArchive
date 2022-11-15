package com.bytza.photoarchive.model.photo

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoRemoteRepository(private val photoService: PhotoService) {
    private var photos: MutableLiveData<List<PhotoRemote>?> = MutableLiveData()

    fun getAll(): MutableLiveData<List<PhotoRemote>?> {
        photoService.getAll().enqueue(object : Callback<List<PhotoRemote>> {
            override fun onResponse(call: Call<List<PhotoRemote>>, response: Response<List<PhotoRemote>>) {
                val result = response.body()
                if (result != null){
                    photos.value = result
                }
            }
            override fun onFailure(call: Call<List<PhotoRemote>>, t: Throwable) {
                var err=t
            }
        })

        return photos
    }

}