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
import be.condictum.move_up.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.bottomNavView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(
                item,
                Navigation.findNavController(this, R.id.main_nav_host_fragment)
            )
        }

        binding.bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.splashScreenFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                else -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }

    }
}