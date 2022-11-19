package com.bytza.photoarchive.ui.photos

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bytza.photoarchive.R
import com.bytza.photoarchive.databinding.FragmentPhotosBinding
import com.bytza.photoarchive.model.DbConnection
import com.bytza.photoarchive.model.photo.PhotoRemote
import com.bytza.photoarchive.model.photo.PhotosLocal
import com.bytza.photoarchive.model.photo.PhotosLocalRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException


class PotosFragment : Fragment(), PhotosRemoteListAdapter.ClickListener {

    private var _binding: FragmentPhotosBinding? = null
    lateinit var scope: CoroutineScope
    val LOG_DEBUG = "LOG_DEBUG"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var thisView: View? = null
    private lateinit var db: DbConnection
    private lateinit var localRepository: PhotosLocalRepository
    private lateinit var photosViewModel: PhotosRemoteViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scope = MainScope()
        photosViewModel =
            ViewModelProvider(requireActivity()).get(PhotosRemoteViewModel::class.java)
        photosViewModel.instance = "PhotosFragment"
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DbConnection.getDatabase(requireContext())
        localRepository = PhotosLocalRepository(db)
        thisView = view
        localRepository.getRemoteIds()

        val adapter = PhotosRemoteListAdapter(this)
        binding.photosRecyclerView.adapter = adapter
        photosViewModel.photos.observe(requireActivity()) {
            if (it != null) {
                adapter.items = it
                if (photosViewModel.currentEditItem != null) {
                    adapter.items[1].descript = photosViewModel.currentEditItem!!.descript
                }
                photosViewModel.currentEditItem = null
                photosViewModel.currentEditPosition = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
    }

    override fun onClickFavorite(photo: PhotoRemote, imageView: ImageView) {
        super.onClickFavorite(photo, imageView)
        if (photo.local>0)
        {
            // Delete from local DB
            scope.launch {
                Log.d(LOG_DEBUG, "PhotosFragment.kt - Local Delete launched")
                localRepository.deleteByRemoteId(photo)
            }.start()
            imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            photo.local = 0
        } else {
            // Insert to local DB
            scope.launch {
                Log.d(LOG_DEBUG, "PhotosFragment.kt - Local Insert launched")
                localRepository.insert(convertRemoteToLocal(photo))
            }.start()
            imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
            photo.local = 1
        }
    }

    override fun onClickItem(photo: PhotoRemote) {
        super.onClickItem(photo)

        photosViewModel.currentEditItem = photo
        thisView?.let { Navigation.findNavController(it).navigate(R.id.action_navigation_photos_to_navigation_edit_remote) }
    }

    override fun onClickShare(photo: PhotoRemote) {
        super.onClickShare(photo)
        //shareItem(photo.fname)

// Это работает!!! Отправляет ссылку
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, photo.fname)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun convertRemoteToLocal(photoRemote: PhotoRemote) : PhotosLocal {
        val photoLocal = PhotosLocal(null, photoRemote.id, photoRemote.fname, photoRemote.fsize, photoRemote.fcreated, photoRemote.descript, photoRemote.tags)
        return photoLocal
    }

    fun shareItem(url: String?) {
        var bitmap: Bitmap
        GlobalScope.launch {
            bitmap = Picasso.get().load(url).get()
            val bmtUri: Uri? = getLocalBitmapUri(bitmap)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            val localBitmapUri=getLocalBitmapUri(bitmap)
            intent.putExtra(Intent.EXTRA_STREAM, localBitmapUri)
             startActivity(Intent.createChooser(intent, getString(R.string.share_sith)));
            //startActivity(intent)
        }.start()
    }

    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null;
        try {
            val fileName = db.dataDir + localRepository.pathForImages + "/temp.jpg"
            val file =  File(fileName)
            val justDir = localRepository.justPath(fileName)
            val fileJustDir = File(justDir)
            fileJustDir.mkdirs()
            bmpUri = FileProvider.getUriForFile(
                this.requireContext(),
                "com.bytza.photoarchive",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace();
        }
        return bmpUri
    }
}