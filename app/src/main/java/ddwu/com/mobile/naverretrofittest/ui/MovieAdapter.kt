package ddwu.com.mobile.naverretrofittest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ddwu.com.mobile.naverretrofittest.data.Item
import ddwu.com.mobile.naverretrofittest.databinding.ListItemBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    var movies: List<Item>? = null

    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = movies?.get(position)

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w342/${movie?.poster_path}")
            .into(holder.itemBinding.imgBookCover)

        holder.itemBinding.tvTitle.text = movie?.title

        holder.itemBinding.clItem.setOnClickListener {
            clickListener?.onItemClick(it, position)
        }
    }

    class MovieHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListner : NowPlayingMovieAdaper.OnItemClickListner {
        override fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListner? = null

    fun setOnItemClickListener(listener: OnItemClickListner) {
        this.clickListener = listener
    }
}
