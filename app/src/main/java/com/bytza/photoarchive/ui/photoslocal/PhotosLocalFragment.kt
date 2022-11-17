package com.bytza.photoarchive.ui.photoslocal

import android.R.attr.mimeType
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bytza.photoarchive.databinding.FragmentPhotosLocalBinding
import com.bytza.photoarchive.model.DbConnection
import com.bytza.photoarchive.model.photo.PhotosLocal
import com.bytza.photoarchive.model.photo.PhotosLocalRepository
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PhotosLocalFragment : Fragment(), PhotosLocalListAdapter.ClickListener {

    private var _binding: FragmentPhotosLocalBinding? = null
    val LOG_DEBUG = "LOG_DEBUG"
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var db: DbConnection
    private lateinit var repository: PhotosLocalRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val photosLocalViewModel =
            ViewModelProvider(this).get(PhotosLocalViewModel::class.java)

        _binding = FragmentPhotosLocalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d(LOG_DEBUG, "Local fragment onCreateView")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val db = Room.databaseBuilder(requireContext(), DbConnection::class.java, "db").build()
        db = DbConnection.getDatabase(requireContext())
        repository = PhotosLocalRepository(db)
        repository.getAll()
        val adapter = PhotosLocalListAdapter(this)

        repository.photosLocal?.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }

        binding.photosRecyclerView.adapter = adapter
        Log.d(LOG_DEBUG, "Local fragment onViewCreated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(LOG_DEBUG, "Local fragment onViewStateRestored")
    }
    override fun onStart() {
        super.onStart()
        Log.d(LOG_DEBUG, "Local fragment onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.d(LOG_DEBUG, "Local fragment onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.d(LOG_DEBUG, "Local fragment onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.d(LOG_DEBUG, "Local fragment onStop")
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(LOG_DEBUG, "Local fragment onSavedInstanceState")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(LOG_DEBUG, "Local fragment onDestroyView")
    }

    override fun onClickShare(photo: PhotosLocal) {
        super.onClickShare(photo)
//        // Тут нужен uriToImage
//        // это с официальной документации. Не пробовал, можно подтащить uri из PhotosFragment (remote)
//        val shareIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_STREAM, uriToImage)
//            type = "image/jpeg"
//        }
//        startActivity(Intent.createChooser(shareIntent, null))

        val icon: Bitmap = BitmapFactory.decodeFile(photo.fname)
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        val bytes = ByteArrayOutputStream()
        //val temporaryFileName = repository.pathForImages+"/temporary_file.jpg"
        val temporaryDirName = Environment.getExternalStorageDirectory().toString() + File.separator.toString() + Environment.DIRECTORY_PICTURES
        val temporaryDir: File = File(temporaryDirName)
        val exists = temporaryDir.exists()
        val canWrite = temporaryDir.canWrite()
        print(exists)
        print(canWrite)
        val temporaryFileName = temporaryDirName + File.separator.toString() + "temporary_file.jpg"
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val f =
            //File(Environment.getExternalStorageDirectory().toString() + File.separator.toString() + "temporary_file.jpg")
            File(temporaryFileName)
        try {
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // это не работает, не передаёт файл
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(temporaryFileName))
        // вариант 2 для каотинке не работает, можно попробовать для видео
//        val resolver = requireContext().contentResolver
//        val contentValues = ContentValues()
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Photo(change to descrip)")
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
//        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
//        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        share.putExtra(Intent.EXTRA_STREAM, uri)
        // end of variant 2
        share.putExtra(Intent.EXTRA_SUBJECT, temporaryFileName)
        startActivity(Intent.createChooser(share, "Share Image"))
    }
}