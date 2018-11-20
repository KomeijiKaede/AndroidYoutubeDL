package net.teamfruit.androidyoutubedl.experimental.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class DBEntity constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        @ColumnInfo(name = "title")
        var title: String,
        @ColumnInfo(name = "videoID")
        var videoID: String
)