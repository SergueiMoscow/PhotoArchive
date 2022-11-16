package com.bytza.photoarchive.ui.photoslocal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bytza.photoarchive.R
import com.bytza.photoarchive.databinding.PhotoLocalListItemBinding
import com.bytza.photoarchive.model.photo.PhotosLocal
import com.squareup.picasso.Picasso

class PhotosLocalListAdapter : RecyclerView.Adapter<PhotosLocalListAdapter.ViewHolder>() {

    var users: List<PhotosLocal> = mutableListOf()


    fun updateList(users: List<PhotosLocal>){
        this.users = users
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
        holder.binding.photo = users[position]
        val view = holder.binding.localPhotoImageView
        Picasso.get().load(users[position].fname).into(view)

    }

    override fun getItemCount(): Int {
        return users.size
    }


}