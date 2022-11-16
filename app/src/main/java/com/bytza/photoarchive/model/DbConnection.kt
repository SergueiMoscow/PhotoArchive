package com.bytza.photoarchive.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import android.content.Context
import com.bytza.photoarchive.model.photo.PhotosLocal
import com.bytza.photoarchive.model.photo.PhotosLocalDao


@Database(entities = [PhotosLocal::class], version = 2, exportSchema = true)
abstract class DbConnection : RoomDatabase() {
    abstract fun getDao(): PhotosLocalDao
    var dataDir: String? = null

    companion object {
        private var INSTANCE: DbConnection? = null
        fun getDatabase(context: Context): DbConnection {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, DbConnection::class.java, "db")
                            .build()
                }
            }
            return INSTANCE!!
        }
        fun getDatabase() : DbConnection {
            return INSTANCE!!
        }
    }
}