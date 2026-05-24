package rs.edu.raf.rma.auth

import rs.edu.raf.rma.core.auth.AuthStore
import rs.edu.raf.rma.core.auth.model.AuthData
import rs.edu.raf.rma.networking.ShowtimeApi
import rs.edu.raf.rma.networking.model.AuthRequest

class AuthRepository(
    private val api: ShowtimeApi,
    private val authStore: AuthStore
) {
    suspend fun login(username: String, pass: String) {
        val response = api.login(AuthRequest(username = username, password = pass))
        saveToken(response.accessToken)
    }

    suspend fun signup(fullName: String, username: String, pass: String) {
        val response = api.signup(AuthRequest(fullName = fullName, username = username, password = pass))
        saveToken(response.accessToken)
    }

    private suspend fun saveToken(token: String) {
        authStore.setAuthData(AuthData(accessToken = token, refreshToken = ""))
    }

    suspend fun logout() {
        authStore.clearAuthData()
        // Kasnije ćemo ovde dodati i brisanje lokalnih Room tabela (Favorites, Watchlist)
    }
}