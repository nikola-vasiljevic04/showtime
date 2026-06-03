package rs.edu.raf.rma.networking.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.SetupRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.dsl.module
import rs.edu.raf.rma.core.auth.AuthStore
import rs.edu.raf.rma.core.auth.model.AuthState
import rs.edu.raf.rma.core.db.dao.ShowtimeDao
import rs.edu.raf.rma.networking.HttpClientFactory
import rs.edu.raf.rma.networking.ShowtimeApi
import rs.edu.raf.rma.networking.createShowtimeApi

val networkingModule = module {

    single<HttpClient>(Qualifiers.Unauthenticated) {
        HttpClientFactory.createHttpClientWithDefaultConfig()
    }

    single<HttpClient>(Qualifiers.Authenticated) {
        val authStoreLazy: Lazy<AuthStore> = inject()
        val showtimeDaoLazy: Lazy<ShowtimeDao> = inject()
        HttpClientFactory.createHttpClientWithDefaultConfig {
            installAuthPlugin(authStoreLazy,showtimeDaoLazy)
        }
    }
    single<ShowtimeApi>(Qualifiers.Unauthenticated) {
        Ktorfit.Builder()
            .httpClient(get<HttpClient>(Qualifiers.Unauthenticated))
            .baseUrl("https://rma.finlab.rs/")
            .build()
            .createShowtimeApi()
    }

    single<ShowtimeApi>(Qualifiers.Authenticated) {
        Ktorfit.Builder()
            .httpClient(get<HttpClient>(Qualifiers.Authenticated))
            .baseUrl("https://rma.finlab.rs/")
            .build()
            .createShowtimeApi()
    }
}

private fun HttpClientConfig<*>.installAuthPlugin(
    authStoreLazy: Lazy<AuthStore>,
    showtimeDaoLazy: Lazy<ShowtimeDao>

) = install(createClientPlugin("AuthPlugin") {

    on(SetupRequest) { request ->
        val authStore = authStoreLazy.value
        when (val authState = authStore.authState.value) {
            is AuthState.Authenticated -> {
                request.header(
                    key = HttpHeaders.Authorization,
                    value = "Bearer ${authState.data.accessToken}",
                )
            }
            AuthState.Unauthenticated -> Unit
        }
    }

    on(Send) { request ->
        val originalCall = proceed(request)

        originalCall.response.run {
            if (status != HttpStatusCode.Unauthorized) {
                return@run originalCall
            }

            val authStore = authStoreLazy.value
            val showtimeDao = showtimeDaoLazy.value

            CoroutineScope(Dispatchers.Default).launch {
                authStore.clearAuthData()
                showtimeDao.clearFavorites()
                showtimeDao.clearWatchlist()
            }

            originalCall
        }
    }
})