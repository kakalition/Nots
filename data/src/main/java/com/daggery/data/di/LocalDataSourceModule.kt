package com.daggery.data.di

import com.daggery.data.repositories.notes.NotesLocalDataSource
import com.daggery.data.repositories.notes.NotesLocalDataSourceImpl
import com.daggery.data.repositories.tags.TagsLocalDataSource
import com.daggery.data.repositories.tags.TagsLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindsNotesLocalDataSource(notesLocalDataSourceImpl: NotesLocalDataSourceImpl)
        : NotesLocalDataSource

    @Binds
    abstract fun bindsTagsLocalDataSource(tagsLocalDataSourceImpl: TagsLocalDataSourceImpl)
        : TagsLocalDataSource
}