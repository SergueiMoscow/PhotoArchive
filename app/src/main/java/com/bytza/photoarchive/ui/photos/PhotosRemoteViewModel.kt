package com.bytza.photoarchive.ui.photos

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bytza.photoarchive.model.DbConnection
import com.bytza.photoarchive.model.photo.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotosRemoteViewModel(application: Application) : AndroidViewModel(application) {
    private val photoService: PhotoService

//  Базу пока переносим во fragment
//    private val db: RoomDatabase
//    private lateinit var localRepository: PhotosLocalRepository
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sushkovs.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        photoService = retrofit.create(PhotoService::class.java)
//        db = Room.databaseBuilder(getApplication<Application>().applicationContext, DbConnection::class.java, "db").build()
//        localRepository = PhotosLocalRepository(db)
    }

    private val photosRepository = PhotoRemoteRepository(photoService)

    var photos: MutableLiveData<List<PhotoRemote>?> = photosRepository.getAll()
}