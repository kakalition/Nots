package com.daggery.data.di

import com.daggery.data.repositories.notes.NotesRepositoryImpl
import com.daggery.data.repositories.tags.TagsRepositoryImpl
import com.daggery.domain.repositories.NotesRepository
import com.daggery.domain.repositories.TagsRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    abstract fun bindsNotesRepository(notesRepositoryImpl: NotesRepositoryImpl): NotesRepository

    abstract fun bindsTagsRepository(tagsRepositoryImpl: TagsRepositoryImpl): TagsRepository
}