package com.bytza.photoarchive.ui.photoslocal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.bytza.photoarchive.databinding.FragmentPhotosLocalBinding
import com.bytza.photoarchive.model.DbConnection
import com.bytza.photoarchive.model.photo.PhotosLocalRepository

class PhotosLocalFragment : Fragment() {

    private var _binding: FragmentPhotosLocalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val photosLocalViewModel =
            ViewModelProvider(this).get(PhotosLocalViewModel::class.java)

        _binding = FragmentPhotosLocalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val db = Room.databaseBuilder(requireContext(), DbConnection::class.java, "db").build()
        val db = DbConnection.getDatabase(requireContext())
        val repository = PhotosLocalRepository(db)
        repository.getAll()
        val adapter = PhotosLocalListAdapter()

        repository.photosLocal?.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }

        binding.photosRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}