package rs.edu.raf.rma.presentation.profile.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.rma.data.repository.ProfileRepositoryImpl
import rs.edu.raf.rma.domain.repository.ProfileRepository
import rs.edu.raf.rma.networking.di.Qualifiers
import rs.edu.raf.rma.presentation.profile.ProfileViewModel

val profileFeatureModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl(api = get(Qualifiers.Authenticated))
    }
    viewModelOf(::ProfileViewModel)
}