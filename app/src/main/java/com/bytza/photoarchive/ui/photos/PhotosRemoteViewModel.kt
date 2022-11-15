package com.bytza.photoarchive.ui.photos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bytza.photoarchive.model.photo.PhotoRemote
import com.bytza.photoarchive.model.photo.PhotoRemoteRepository
import com.bytza.photoarchive.model.photo.PhotoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotosRemoteViewModel : ViewModel() {

    private val photoService: PhotoService
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sushkovs.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        photoService = retrofit.create(PhotoService::class.java)
    }

    private val photosRepository = PhotoRemoteRepository(photoService)

    var photos: MutableLiveData<List<PhotoRemote>?> = photosRepository.getAll()
}