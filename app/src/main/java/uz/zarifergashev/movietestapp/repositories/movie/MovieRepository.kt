package uz.zarifergashev.movietestapp.repositories.movie

import uz.zarifergashev.movietestapp.commons.Result
import uz.zarifergashev.movietestapp.models.Movie
import uz.zarifergashev.movietestapp.models.PagingResult

interface MovieRepository {
    suspend fun searchMovie(page: Int, query: String = ""): Result<PagingResult<Movie>>
}