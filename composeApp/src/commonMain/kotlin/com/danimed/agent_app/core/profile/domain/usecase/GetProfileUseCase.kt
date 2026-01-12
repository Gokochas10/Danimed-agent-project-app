package com.danimed.agent_app.core.profile.domain.usecase

import com.danimed.agent_app.core.profile.domain.model.Profile
import com.danimed.agent_app.core.profile.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<Profile> {
        return profileRepository.getProfile()
    }
}




