package rs.edu.raf.rma.auth.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import rs.edu.raf.rma.auth.AuthRepository
import rs.edu.raf.rma.auth.AuthViewModel
import rs.edu.raf.rma.networking.di.Qualifiers

val authFeatureModule = module {
    single { AuthRepository(get(Qualifiers.Unauthenticated), get(),get()) }
    viewModel { AuthViewModel(get()) }
}