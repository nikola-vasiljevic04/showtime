package rs.edu.raf.rma.data.repository

import rs.edu.raf.rma.domain.repository.ProfileRepository
import rs.edu.raf.rma.networking.ShowtimeApi
import rs.edu.raf.rma.networking.model.UserDto

class ProfileRepositoryImpl(
    private val api: ShowtimeApi
) : ProfileRepository {
    override suspend fun getProfile(): UserDto {
        return api.getProfile()
    }
}