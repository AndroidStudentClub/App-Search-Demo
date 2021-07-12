package ru.androidschool.appsearch.data

object MovieMapper {

    fun toMovieDocument(movie: List<Movie>): List<MovieDocument> {
        return movie.map { toMovieDocument(it) }.toList()
    }

    fun toMovieDocument(movie: Movie): MovieDocument {
        return MovieDocument(
            namespace = "movie",
            id = movie.id.toString(),
            score = 10,
            title = movie.title ?: "",
            text = movie.overview ?: "",
            poster = movie.posterPath ?: ""
        )
    }

    fun fromMovieDocument(d: MovieDocument): Movie {
        return Movie(
            id = d.id.toInt(),
            overview = d.text,
            title = d.title
        ).apply { posterPath = d.poster }
    }
}