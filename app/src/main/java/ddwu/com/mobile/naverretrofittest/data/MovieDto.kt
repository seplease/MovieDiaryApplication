package ddwu.com.mobile.naverretrofittest.data

data class MovieRoot(
    val page: Long,
    val results: List<Item>,
    val total_pages: Long,
    val total_results: Long,
)

data class Item(
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
)
