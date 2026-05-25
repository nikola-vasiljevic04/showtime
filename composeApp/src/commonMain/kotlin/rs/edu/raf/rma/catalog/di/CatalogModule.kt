package rs.edu.raf.rma.catalog.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import rs.edu.raf.rma.catalog.CatalogRepository
import rs.edu.raf.rma.catalog.CatalogViewModel
import rs.edu.raf.rma.networking.di.Qualifiers

val catalogFeatureModule = module {
    single { CatalogRepository(api = get(Qualifiers.Unauthenticated), dao = get()) }
    viewModel { CatalogViewModel(repository = get()) }
}