package com.bytza.photoarchive.ui.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bytza.photoarchive.R
import com.bytza.photoarchive.databinding.PhotoListItemBinding
import com.bytza.photoarchive.model.photo.PhotoRemote
import com.squareup.picasso.Picasso

class PhotosRemoteListAdapter(val listener: ClickListener) : RecyclerView.Adapter<PhotosRemoteListAdapter.ViewHolder>() {
    var items: List<PhotoRemote> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var remoteIdList: List<Int> = mutableListOf()
        set(value) {
            field = value
        }

//    private var itemClick: (PhotoRemote) -> Unit = {}
//    fun itemClick(listener: (PhotoRemote) -> Unit) {
//        itemClick = listener
//    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = PhotoListItemBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.photo = items[position]
        val url  = items[position].fname
        val view = holder.binding.photoImageView
        Picasso.get().load(url).into(view)
        if (items[position].local > 0)
            holder.binding.likeImageView.setImageResource(R.drawable.ic_baseline_favorite_24)
            //Picasso.get().load(R.drawable.ic_baseline_favorite_24).into(holder.binding.likeImageView)
        holder.binding.remoteCardView.setOnClickListener {
            listener.onClickItem(items[position])
        }
        holder.binding.likeImageView.setOnClickListener{
            listener.onClickFavorite(items[position], holder.binding.likeImageView)
        }
        holder.binding.shareImageView.setOnClickListener{
            listener.onClickShare(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ClickListener {
        fun onClickItem(photo: PhotoRemote) {}
        fun onClickFavorite(photo: PhotoRemote, imageView: ImageView) {}
        fun onClickShare(photo: PhotoRemote) {}
    }

}