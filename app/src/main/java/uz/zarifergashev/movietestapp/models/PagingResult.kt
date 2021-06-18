package uz.zarifergashev.movietestapp.models

import com.google.gson.annotations.SerializedName

data class PagingResult<T>(
    @SerializedName("page") val page: Int? = 0,
    @SerializedName("results") val results: List<T> = emptyList(),
    @SerializedName("total_results") val totalResults: Int? = 0,
    @SerializedName("total_pages") val totalPages: Int? = 0,
)