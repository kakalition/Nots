package com.daggery.data.di

import android.content.Context
import com.daggery.data.db.NoteDao
import com.daggery.data.db.TagDao
import com.daggery.data.db.NotsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext appContext: Context): NotsDatabase {
        return NotsDatabase.getDatabase(appContext)
    }

    @Provides
    fun providesNoteDao(notsDatabase: NotsDatabase): NoteDao {
        return notsDatabase.noteDao()
    }

    @Provides
    fun providesTagDao(notsDatabase: NotsDatabase): TagDao {
        return notsDatabase.tagDao()
    }
}