package com.bytza.photoarchive.model.photo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhotosLocalDao {
    @Query("Select * from photos")
    fun getAll(): LiveData<List<PhotosLocal>>

    //@Query("select * from photos where id = :id")
    //suspend fun getById(id: Int)

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg PhotosLocal: PhotosLocal)

    @Delete
    suspend fun delete(PhotosLocal: PhotosLocal)

}