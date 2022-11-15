package com.bytza.photoarchive.ui.account

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bytza.photoarchive.model.LoginApi
import retrofit2.Retrofit

class AccountViewModel : ViewModel() {
    private val _token = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val token: LiveData<String> = _token

}