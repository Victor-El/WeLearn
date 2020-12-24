package me.codeenzyme.welearn.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import me.codeenzyme.welearn.MainNavigationDirections
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = (supportFragmentManager.findFragmentById(binding.navHost.id) as NavHostFragment).navController

        binding.bottomNav.run {
            selectedItemId = R.id.bottom_menu_home
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.bottom_menu_profile -> {
                        val action = MainNavigationDirections.actionGlobalProfileFragment()
                        navController.navigate(action)
                    }
                    R.id.bottom_menu_home -> {
                        val action = MainNavigationDirections.actionGlobalHomeFragment()
                        navController.navigate(action)
                    }
                    R.id.bottom_menu_leaderboard -> {
                        val action = MainNavigationDirections.actionGlobalLeaderboardFragment()
                        navController.navigate(action)
                    }
                    R.id.bottom_menu_settings -> {
                        val action = MainNavigationDirections.actionGlobalSettingsFragment()
                        navController.navigate(action)
                    }
                }
                true
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}