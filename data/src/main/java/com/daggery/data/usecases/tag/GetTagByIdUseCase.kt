package com.daggery.domain.usecases.tag

import com.daggery.domain.repositories.TagsRepository

class GetTagByIdUseCase(private val tagsRepository: TagsRepository) : BaseGetTagByIdUseCase {
    override suspend operator fun invoke(id: Int) = tagsRepository.getTagById(id)
}