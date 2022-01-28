package com.daggery.nots.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NotsDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotsDatabase? = null
        fun getDatabase(context: Context): NotsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    NotsDatabase::class.java,
                    "notes"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}