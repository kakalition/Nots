package com.daggery.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daggery.data.entities.NoteDataEntity
import com.daggery.data.entities.NoteTagEntity

@Database(entities = [NoteDataEntity::class, NoteTagEntity::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class NotsDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var INSTANCE: NotsDatabase? = null
        fun getDatabase(context: Context): NotsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    NotsDatabase::class.java,
                    "nots"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}