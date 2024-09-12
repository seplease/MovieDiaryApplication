package ddwu.com.mobile.naverretrofittest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import ddwu.com.mobile.naverretrofittest.data.NowPlayingMovieRoot
import ddwu.com.mobile.naverretrofittest.databinding.ActivityHomeBinding
import ddwu.com.mobile.naverretrofittest.network.IMovieAPIService
import ddwu.com.mobile.naverretrofittest.ui.MovieAdapter
import ddwu.com.mobile.naverretrofittest.ui.NowPlayingMovieAdaper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"

    val homeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    var root: NowPlayingMovieRoot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(homeBinding.root)

        val adapter = NowPlayingMovieAdaper()

        homeBinding.rvBooks.adapter = adapter
        homeBinding.rvBooks.layoutManager = GridLayoutManager(this, 3) // 3은 한 줄에 표시할 아이템 수

        adapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListner {
            override fun onItemClick(view: View, position: Int) {
                val movie = adapter.nowPlayingMovies?.get(position)
                val url = "https://image.tmdb.org/t/p/w342/${movie?.poster_path}"

                val intent = Intent(applicationContext, DetailActivity::class.java).apply {
                    putExtra("url", url)
                    putExtra("title", movie?.title)
                    putExtra("original_title", movie?.original_title)
                    putExtra("overview", movie?.overview)
                    putExtra("release_date", movie?.release_date)
                    putExtra("vote_average", movie?.vote_average)
                }
                startActivity(intent)
            }
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.tmdb_api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IMovieAPIService::class.java)

        val apiCallback = object : Callback<NowPlayingMovieRoot> {
            override fun onResponse(call: Call<NowPlayingMovieRoot>, response: Response<NowPlayingMovieRoot>) {
                Log.d("TAG", response.toString())

                if (response.isSuccessful) {
                    root = response.body()
                    adapter.nowPlayingMovies = root?.results
                    adapter.notifyDataSetChanged()

                } else {
                    Log.d(TAG, "Unsuccessful Response")
                    Log.d(TAG, response.errorBody()!!.string())     // 응답 오류가 있을 때 상세정보 확인
                }
            }

            override fun onFailure(call: Call<NowPlayingMovieRoot>, t: Throwable) {
                Log.d(TAG, "OpenAPI Call Failure ${t.message}")
            }
        }

        val apiCall: Call<NowPlayingMovieRoot> = service.getNowPlayingMovies(
            resources.getString(R.string.api_key),
            resources.getString(R.string.language)
        )

        apiCall.enqueue(apiCallback)
    }
}
