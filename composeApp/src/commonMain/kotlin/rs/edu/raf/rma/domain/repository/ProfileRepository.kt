package rs.edu.raf.rma.domain.repository

import rs.edu.raf.rma.networking.model.UserDto

interface ProfileRepository {
    suspend fun getProfile(): UserDto
}