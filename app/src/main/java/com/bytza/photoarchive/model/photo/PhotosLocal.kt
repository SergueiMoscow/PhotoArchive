package com.bytza.photoarchive.model.photo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotosLocal(
    @PrimaryKey val id: Int,
    val remoteId: Int,
    val fname: String,
    val fsize: Int,
    val fcreated: String,
    var descript: String,
    var tags: String
)
