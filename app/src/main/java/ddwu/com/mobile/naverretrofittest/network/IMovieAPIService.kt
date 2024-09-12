package ddwu.com.mobile.naverretrofittest.network

import ddwu.com.mobile.naverretrofittest.data.MovieRoot
import ddwu.com.mobile.naverretrofittest.data.NowPlayingMovieRoot
//import ddwu.com.mobile.naverretrofittest.data.NowPlayingMovieRoot
import org.intellij.lang.annotations.Language
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IMovieAPIService {

    @GET("search/movie")
    fun getMoviesByKeyword(
        @Query("api_key") apiKey: String,
        @Query("page") page: String,
        @Query("query") keyword: String,
        @Query("language") language: String
    ): Call<MovieRoot>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<NowPlayingMovieRoot>
}
