package me.codeenzyme.welearn.view.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.Password
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.model.LoginResponse
import me.codeenzyme.welearn.utils.getStringValue
import me.codeenzyme.welearn.utils.getTrimmedStringValue
import me.codeenzyme.welearn.view.activities.AuthActivity
import me.codeenzyme.welearn.view.activities.MainActivity
import me.codeenzyme.welearn.viewmodel.AuthViewModel

@AndroidEntryPoint
class LoginFragment() : Fragment(), Validator.ValidationListener {

    val authViewModel by viewModels<AuthViewModel>()

    lateinit var rootView: LinearLayout

    lateinit var validator: Validator

    lateinit var progressDialog: ProgressDialog

    @Email
    lateinit var emailEditText: EditText

    @Password(min = 6)
    lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews(view)

        setUpValidation()
    }

    private fun setUpViews(view: View) {
        rootView = view.findViewById(R.id.login_root_view)

        emailEditText = view.findViewById(R.id.login_email)
        passwordEditText = view.findViewById(R.id.login_password)

        progressDialog = ProgressDialog(context).apply {
            setCancelable(false)
            setTitle(getString(R.string.logging_in))
            setMessage(getString(R.string.retrieving_account))
        }

        val goToRegisterTextView = view.findViewById<TextView>(R.id.go_to_register_tv)
        goToRegisterTextView.setOnClickListener { _: View? ->
            val viewPager = (activity as AuthActivity).getPager()
            viewPager.setCurrentItem(1, true)
        }

        val loginBtn = view.findViewById<Button>(R.id.login_btn)
        loginBtn.setOnClickListener {
            progressDialog.show()
            validator.validate()
        }
    }

    private fun setUpValidation() {
        validator = Validator(this)
        validator.setValidationListener(this)
    }

    override fun onValidationSucceeded() {
        val email = emailEditText.getTrimmedStringValue()
        val password = passwordEditText.getStringValue()
        lifecycleScope.launch {
            val loginResponse = authViewModel.loginUser(email, password)

            when(loginResponse) {
                is LoginResponse.Success -> {
                    progressDialog.dismiss()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
                is LoginResponse.Failure -> {
                    progressDialog.dismiss()
                    Snackbar.make(rootView, loginResponse.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>) {
        progressDialog.dismiss()
        for (error in errors) {
            val view = error.view

            when (view) {
                is EditText -> {
                    view.error = error.getCollatedErrorMessage(activity)
                }
                else -> Toast.makeText(activity, error.getCollatedErrorMessage(context), Toast.LENGTH_SHORT).show()
            }
        }
    }
}