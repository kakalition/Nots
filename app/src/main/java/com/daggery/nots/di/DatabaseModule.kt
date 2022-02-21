package com.daggery.nots.di

import android.content.Context
import com.daggery.nots.data.NoteDao
import com.daggery.nots.data.NotsDatabase
import com.daggery.nots.data.NoteTagDao
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
    fun providesNotsDatabase(@ApplicationContext appContext: Context): NotsDatabase {
        return NotsDatabase.getDatabase(appContext)
    }

    @Provides
    fun providesNoteDao(notsDatabase: NotsDatabase): NoteDao {
        return notsDatabase.noteDao()
    }

    @Provides
    fun provideTagDao(notsDatabase: NotsDatabase): NoteTagDao {
        return notsDatabase.tagDao()
    }
}