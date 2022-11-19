package com.bytza.photoarchive.ui

import android.app.Instrumentation
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bytza.photoarchive.databinding.FragmentEditPhotoBinding
import com.bytza.photoarchive.model.photo.PhotoRemote
import com.bytza.photoarchive.model.photo.PhotoService
import com.bytza.photoarchive.ui.photos.PhotosRemoteViewModel
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ITEM_REMOTE = "itemRemote"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditPhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditPhotoFragment : Fragment() {
    private var param1: String? = null
    private var photoRemote: PhotoRemote? = null
    private lateinit var binding: FragmentEditPhotoBinding
    private var thisView: View? = null
    private lateinit var photosViewModel: PhotosRemoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photosViewModel = ViewModelProvider(requireActivity()).get(PhotosRemoteViewModel::class.java)
        photoRemote=photosViewModel.currentEditItem
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view
        if (photoRemote != null) {
            val url = photoRemote!!.fname
            binding.item = photoRemote
            Picasso.get().load(url).into(binding.imageView)
            binding.button.setOnClickListener() {
                photoRemote!!.descript = binding.nameEditText.text.toString()
                update(photoRemote!!)
// вариант 1 Так список обновляется целиком и показывается верх после возврата с edit
//                thisView?.let { Navigation.findNavController(it).navigate(R.id.action_navigation_edit_remote_to_navigation_photos) }
// вариант 2 Так возвращается на место, но следующее edit вылетает из программы
//                onBackPressed()
// вариант 3 Так первым нажатием закрывает клавиатуру, вторым выходит на место. Приемлемо.
                Thread {
                    val inst = Instrumentation()
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
                }.start()
            }
        }
    }

    fun onBackPressed() {
        var fm = getActivity()?.getSupportFragmentManager()
        if (fm != null) {
            fm.popBackStack()
        }
    }

    fun update(photo: PhotoRemote)
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://sushkovs.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val photoApi = retrofit.create(PhotoService::class.java)
        val result = photoApi.updatePhoto(photo.id, photo.descript)
        result.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val string = response.body().toString()
                print(string)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                var err = t
                print(t)
            }
        })

    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment EditPhotoFragment.
//         */
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            EditPhotoFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ITEM_REMOTE, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}