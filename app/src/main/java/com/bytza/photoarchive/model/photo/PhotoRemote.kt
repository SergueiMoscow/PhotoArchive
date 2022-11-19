package com.bytza.photoarchive.model.photo

import android.text.Editable


data class PhotoRemote(
    val id: Int,
    var local: Int,
    val fname: String,
    val fsize: Int,
    val fcreated: String,
    var descript: String,
    var tags: String
)
