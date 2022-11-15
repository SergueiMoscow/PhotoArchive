package com.bytza.photoarchive.model.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.RoomDatabase
import com.bytza.photoarchive.model.DbConnection

class PhotosLocalRepository(var dataBase: DbConnection) {
    var photosLocal: LiveData<List<PhotosLocal>> = dataBase.photosLocalDao().getAll()

    fun getAll(): LiveData<List<PhotosLocal>> {
        return photosLocal
    }

    suspend fun insert(PhotosLocal: PhotosLocal) {
        dataBase.photosLocalDao().insert(PhotosLocal)
    }

    suspend fun delete(PhotosLocal: PhotosLocal) {
        dataBase.photosLocalDao().delete(PhotosLocal)
    }
}