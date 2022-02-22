package com.daggery.domain.usecases

import com.daggery.domain.repositories.TagsRepository

class GetTagByIdUseCase(private val tagsRepository: TagsRepository) {
    suspend operator fun invoke(id: Int) = tagsRepository.getTagById(id)
}