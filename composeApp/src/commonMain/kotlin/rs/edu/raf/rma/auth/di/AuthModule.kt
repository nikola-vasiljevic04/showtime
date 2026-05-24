package rs.edu.raf.rma.auth.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import rs.edu.raf.rma.auth.AuthRepository
import rs.edu.raf.rma.auth.AuthViewModel

val authModule = module {
    single { AuthRepository(get(), get()) }

    viewModel { AuthViewModel(get()) }
}