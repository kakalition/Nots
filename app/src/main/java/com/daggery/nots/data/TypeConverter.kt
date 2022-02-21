package com.daggery.nots.data

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TypeConverter {
    @TypeConverter
    fun tagsFromList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun tagsToList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}