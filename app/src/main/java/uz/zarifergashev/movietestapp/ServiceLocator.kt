package uz.zarifergashev.movietestapp

import uz.zarifergashev.movietestapp.network.MovieApi
import uz.zarifergashev.movietestapp.repositories.movie.MovieRepository
import uz.zarifergashev.movietestapp.repositories.movie.MovieRepositoryImpl

object ServiceLocator {

    @Volatile
    private var movieRepository: MovieRepository? = null

    private var api: MovieApi = MovieApi.instatnce()

    fun provideMovieRepository(): MovieRepository {
        return movieRepository ?: createMovieRepository().also {
            movieRepository = it
        }
    }

    private fun createMovieRepository(): MovieRepository {
        return MovieRepositoryImpl(api = api)
    }

}