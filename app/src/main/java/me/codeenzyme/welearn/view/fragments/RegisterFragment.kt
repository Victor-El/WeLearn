package me.codeenzyme.welearn.view.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.model.RegisterResponse
import me.codeenzyme.welearn.model.User
import me.codeenzyme.welearn.utils.getStringValue
import me.codeenzyme.welearn.utils.getTrimmedStringValue
import me.codeenzyme.welearn.view.activities.AuthActivity
import me.codeenzyme.welearn.view.activities.MainActivity
import me.codeenzyme.welearn.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment() : Fragment(), Validator.ValidationListener {

    lateinit var validator: Validator

    lateinit var registerFragmentRootView: LinearLayout

    lateinit var progressDialog: ProgressDialog

    @NotEmpty
    @Length(min = 2, max = 30, trim = true, message = "First name should be between 2 to 30 characters")
    lateinit var firstNameEditText: EditText

    @NotEmpty
    @Length(min = 2, max = 30, trim = true, message = "Last name should be between 2 to 30 characters")
    lateinit var lastNameEditText: EditText

    @Email
    lateinit var emailEditText: EditText

    @Password(min = 6)
    lateinit var passwordEditText: EditText

    @ConfirmPassword
    lateinit var confirmPasswordEditText: TextView

    @Checked(message = "You must agree to the terms and conditions")
    lateinit var agreeWithTermsAndConditionCheckBox: CheckBox

    lateinit var registerButton: Button

    val authViewModel by viewModels<AuthViewModel>()

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

        progressDialog = ProgressDialog(context).apply {
            setCancelable(false)
            setTitle(getString(R.string.registering))
            setMessage(getString(R.string.creating_account))
        }

        agreeWithTermsAndConditionCheckBox = view.findViewById<CheckBox>(R.id.agree_register_checkbox)
        agreeWithTermsAndConditionCheckBox.movementMethod = LinkMovementMethod.getInstance()
        agreeWithTermsAndConditionCheckBox.setLinkTextColor(resources.getColor(R.color.colorPrimary))

        firstNameEditText = view.findViewById(R.id.register_first_name)
        lastNameEditText = view.findViewById(R.id.register_last_name)
        emailEditText = view.findViewById(R.id.register_email)
        passwordEditText = view.findViewById(R.id.register_password)
        confirmPasswordEditText = view.findViewById(R.id.register_confirm_password)

        registerButton = view.findViewById(R.id.register_btn)
        registerButton.setOnClickListener {
            progressDialog.show()
            validator.validate()
        }

        registerFragmentRootView = view.findViewById(R.id.register_fragment_root)

        val goToLoginTextView = view.findViewById<TextView>(R.id.go_to_login_tv)

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
        val firstName = firstNameEditText.getTrimmedStringValue()
        val lastName = lastNameEditText.getTrimmedStringValue()
        val email = emailEditText.getTrimmedStringValue()
        val password = passwordEditText.getStringValue()

        val user = User(
            firstName,
            lastName,
            null,
            null,
            null,
            email,
            null
        )

        Toast.makeText(activity, "Validation Successful", Toast.LENGTH_LONG).show()

        lifecycleScope.launch {
            val authResponse = authViewModel.registerUser(email, password, user)

            when (authResponse) {
                is RegisterResponse.Success -> {
                    progressDialog.dismiss()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
                is RegisterResponse.Failure -> {
                    progressDialog.dismiss()
                    Snackbar.make(registerFragmentRootView, authResponse.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>) {
        progressDialog.dismiss()

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