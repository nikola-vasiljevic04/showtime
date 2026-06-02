package rs.edu.raf.rma.presentation.catalog.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.rma.data.repository.CatalogRepositoryImpl
import rs.edu.raf.rma.domain.repository.CatalogRepository
import rs.edu.raf.rma.networking.di.Qualifiers
import rs.edu.raf.rma.presentation.catalog.CatalogViewModel
import rs.edu.raf.rma.presentation.details.MovieDetailsViewModel
import rs.edu.raf.rma.presentation.favorites.FavoritesViewModel
import rs.edu.raf.rma.presentation.filters.FiltersViewModel
import rs.edu.raf.rma.presentation.watchlist.WatchlistViewModel

val catalogFeatureModule = module {

    single<CatalogRepository> {
        CatalogRepositoryImpl(
            api = get(Qualifiers.Authenticated),
            dao = get()
        )
    }
    viewModelOf(::CatalogViewModel)
    viewModelOf(::FiltersViewModel)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::WatchlistViewModel)
//    viewModel {
//        CatalogViewModel(
//            repository = get<CatalogRepository>(),
//            filterManager = get()
//        )
//    }
//
//    viewModel {
//        FiltersViewModel(
//            repository = get<CatalogRepository>(),
//            filterManager = get()
//        )
//    }
//    viewModel { MovieDetailsViewModel(savedStateHandle = get(), repository = get()) }
//    viewModel { FavoritesViewModel(repository = get()) }
//    viewModel { WatchlistViewModel(repository = get()) }
}