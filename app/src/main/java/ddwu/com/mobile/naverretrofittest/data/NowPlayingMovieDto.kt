package ddwu.com.mobile.naverretrofittest.data

data class NowPlayingMovieRoot(
    val dates: Dates,
    val page: Long,
    val results: List<NowPlayingMovie>,
    val totalPages: Long,
    val totalResults: Long,
)

data class Dates(
    val maximum: String,
    val minimum: String,
)

data class NowPlayingMovie(
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
)
