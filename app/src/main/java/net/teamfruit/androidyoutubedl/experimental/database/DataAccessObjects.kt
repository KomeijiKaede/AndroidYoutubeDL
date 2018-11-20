package net.teamfruit.androidyoutubedl.experimental.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface DataAccessObjects {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(info: DBEntity)
    @Delete
    fun delete(info: DBEntity)
    @Query("SELECT * FROM DBEntity")
    fun findAll(): List<DBEntity>
    @Query("SELECT * FROM DBEntity")
    fun livedataAll(): LiveData<List<DBEntity>>
}