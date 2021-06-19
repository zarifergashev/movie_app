package uz.zarifergashev.movietestapp.models

import com.google.gson.annotations.SerializedName
import uz.zarifergashev.movietestapp.BuildConfig

data class Movie(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("original_title") val originalTitle: String? = null,
    @SerializedName("original_language") val originalLanguage: String? = null,
    @SerializedName("adult") val adult: Boolean? = null,
    @SerializedName("video") val video: Boolean? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("popularity") val popularity: Float? = null,
    @SerializedName("vote_count") val voteCount: Int? = null,
    @SerializedName("vote_average") val voteAverage: Float? = null,
    @SerializedName("genre_ids") val genreIds: List<Int>? = null,
) {
    fun avatarUrl(size: Int = 500): String = if (posterPath?.isNotEmpty() == true) {
        "${BuildConfig.IMAGE_URL}w${size}$posterPath"
    } else {
        ""
    }
}