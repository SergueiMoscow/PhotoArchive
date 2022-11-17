package com.bytza.photoarchive.ui.photos

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.System.load


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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scope = MainScope()
        val photosViewModel =
            ViewModelProvider(this).get(PhotosRemoteViewModel::class.java)

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

        val bundle: Bundle = Bundle()
        val gson = Gson()
        val jsonString = gson.toJson(photo)
        bundle.putString("itemRemote", jsonString)
        thisView?.let { Navigation.findNavController(it).navigate(R.id.action_navigation_photos_to_navigation_edit_remote, bundle) }
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
        var photoLocal = PhotosLocal(null, photoRemote.id, photoRemote.fname, photoRemote.fsize, photoRemote.fcreated, photoRemote.descript, photoRemote.tags)
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
//            val localBitmapUri= FileProvider.getUriForFile(
//                this,
//                "com.bytza.photoarchive.provider",
//                bitmap
//            )
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
//            val exists = fileJustDir.exists()
//            val canWrite = fileJustDir.canWrite()
//            val fileExists = file.exists()
//            val databasePath = this.context?.getApplicationContext()?.getDatabasePath("db");


//            var out: FileOutputStream =  FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.close();
//            bmpUri = Uri.fromFile(file);
        } catch (e: IOException) {
            e.printStackTrace();
        }
        return bmpUri
    }

}