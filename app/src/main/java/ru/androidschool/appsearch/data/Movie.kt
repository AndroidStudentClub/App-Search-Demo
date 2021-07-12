package ru.androidschool.appsearch.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title", alternate = ["name"])
    var title: String?
) {
    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = "https://image.tmdb.org/t/p/w500$field"
}