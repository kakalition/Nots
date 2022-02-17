package com.daggery.nots.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "note_order") val noteOrder: Int,
    @ColumnInfo(name = "note_title") val noteTitle: String,
    @ColumnInfo(name = "note_body") val noteBody: String,
    @ColumnInfo(name = "note_date") val noteDate: Long,
    @ColumnInfo(name = "note_tags") val noteTags: List<String>,
)