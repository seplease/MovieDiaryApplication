package ddwu.com.mobile.naverretrofittest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ddwu.com.mobile.naverretrofittest.data.MemoDto
import ddwu.com.mobile.naverretrofittest.databinding.ListItemBinding

class MemoAdapter: RecyclerView.Adapter<MemoAdapter.MemoHolder>(){
    var memoList: List<MemoDto>? = null
    var itemClickListener: OnMemoItemClickListener? = null

    override fun getItemCount(): Int {
        return memoList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        val itemBinding = ListItemBinding.inflate( LayoutInflater.from(parent.context), parent, false)
        return MemoHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        val memo = memoList?.get(position)

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w342/${memo?.poster_path}")
            .into(holder.itemBinding.imgBookCover)

        holder.itemBinding.tvTitle.text = memo?.title

        holder.itemBinding.clItem.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    class MemoHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnMemoItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnMemoItemClickListener) {
        itemClickListener = listener
    }
}
