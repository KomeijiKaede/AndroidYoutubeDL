package net.teamfruit.androidyoutubedl.experimental.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import java.util.*

@Database(entities = arrayOf(DBEntity::class), version = 1)
abstract class Base: RoomDatabase() {
    abstract fun dbdao(): DataAccessObjects
    companion object {
        fun create(title: String, videoID: String): DBEntity { return DBEntity(Random().nextInt(), title, videoID) }
        private var INSTANCE: Base? = null
        fun getDataBase(context: Context): Base {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, Base::class.java, "info.db").allowMainThreadQueries().build()
            }
            return INSTANCE as Base
        }
    }
}