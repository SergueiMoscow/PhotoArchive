package com.bytza.photoarchive.ui.photos

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bytza.photoarchive.databinding.FragmentPhotosBinding
import com.bytza.photoarchive.model.DbConnection
import com.bytza.photoarchive.model.photo.PhotoRemote
import com.bytza.photoarchive.model.photo.PhotosLocal
import com.bytza.photoarchive.model.photo.PhotosLocalRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PotosFragment : Fragment(), PhotosRemoteListAdapter.ClickListener {

    private var _binding: FragmentPhotosBinding? = null
    val scope = MainScope()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val photosViewModel =
            ViewModelProvider(this).get(PhotosRemoteViewModel::class.java)

        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PhotosRemoteListAdapter(this)
        binding.photosRecyclerView.adapter = adapter
        val photosViewModel = ViewModelProvider(this)[PhotosRemoteViewModel::class.java]
        photosViewModel.photos.observe(requireActivity()) {
            if (it != null) {
                adapter.items = it
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
    }

    override fun onClickFavorite(photo: PhotoRemote) {
        super.onClickFavorite(photo)
        Toast.makeText(requireContext(), "Нажата Favorite (Добавить в локальную базу", Toast.LENGTH_LONG)
        var db = Room.databaseBuilder(requireContext().applicationContext, DbConnection::class.java, "db").build()
        var localRepository = PhotosLocalRepository(db)
        scope.launch {
            db.photosLocalDao().insert(convertRemoteToLocal(photo))
        }
    }

    override fun onClickItem(photo: PhotoRemote) {
        super.onClickItem(photo)
        Toast.makeText(requireContext(), "Нажата фотка типа редкатировать", Toast.LENGTH_LONG)
    }

    override fun onClickShare(photo: PhotoRemote) {
        super.onClickShare(photo)
        Toast.makeText(requireContext(), "Нажата Share (Поделиться)", Toast.LENGTH_LONG)
    }

    fun convertRemoteToLocal(photoRemote: PhotoRemote) : PhotosLocal {
        var photoLocal = PhotosLocal(0, photoRemote.id, photoRemote.fname, photoRemote.fsize, photoRemote.fcreated, photoRemote.descript, photoRemote.tags)
        return photoLocal
    }
}