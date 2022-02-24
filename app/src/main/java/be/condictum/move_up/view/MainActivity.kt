package be.condictum.move_up.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import be.condictum.move_up.R
import be.condictum.move_up.databinding.ActivityMainBinding
import be.condictum.move_up.util.notification.NotificationUtil
import be.condictum.move_up.util.notification.receiver.ResultReceiver
import be.condictum.move_up.view.ui.goalresult.GoalResultFragmentDirections
import be.condictum.move_up.view.ui.settings.SettingsFragment
import be.condictum.move_up.viewmodel.GoalsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: GoalsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startBottomNavigationView()
        showNotificationIfRequired()
    }

    private fun showNotificationIfRequired() {
        if (notificationShouldShow()) {
            val goalList = viewModel.getAllGoals()
            val nowDate = Date(System.currentTimeMillis())

            goalList.observe(this, Observer { listOfGoals ->
                val upcomingDates = listOfGoals.filter {
                    it.dataDate.after(nowDate) && (TimeUnit.DAYS.convert(
                        it.dataDate.time - nowDate.time,
                        TimeUnit.MILLISECONDS
                    ) + 1 <= 7)
                }

                upcomingDates.forEach {
                    val dayDifference = TimeUnit.DAYS.convert(
                        it.dataDate.time - nowDate.time,
                        TimeUnit.MILLISECONDS
                    ) + 1
                    val title = it.dataName
                    val message = resources.getString(
                        R.string.goal_date_notification_message_formatted_text,
                        dayDifference.toString()
                    )

                    val intent = Intent(applicationContext, ResultReceiver::class.java)
                    intent.action = ResultReceiver.ACTION_CLICK
                    NotificationUtil.with(applicationContext).showNotification(
                        title,
                        message,
                        R.drawable.ic_baseline_warning_24,
                        intent
                    )
                }
            })
        }
    }

    private fun startBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.goalScreenFragment,
                R.id.profileFragment,
                R.id.settingsFragment,
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

    private fun notificationShouldShow(): Boolean {
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        return sharedPreferences.getBoolean(
            SettingsFragment.SHARED_PREFERENCES_KEY_NOTIFICATION_IS_OPEN,
            false
        )
    }
}