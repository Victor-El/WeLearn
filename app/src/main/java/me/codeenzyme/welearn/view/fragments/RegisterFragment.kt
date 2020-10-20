package me.codeenzyme.welearn.view.fragments

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.*
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.view.activities.AuthActivity
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment() : Fragment(), Validator.ValidationListener {

    lateinit var validator: Validator

    lateinit var registerFragmentRootView: LinearLayout

    @NotEmpty
    @Length(min = 2, max = 30, trim = true, message = "First name should be between 2 to 30 characters")
    lateinit var firstNameTextView: TextView

    @NotEmpty
    @Length(min = 2, max = 30, trim = true, message = "Last name should be between 2 to 30 characters")
    lateinit var lastNameTextView: TextView

    @Email
    lateinit var emailTextView: TextView

    @Password(min = 6)
    lateinit var passwordTextView: TextView

    @ConfirmPassword
    lateinit var confirmPasswordTextView: TextView

    @Checked(message = "You must agree to the terms and conditions")
    lateinit var agreeWithTermsAndConditionCheckBox: CheckBox

    lateinit var registerButton: Button

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews(view)

        setUpValidation()

    }

    private fun setUpViews(view: View) {
        agreeWithTermsAndConditionCheckBox = view.findViewById<CheckBox>(R.id.agree_register_checkbox)
        agreeWithTermsAndConditionCheckBox.movementMethod = LinkMovementMethod.getInstance()
        agreeWithTermsAndConditionCheckBox.setLinkTextColor(resources.getColor(R.color.colorPrimary))

        firstNameTextView = view.findViewById(R.id.register_first_name)
        lastNameTextView = view.findViewById(R.id.register_last_name)
        emailTextView = view.findViewById(R.id.register_email)
        passwordTextView = view.findViewById(R.id.register_password)
        confirmPasswordTextView = view.findViewById(R.id.register_confirm_password)

        registerButton = view.findViewById(R.id.register_btn)
        registerButton.setOnClickListener {
            validator.validate()
        }

        registerFragmentRootView = view.findViewById(R.id.register_fragment_root)

        val goToLoginTextView = view.findViewById<TextView>(R.id.go_to_login_tv)

        Toast.makeText(activity, "Hello" + firebaseAuth.currentUser, Toast.LENGTH_LONG).show()

        goToLoginTextView.setOnClickListener { _: View? ->
            val viewPager = (activity as AuthActivity).getPager()
            viewPager.setCurrentItem(0, true)
        }
    }

    private fun setUpValidation() {
        validator = Validator(this)
        validator.setValidationListener(this)
    }

    override fun onValidationSucceeded() {
        Toast.makeText(activity, "Validation Successful", Toast.LENGTH_LONG).show()
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>) {

        for (error in errors) {
            val view = error.view
            if (view is EditText) {
                view.error = error.getCollatedErrorMessage(activity)
            }

            if (errors.size == 1 && view is CheckBox) {
                val snackbar = Snackbar.make(registerFragmentRootView, error.getCollatedErrorMessage(activity), Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") {
                    snackbar.dismiss()
                }.show()
            }
        }
    }
}