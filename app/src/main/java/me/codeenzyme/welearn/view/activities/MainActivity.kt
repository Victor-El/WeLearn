package me.codeenzyme.welearn.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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
            // used this block when implementing bottom nav listener manually
            /*selectedItemId = R.id.homeFragment
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.profileFragment -> {
                        val action = MainNavigationDirections.actionGlobalProfileFragment()
                        navController.navigate(action)
                    }
                    R.id.homeFragment -> {
                        val action = MainNavigationDirections.actionGlobalHomeFragment()
                        navController.navigate(action)
                    }
                    R.id.leaderboardFragment -> {
                        val action = MainNavigationDirections.actionGlobalLeaderboardFragment()
                        navController.navigate(action)
                    }
                    R.id.settingsFragment -> {
                        val action = MainNavigationDirections.actionGlobalSettingsFragment()
                        navController.navigate(action)
                    }
                }
                true
            }*/

            setupWithNavController(navController)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // finishAffinity()
    }
}