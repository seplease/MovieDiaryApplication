package ddwu.com.mobile.naverretrofittest

import ddwu.com.mobile.naverretrofittest.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import ddwu.com.mobile.naverretrofittest.data.MovieRoot
import ddwu.com.mobile.naverretrofittest.databinding.ActivityMainBinding
import ddwu.com.mobile.naverretrofittest.network.IMovieAPIService
import ddwu.com.mobile.naverretrofittest.ui.MovieAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    var root: MovieRoot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        val adapter = MovieAdapter()

        mainBinding.rvBooks.adapter = adapter
        mainBinding.rvBooks.layoutManager = GridLayoutManager(this, 3) // 3은 한 줄에 표시할 아이템 수

        adapter.setOnItemClickListener(object : MovieAdapter.OnItemClickListner {
            override fun onItemClick(view: View, position: Int) {
                val movie = adapter.movies?.get(position)
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

        mainBinding.btnHome.setOnClickListener {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        mainBinding.btnMyMemo.setOnClickListener {
            val intent = Intent(this@MainActivity, ShowMemoListActivity::class.java)
            startActivity(intent)
        }

        mainBinding.btnSearch.setOnClickListener {
            val keyword = mainBinding.etKeyword.text.toString()

            val apiCallback = object : Callback<MovieRoot> {
                override fun onResponse(call: Call<MovieRoot>, response: Response<MovieRoot>) {
                    Log.d("TAG", response.toString())

                    if (response.isSuccessful) {
                        root = response.body()
                        adapter.movies = root?.results
                        adapter.notifyDataSetChanged()

                    } else {
                        Log.d(TAG, "Unsuccessful Response")
                        Log.d(TAG, response.errorBody()!!.string())     // 응답 오류가 있을 때 상세정보 확인
                    }
                }

                override fun onFailure(call: Call<MovieRoot>, t: Throwable) {
                    Log.d(TAG, "OpenAPI Call Failure ${t.message}")
                }
            }

            val apiCall: Call<MovieRoot> = service.getMoviesByKeyword(
                resources.getString(R.string.api_key),
                resources.getString(R.string.page),
                keyword,
                resources.getString(R.string.language)
            )

            apiCall.enqueue(apiCallback)
        }
    }
}
