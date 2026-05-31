package rs.edu.raf.rma.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import rs.edu.raf.rma.auth.di.authFeatureModule
import rs.edu.raf.rma.presentation.catalog.di.catalogFeatureModule
import rs.edu.raf.rma.core.auth.di.authCoreModule
import rs.edu.raf.rma.core.db.di.databaseModule
import rs.edu.raf.rma.networking.di.networkingModule
import rs.edu.raf.rma.presentation.state.FilterManager
import rs.edu.raf.rma.splash.di.splashModule

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        config?.invoke(this)
        modules(
            databaseModule(),
            networkingModule,
            authCoreModule,
            authFeatureModule,
            catalogFeatureModule,
            splashModule,
            module {
                single { FilterManager() }
            }
        )
    }
}
