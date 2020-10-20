package me.codeenzyme.welearn.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import me.codeenzyme.welearn.view.fragments.LoginFragment
import me.codeenzyme.welearn.view.fragments.RegisterFragment

class AuthFragmentStatePagerAdapter constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object{
        val TAB_NAMES = arrayOf("Login", "Register")
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return if (position == 0) {
            LoginFragment()
        } else {
            RegisterFragment()
        }
    }
}