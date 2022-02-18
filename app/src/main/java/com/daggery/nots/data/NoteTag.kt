package com.daggery.nots.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "tags")
data class NoteTag(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tag_name") val tagName: String,
    @ColumnInfo(name = "checked") val checked: Boolean
)