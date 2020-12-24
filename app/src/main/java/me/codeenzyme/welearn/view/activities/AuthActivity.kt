package me.codeenzyme.welearn.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.view.adapters.AuthFragmentStatePagerAdapter

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (Firebase.auth.currentUser != null)
            startActivity(Intent(this, MainActivity::class.java))

        val authFragmentStatePagerAdapter = AuthFragmentStatePagerAdapter(supportFragmentManager, lifecycle)
        auth_pager.adapter = authFragmentStatePagerAdapter
        TabLayoutMediator(auth_tab_layout, auth_pager) { tab, position ->
            tab.text = AuthFragmentStatePagerAdapter.TAB_NAMES[position]
        }.attach()
    }

    fun getPager(): ViewPager2 {
        return auth_pager
    }
}