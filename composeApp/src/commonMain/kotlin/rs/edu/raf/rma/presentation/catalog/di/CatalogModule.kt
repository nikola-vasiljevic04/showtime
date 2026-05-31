package rs.edu.raf.rma.presentation.catalog.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import rs.edu.raf.rma.data.repository.CatalogRepositoryImpl
import rs.edu.raf.rma.domain.repository.CatalogRepository
import rs.edu.raf.rma.networking.di.Qualifiers
import rs.edu.raf.rma.presentation.catalog.CatalogViewModel
import rs.edu.raf.rma.presentation.details.MovieDetailsViewModel
import rs.edu.raf.rma.presentation.filters.FiltersViewModel

val catalogFeatureModule = module {

    // Obavezno naglašavamo tip: single<CatalogRepository>
    single<CatalogRepository> {
        CatalogRepositoryImpl(
            api = get(Qualifiers.Authenticated),
            dao = get()
        )
    }

    viewModel {
        CatalogViewModel(
            repository = get<CatalogRepository>(),
            filterManager = get()
        )
    }

    viewModel {
        FiltersViewModel(
            repository = get<CatalogRepository>(),
            filterManager = get()
        )
    }
    viewModel { MovieDetailsViewModel(savedStateHandle = get(), repository = get()) }
}