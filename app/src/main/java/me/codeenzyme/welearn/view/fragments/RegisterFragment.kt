package me.codeenzyme.welearn.view.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.view.activities.AuthActivity

class RegisterFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val checkbox = view.findViewById<CheckBox>(R.id.agree_register_checkbox)
        checkbox.movementMethod = LinkMovementMethod.getInstance()
        checkbox.setLinkTextColor(resources.getColor(R.color.colorPrimary))

        val goToLoginTextView = view.findViewById<TextView>(R.id.go_to_login_tv)

        goToLoginTextView.setOnClickListener { _: View? ->
            val viewPager = (activity as AuthActivity).getPager()
            viewPager.setCurrentItem(0, true)
        }
    }
}