package net.teamfruit.androidyoutubedl.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable

class DBOpenHelper(context: Context):ManagedSQLiteOpenHelper(context,"MusicList",null,1) {
    companion object {
        const val tableName = "list"
        private var instance :DBOpenHelper? = null

        fun newInstance(context: Context):DBOpenHelper{return instance ?: DBOpenHelper(context.applicationContext)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.run { createTable(tableName, ifNotExists = true, columns = *arrayOf("title" to TEXT, "url" to TEXT, "originURL" to TEXT))}
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}