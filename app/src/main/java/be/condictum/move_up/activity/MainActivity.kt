package be.condictum.move_up.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import be.condictum.move_up.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.goalScreenFragment,
                R.id.profileFragment,
                R.id.settingsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(
                item,
                Navigation.findNavController(this, R.id.main_nav_host_fragment)
            )
        }

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.goalScreenFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.mainFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.splashScreenFragment -> {
                    bottomNavigationView.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
        }

    }
}