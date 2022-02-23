package com.daggery.domain.usecases.tag

import com.daggery.domain.entities.NoteTag
import com.daggery.domain.repositories.TagsRepository

interface BaseDeleteTagsUseCase {
    suspend operator fun invoke(tagList: List<NoteTag>)
}