package com.bytza.photoarchive.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bytza.photoarchive.R
import com.bytza.photoarchive.databinding.FragmentEditPhotoBinding
import com.bytza.photoarchive.model.photo.PhotoRemote
import com.bytza.photoarchive.model.photo.PhotoService
import com.google.gson.Gson
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ITEM_REMOTE)
        }
        if (param1 != null) {
            var gson = Gson()
            photoRemote = gson.fromJson(param1, PhotoRemote::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentEditPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view
        if (photoRemote != null) {
            var name = photoRemote!!.descript
            val url = photoRemote!!.fname
            binding.item = photoRemote
            Picasso.get().load(url).into(binding.imageView)
            binding.button.setOnClickListener() {
                photoRemote!!.descript = binding.nameEditText.text.toString()
                update(photoRemote!!)
                //thisView?.let { Navigation.findNavController(it).navigate(R.id.action_navigation_edit_remote_to_navigation_photos) }
                //onBackPressed()

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditPhotoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditPhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ITEM_REMOTE, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}