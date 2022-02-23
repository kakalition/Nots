package com.daggery.data.di

import com.daggery.data.repositories.notes.NotesLocalDataSource
import com.daggery.data.repositories.notes.NotesLocalDataSourceImpl
import com.daggery.data.repositories.tags.TagsLocalDataSource
import com.daggery.data.repositories.tags.TagsLocalDataSourceImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocalDataSourceModule {

    abstract fun bindsNotesLocalDataSource(notesLocalDataSourceImpl: NotesLocalDataSourceImpl)
        : NotesLocalDataSource

    abstract fun bindsTagsLocalDataSource(tagsLocalDataSourceImpl: TagsLocalDataSourceImpl)
        : TagsLocalDataSource
}