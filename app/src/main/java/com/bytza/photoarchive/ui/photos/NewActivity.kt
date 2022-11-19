package com.bytza.photoarchive.ui.photos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bytza.photoarchive.databinding.ActivityNewBinding
import com.bytza.photoarchive.model.DbConnection
import com.bytza.photoarchive.model.photo.PhotoService
import com.bytza.photoarchive.model.photo.PhotosLocalRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*


class NewActivity : AppCompatActivity() {
    private lateinit var db: DbConnection
    private lateinit var repository: PhotosLocalRepository
    private lateinit var binding: ActivityNewBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DbConnection.getDatabase(this)
        repository = PhotosLocalRepository(db)


        // Обработка пришедшего интента
        val intent = getIntent()
        val action = intent.getAction()
        var type: String? = intent.getType()
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM)
                binding.newImageView.setImageURI(imageUri)

            }
        }

        binding.saveButton.setOnClickListener() {
            // get inputStream from received file
            var localTempFileName = db.dataDir + repository.pathForImages + "/downloaded.jpg"
            var localTempFile = File(localTempFileName)
            val inputStream: InputStream? = imageUri?.let { it1 ->
                contentResolver.openInputStream(
                    it1
                )
            }
            if (inputStream != null) {
                // get real temporary file
                copyInputStreamToFile(inputStream, localTempFile)
            }


            val file = File(localTempFileName) //imageUri?.getPath()?.let { it1 -> File(it1) }
            if (file != null) {
                val descript = binding.descriptionEditText.text.toString()
                var fileName=file.getName()
                val exists = file.exists()
                val isFile = file.isFile
                val absolutePath = file.absolutePath
                print(exists.toString()+isFile+absolutePath)

                // convert to Multipart
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                var miltipartImage = MultipartBody.Part
                    .createFormData("image", file.getName(), requestFile)

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://sushkovs.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val photoApi = retrofit.create(PhotoService::class.java)

                val result = photoApi.insertPhoto(descript, miltipartImage)
                result.enqueue(object: Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        showDialog("Image uploaded", "Info")
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        showDialog("Image upload failed", "Error")

                    }
                })

            }

        }
    }

    fun showDialog(message: String, title: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNeutralButton(android.R.string.ok) { dialog, which -> finish()}
    }


    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        var out: OutputStream? = null
        try {
            out = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if (out != null) {
                    out.close()
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}