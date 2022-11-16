package com.bytza.photoarchive.model.photo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotosLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name="remoteid")
    val remoteId: Int,
    var fname: String,
    val fsize: Int,
    val fcreated: String,
    var descript: String,
    var tags: String
)
