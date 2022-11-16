package com.bytza.photoarchive.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bytza.photoarchive.R
import com.bytza.photoarchive.databinding.FragmentPhotosBinding
import com.bytza.photoarchive.model.DbConnection
import com.bytza.photoarchive.model.photo.PhotoRemote
import com.bytza.photoarchive.model.photo.PhotosLocal
import com.bytza.photoarchive.model.photo.PhotosLocalRepository
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PotosFragment : Fragment(), PhotosRemoteListAdapter.ClickListener {

    private var _binding: FragmentPhotosBinding? = null
    val scope = MainScope()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var thisView: View? = null

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
        val db = DbConnection.getDatabase(requireContext())
        val localRepository = PhotosLocalRepository(db)
        thisView = view
        localRepository.getRemoteIds()

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
        var db = DbConnection.getDatabase(requireContext())
        var repository = PhotosLocalRepository(db)


        //var localRepository = PhotosLocalRepository(db)
        scope.launch {
            repository.insert(convertRemoteToLocal(photo))
        }.start()
    }

    override fun onClickItem(photo: PhotoRemote) {
        super.onClickItem(photo)

        val bundle: Bundle = Bundle()
        val gson = Gson()
        val jsonString = gson.toJson(photo)
        bundle.putString("itemRemote", jsonString)
        thisView?.let { Navigation.findNavController(it).navigate(R.id.action_navigation_photos_to_navigation_edit_remote, bundle) }

        Toast.makeText(requireContext(), "Нажата фотка типа редкатировать", Toast.LENGTH_LONG)
    }

    override fun onClickShare(photo: PhotoRemote) {
        super.onClickShare(photo)
        Toast.makeText(requireContext(), "Нажата Share (Поделиться)", Toast.LENGTH_LONG)
    }

    fun convertRemoteToLocal(photoRemote: PhotoRemote) : PhotosLocal {
        var photoLocal = PhotosLocal(null, photoRemote.id, photoRemote.fname, photoRemote.fsize, photoRemote.fcreated, photoRemote.descript, photoRemote.tags)
        return photoLocal
    }
}