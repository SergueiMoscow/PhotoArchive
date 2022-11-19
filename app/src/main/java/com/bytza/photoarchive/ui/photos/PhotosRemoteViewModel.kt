package com.bytza.photoarchive.ui.photos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bytza.photoarchive.model.photo.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotosRemoteViewModel(application: Application) : AndroidViewModel(application) {
    private val photoService: PhotoService
    var currentEditItem: PhotoRemote? = null
    var currentEditPosition: Int? = null

//  Базу пока переносим во fragment
//    private val db: RoomDatabase
//    private lateinit var localRepository: PhotosLocalRepository
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sushkovs.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        photoService = retrofit.create(PhotoService::class.java)
//        db = Room.databaseBuilder(getApplication<Application>().applicationContext, DbConnection::class.java, "db").build()
//        localRepository = PhotosLocalRepository(db)
    }

    private val photosRepository = PhotoRemoteRepository(photoService)
    var instance: String = "Nothing"

    var photos: MutableLiveData<List<PhotoRemote>?> = photosRepository.getAll()


}