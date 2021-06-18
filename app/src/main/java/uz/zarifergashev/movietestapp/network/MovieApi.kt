package uz.zarifergashev.movietestapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uz.zarifergashev.movietestapp.BuildConfig
import uz.zarifergashev.movietestapp.models.Movie
import uz.zarifergashev.movietestapp.models.PagingResult
import java.util.concurrent.TimeUnit

interface MovieApi {
    @GET("/search/search-movies")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String,
    ): Response<PagingResult<Movie>>

    companion object {
        var INSTANCE: MovieApi? = null

        fun instatnce(): MovieApi {
            return INSTANCE
                ?: createInstance().also { INSTANCE = it }
        }

        private fun createInstance(): MovieApi {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(MovieApi::class.java)
        }
    }
}