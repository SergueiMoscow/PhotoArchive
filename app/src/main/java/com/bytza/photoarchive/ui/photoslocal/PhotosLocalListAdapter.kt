package com.bytza.photoarchive.ui.photoslocal

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bytza.photoarchive.R
import com.bytza.photoarchive.databinding.PhotoLocalListItemBinding
import com.bytza.photoarchive.model.photo.PhotosLocal

class PhotosLocalListAdapter(val listener: ClickListener) : RecyclerView.Adapter<PhotosLocalListAdapter.ViewHolder>() {

    var photosLocal: List<PhotosLocal> = mutableListOf()


    fun updateList(photosLocal: List<PhotosLocal>){
        this.photosLocal = photosLocal
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: PhotoLocalListItemBinding = PhotoLocalListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_local_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.photo = photosLocal[position]
        val view = holder.binding.localPhotoImageView
        view.setImageBitmap(BitmapFactory.decodeFile(photosLocal[position].fname))
        holder.binding.shareImageView.setOnClickListener() {
           listener.onClickShare(photosLocal[position])
        }

    }

    override fun getItemCount(): Int {
        return photosLocal.size
    }

    interface ClickListener {
        fun onClickItem(photo: PhotosLocal) {}
        fun onClickFavorite(photo: PhotosLocal, imageView: ImageView) {}
        fun onClickShare(photo: PhotosLocal) {}
    }


}