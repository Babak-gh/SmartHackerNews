package mobi.artapps.smarthackernews

import mobi.artapps.smarthackernews.data.NewsRepository
import mobi.artapps.smarthackernews.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { NewsRepository() }
    viewModel { MainViewModel(get()) }
}