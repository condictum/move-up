package be.condictum.move_up.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import be.condictum.move_up.R

class MainActivity : AppCompatActivity() {
    private val navHostFragment =
        supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
    val navController = navHostFragment.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}