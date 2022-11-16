package com.bytza.photoarchive.model.photo

import androidx.lifecycle.MutableLiveData
import com.bytza.photoarchive.model.DbConnection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoRemoteRepository(private val photoService: PhotoService) {
    private var photos: MutableLiveData<List<PhotoRemote>?> = MutableLiveData()

    fun getAll(): MutableLiveData<List<PhotoRemote>?> {
        val db = DbConnection.getDatabase()
        val localRepository = PhotosLocalRepository(db)
        photoService.getAll().enqueue(object : Callback<List<PhotoRemote>> {
            override fun onResponse(call: Call<List<PhotoRemote>>, response: Response<List<PhotoRemote>>) {
                val result = response.body()
                if (result != null){
                    GlobalScope.launch {
                        result.forEach {
                            var local: Int? = localRepository.remoteIdExists(it.id)
                            it.local = if (local == null) 0 else local
                        }
                    }
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