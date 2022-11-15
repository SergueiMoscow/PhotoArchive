package com.bytza.photoarchive.model.photo


data class PhotoRemote(
    val id: Int,
    val fname: String,
    val fsize: Int,
    val fcreated: String,
    var descript: String,
    var tags: String
)
