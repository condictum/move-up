package be.condictum.move_up.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import be.condictum.move_up.R
import be.condictum.move_up.databinding.ActivityMainBinding
import be.condictum.move_up.fragment.GoalResultFragmentDirections

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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

        binding.bottomNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavView.setOnItemSelectedListener { item ->
            Navigation.findNavController(this, R.id.main_nav_host_fragment).popBackStack()

            NavigationUI.onNavDestinationSelected(
                item,
                Navigation.findNavController(this, R.id.main_nav_host_fragment)
            )
        }

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
                R.id.goalScreenFragment -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
                else -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val navController = this.findNavController(R.id.main_nav_host_fragment)
        when (navController.currentDestination?.id) {
            R.id.goalResultFragment -> {
                val action =
                    GoalResultFragmentDirections.actionGoalResultFragmentToGoalScreenFragment()
                findNavController(R.id.main_nav_host_fragment).navigate(action)
            }
            R.id.mainFragment -> {
                finish()
            }
            R.id.splashScreenFragment -> {
                finish()
            }
            else -> navController.navigateUp()
        }
    }
}