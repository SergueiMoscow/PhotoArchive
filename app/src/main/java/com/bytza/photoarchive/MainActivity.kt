package com.bytza.photoarchive

import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bytza.photoarchive.databinding.ActivityMainBinding
import com.bytza.photoarchive.model.DbConnection

class MainActivity : AppCompatActivity() {
    val PREFS_FILENAME = "settings"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var aaa=getApplicationInfo().dataDir
        var db = DbConnection.getDatabase(this)
        db.dataDir= application.dataDir.toString()
        var prefs: SharedPreferences?=this.getSharedPreferences(PREFS_FILENAME, 0)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_photos, R.id.navigation_albums, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}