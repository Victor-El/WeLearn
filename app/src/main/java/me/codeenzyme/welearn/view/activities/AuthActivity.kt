package me.codeenzyme.welearn.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_auth.*
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.view.adapters.AuthFragmentStatePagerAdapter

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val authFragmentStatePagerAdapter = AuthFragmentStatePagerAdapter(supportFragmentManager, lifecycle)
        auth_pager.adapter = authFragmentStatePagerAdapter
        TabLayoutMediator(auth_tab_layout, auth_pager) { tab, position ->
            tab.text = AuthFragmentStatePagerAdapter.TAB_NAMES[position]
        }.attach()
    }

    public fun getPager(): ViewPager2 {
        return auth_pager
    }
}