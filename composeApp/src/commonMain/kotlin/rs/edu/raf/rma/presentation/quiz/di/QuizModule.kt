package rs.edu.raf.rma.presentation.quiz.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.rma.data.repository.QuizRepositoryImpl
import rs.edu.raf.rma.domain.repository.QuizRepository
import rs.edu.raf.rma.networking.di.Qualifiers
import rs.edu.raf.rma.presentation.quiz.QuizViewModel
val quizFeatureModule = module {

    single<QuizRepository> {
        QuizRepositoryImpl(
            get(),
            get(Qualifiers.Authenticated)
        )
    }
    viewModelOf(::QuizViewModel)
}