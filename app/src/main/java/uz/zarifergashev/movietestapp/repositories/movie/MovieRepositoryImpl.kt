package uz.zarifergashev.movietestapp.repositories.movie

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.zarifergashev.movietestapp.BuildConfig
import uz.zarifergashev.movietestapp.commons.Result
import uz.zarifergashev.movietestapp.models.Movie
import uz.zarifergashev.movietestapp.network.MovieApi

class MovieRepositoryImpl(
    private val api: MovieApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    MovieRepository {
    override suspend fun searchMovie(page: Int, query: String): Result<List<Movie>> =
        withContext(dispatcher) {
            return@withContext try {
                val result = api.searchMovies(
                    query = query,
                    page = page,
                    apiKey = BuildConfig.THEMOVIEDB_API_KEY
                )
                if (result.isSuccessful) {
                    Result.Success(result.body()?.results ?: listOf())
                } else {
                    Result.Error(Exception(result.errorBody()?.string() ?: ""))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}