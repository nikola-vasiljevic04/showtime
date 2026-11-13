# Showtime 🎬

A cross-platform movie app built with **Kotlin Multiplatform** and **Compose Multiplatform**. You can browse a movie catalog, save favorites and a watchlist, and test your movie knowledge in a timed quiz with a global leaderboard.

> Built as the course project for *Mobile Application Development* (RMA) at the School of Computing (RAF), Union University.

## Features

- **Authentication**: sign up and log in against a REST API. The access token is stored with DataStore.
- **Movie catalog**: paginated list with search, filters (genre, minimum rating, year range) and sorting.
- **Movie details**: overview, cast, image gallery, trailers, production companies and collections.
- **Favorites & watchlist**: synced with the backend. Updates are optimistic: the change shows immediately and is rolled back if the API call fails.
- **Movie quiz**: 10 questions generated from cached movie data (guess the movie, the release year or the actor), with a 60-second timer.
- **Leaderboard & profile**: results are submitted to a global leaderboard, and personal stats are kept locally.
- **Offline-friendly**: movies, genres and details are cached in a local Room database.

## Tech stack

| Area | Libraries |
|---|---|
| UI | Compose Multiplatform, Material 3, Navigation |
| Architecture | MVI-style ViewModels (UiState / UiEvent / SideEffect), repository pattern, Kotlin Coroutines & Flow |
| Networking | Ktor client, Ktorfit, kotlinx.serialization |
| Local storage | Room (SQLite), DataStore |
| Dependency injection | Koin |
| Images | Coil 3 |
| Logging | Napier |

## Project structure

```
composeApp/        Shared code (commonMain) + platform-specific source sets
  ├─ auth/         Login & signup
  ├─ core/         Room database, DAOs, auth storage
  ├─ data/         Repository implementations
  ├─ domain/       Models & repository interfaces
  ├─ networking/   API definition, HTTP client, DTOs
  └─ presentation/ Screens: catalog, details, filters, favorites, watchlist, quiz, profile
app/android/       Android entry point
app/desktop/       Desktop (JVM) entry point
app/ios/           iOS entry point
```

## Running the app

**Android**
```bash
./gradlew :app:android:installDebug
```
Or run the `app.android` configuration from Android Studio.

**Desktop (JVM)**
```bash
./gradlew :app:desktop:run
```

**iOS**: open `app/ios` in Xcode and run it.

## What I learned

- Sharing business logic and UI across Android, iOS and Desktop with Kotlin Multiplatform
- Consuming a REST API with authentication, pagination and query parameters
- Combining a remote API with a local database cache
- Unidirectional data flow (MVI) with StateFlow and Compose
