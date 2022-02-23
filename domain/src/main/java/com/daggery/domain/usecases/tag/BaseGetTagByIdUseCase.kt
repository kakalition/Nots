package com.daggery.domain.usecases.tag

import com.daggery.domain.repositories.TagsRepository

interface BaseGetTagByIdUseCase {
    suspend operator fun invoke(id: Int)
}