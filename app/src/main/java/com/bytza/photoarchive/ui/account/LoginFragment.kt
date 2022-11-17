package com.bytza.photoarchive.ui.account

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bytza.photoarchive.R
import com.bytza.photoarchive.databinding.FragmentLoginBinding
import com.bytza.photoarchive.model.LoginApi
import com.bytza.photoarchive.model.LoginResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    val PREFS_FILENAME = "settings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        _binding = FragmentLoginBinding.inflate(inflater)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener() {
            val user = binding.editTextPersonName.text.toString()
            val pass = binding.editTextPassword.text.toString()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://sushkovs.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            var loginApi = retrofit.create(LoginApi::class.java)
            var result = loginApi.login(user, pass)
            result.enqueue(object: Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    var gson = Gson()
                    val loginResponse = response.body() //gson.fromJson<LoginResponse>(response.body(), LoginResponse::class.java)
                    binding.remoteIdTextView.text = if (loginResponse?.session == "") loginResponse.message else loginResponse?.session

                    val prefs: SharedPreferences? =
                        getActivity()?.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
                    if (prefs != null) {
                        val prefsEditor = prefs.edit()
                        prefsEditor.putString("session", loginResponse?.session)
                        prefsEditor.commit()
                    }
                    Navigation.findNavController(it).navigate(R.id.action_navigation_login_to_navigation_account)
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    binding.remoteIdTextView.text="Login failed"
                }
            })
            binding.remoteIdTextView.text = result.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}