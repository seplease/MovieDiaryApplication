package ddwu.com.mobile.naverretrofittest

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ddwu.com.mobile.naverretrofittest.databinding.ActivityDetailBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import java.util.Date
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import android.graphics.BitmapFactory
import ddwu.com.mobile.naverretrofittest.ui.MovieAdapter
import java.io.FileNotFoundException

class DetailActivity : AppCompatActivity() {
    private val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var title: String? = null
    private var original_title: String? = null
    private var overview: String? = null
    private var release_date: String? = null
    private var vote_average: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        url = intent.getStringExtra("url")
        title = intent.getStringExtra("title")
        original_title = intent.getStringExtra("original_title")
        overview = intent.getStringExtra("overview")
        release_date = intent.getStringExtra("release_date")
        vote_average = intent.getDoubleExtra("vote_average", 0.0)

        if (url != null) {
            Glide.with(this)
                .load(url)
                .into(detailBinding.poster)
        }

        val movieDetailText = "$title\n$original_title\n$release_date 개봉\n⭐ $vote_average"
        val movieOverview = "$overview"
        detailBinding.tvMovieDetail.text = movieDetailText
        detailBinding.tvMovieOverview.text = movieOverview

        detailBinding.btnAdd.setOnClickListener{
            val intent = Intent( this, AddMemoActivity::class.java).apply {
                putExtra("url", url)
                putExtra("title", title)
            }
            startActivity(intent)
        }
    }
}
