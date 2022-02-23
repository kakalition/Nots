package com.daggery.data.di

import com.daggery.data.repositories.notes.NotesRepositoryImpl
import com.daggery.data.repositories.tags.TagsRepositoryImpl
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindsNotesRepository(notesRepositoryImpl: NotesRepositoryImpl): NotesRepository

    @Binds
    abstract fun bindsTagsRepository(tagsRepositoryImpl: TagsRepositoryImpl): TagsRepository
}