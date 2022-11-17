package com.bytza.photoarchive.model.photo

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import com.bytza.photoarchive.model.DbConnection
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PhotosLocalRepository(var dataBase: DbConnection) {
    var photosLocal: LiveData<List<PhotosLocal>>? = null
    var remoteIdList: LiveData<List<Int>>? = null
    val pathForImages = "/images"
    val LOG_DEBUG = "LOG_DEBUG"

    fun getAll() {//: LiveData<List<PhotosLocal>> {
        photosLocal = dataBase.getDao().getAll()
        //return photosLocal
    }

    fun remoteIdExists(id: Int): Int {
        var count: Int? = 0
        try {
            count = dataBase.getDao().remoteIdExists(id)
            print("")
        } catch(e: java.lang.Exception) {
            print(e)
        }
        return if (count == null) 0 else count
    }
    fun getRemoteIds() {
        remoteIdList = dataBase.getDao().getRemoteIds()
    }

    fun deleteByRemoteId(photo: PhotoRemote) {
        val photoLocalFileName = dataBase.dataDir + pathForImages + "/" + justFileName(photo.fname)
        GlobalScope.launch {
            deleteFile(photoLocalFileName)
            dataBase.getDao().deleteByRemoteId(photo.id)
            Log.d(LOG_DEBUG, "Record deleted")
        }.start()

    }

    fun insert(photoLocal: PhotosLocal) {
        GlobalScope.launch {
            Log.d(LOG_DEBUG, "Insert begin")
            val photoRemoteURL = photoLocal.fname
            val photoLocalFileName = dataBase.dataDir + pathForImages + "/" + justFileName(photoLocal.fname)
            saveImage(Picasso.get().load(photoRemoteURL).get(), photoLocalFileName)
            photoLocal.fname = photoLocalFileName
            dataBase.getDao().insertPhoto(photoLocal)
            Log.d(LOG_DEBUG, "Insert end (${photoLocal.fname})")
        }.start()
    }

    suspend fun delete(PhotosLocal: PhotosLocal) {
        dataBase.getDao().delete(PhotosLocal)
    }

    fun justFileName(path: String) :String = path.substring(path.lastIndexOf("/")+1)
    fun justPath(fullPath: String) :String = fullPath.substring(0,fullPath.lastIndexOf("/"))

    fun saveImage(bitmap: Bitmap, fileName: String){
        //val file = File(dataBase.dataDir) //Environment.getDataDirectory()
        //val dir = File(file.absolutePath + pathForImages)
        //val outfile = File(dir, filename)
        val justPath = justPath(fileName)
        val outDir=File(justPath)
        outDir.mkdirs()
        val outFile = File(fileName)
        var outputstream: FileOutputStream? = null
        try {
            outputstream = FileOutputStream(outFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputstream)
            outputstream.flush()
            outputstream.close()
            Log.d(LOG_DEBUG, "Image copy Ok")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(LOG_DEBUG, "Image copy error")
        }
    }

    fun deleteFile(fileName: String) {
        val file = File(fileName)
        if (file.exists()) file.delete()
        Log.d(LOG_DEBUG, "Image deleted")
    }

    fun getImageFileName(fileName: String) :String {
        val file = dataBase.dataDir?.let { File(it) } //Environment.getDataDirectory()
        val dir = File(file?.absolutePath + pathForImages)
        val outfile = File(dir, fileName)
        return ""
    }

    fun getLocalFileNameByRemoteId(id: Int): String {
        return dataBase.getDao().getLocalFileNameByRemoteId(id)
    }
}

