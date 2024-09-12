package ddwu.com.mobile.naverretrofittest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ddwu.com.mobile.naverretrofittest.data.NowPlayingMovie
import ddwu.com.mobile.naverretrofittest.databinding.ListItemBinding

class NowPlayingMovieAdaper : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    var nowPlayingMovies: List<NowPlayingMovie>? = null

    override fun getItemCount(): Int {
        return nowPlayingMovies?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieAdapter.MovieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieHolder, position: Int) {
        val movie = nowPlayingMovies?.get(position)

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w342/${movie?.poster_path}")
            .into(holder.itemBinding.imgBookCover)

        holder.itemBinding.tvTitle.text = movie?.title

        holder.itemBinding.clItem.setOnClickListener {
            clickListener?.onItemClick(it, position)
        }
    }

    class MovieHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListner {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListner? = null

    fun setOnItemClickListener(listener: MovieAdapter.OnItemClickListner) {
        this.clickListener = listener
    }
}
