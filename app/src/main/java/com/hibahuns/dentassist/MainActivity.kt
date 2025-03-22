package com.hibahuns.dentassist

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hibahuns.dentassist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        val customToolbar = findViewById<Toolbar>(R.id.customToolbar)
        setSupportActionBar(customToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

        val profilePicture = binding.profilePicture
        profilePicture.setOnClickListener {
            navController.navigate(R.id.navigation_profile, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, false)
                    .build()
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}