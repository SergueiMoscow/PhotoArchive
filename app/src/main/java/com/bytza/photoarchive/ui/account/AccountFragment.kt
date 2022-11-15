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
import com.bytza.photoarchive.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    val PREFS_FILENAME = "settings"
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
        accountViewModel.token.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutButton.setOnClickListener(){
            val prefs: SharedPreferences? =
                getActivity()?.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
            if (prefs != null) {
                val prefsEditor = prefs.edit()
                prefsEditor.remove("token")
                prefsEditor.commit()
                Navigation.findNavController(it).navigate(R.id.action_navigation_account_to_navigation_login)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}