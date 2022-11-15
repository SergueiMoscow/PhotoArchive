package com.bytza.photoarchive.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bytza.photoarchive.databinding.FragmentPhotosBinding

class PotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null

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
        val adapter = PhotosRemoteListAdapter()
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
    }
}