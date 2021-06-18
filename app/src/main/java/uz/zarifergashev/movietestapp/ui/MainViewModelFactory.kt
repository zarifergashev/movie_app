package uz.zarifergashev.movietestapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.zarifergashev.movietestapp.ServiceLocator
import uz.zarifergashev.movietestapp.repositories.movie.MovieRepository
import uz.zarifergashev.movietestapp.ui.movie.movie_search.MovieSearchViewModel

class MainViewModelFactory(
    private val movieRepository: MovieRepository = ServiceLocator.provideMovieRepository()
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(MovieSearchViewModel::class.java) -> {
                MovieSearchViewModel(movieRepository = movieRepository)
            }
            else -> IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}