package com.bytza.photoarchive.ui.account

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bytza.photoarchive.model.LoginApi
import com.bytza.photoarchive.model.LoginResponse
import retrofit2.Retrofit

class AccountViewModel : ViewModel() {
    private val _session = MutableLiveData<LoginResponse>().apply {
        value = LoginResponse("Session", "Message")
    }
    val session: LiveData<LoginResponse> = _session

}