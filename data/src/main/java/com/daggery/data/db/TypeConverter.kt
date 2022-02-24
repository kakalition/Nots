package com.daggery.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class TypeConverter {
    @TypeConverter
    fun tagsFromList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun tagsToList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}