package me.codeenzyme.welearn.view.fragments.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.databinding.DialogFragmentProfileEditBinding
import me.codeenzyme.welearn.databinding.FragmentProfileBinding
import me.codeenzyme.welearn.utils.getTrimmedStringValue
import me.codeenzyme.welearn.viewmodel.UserProfileViewModel

class ProfileEditDialogFragment(): DialogFragment() {

    lateinit var fragmentProfileBinding: FragmentProfileBinding

    companion object {
        const val KEY_PROFILE_EDIT = "PROFILE_EDIT"
        const val KEY_VIEW_TAG = "VIEW_TAG"

        const val KEY_FIRST_NAME = "FIRST_NAME"
        const val KEY_LAST_NAME = "LAST_NAME"
        const val KEY_EMAIL = "EMAIL"
        const val KEY_SCHOOL = "SCHOOL"
        const val KEY_FACULTY = "FACULTY"
        const val KEY_DEPT = "DEPT"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentProfileEditBinding.inflate(layoutInflater)
        val viewTag = requireArguments().getString(KEY_VIEW_TAG)
        binding.editProfile.setText(requireArguments().getString(KEY_PROFILE_EDIT))
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setView(binding.root)
            setPositiveButton("Ok") { dialog, _ ->
                when (viewTag) {
                    KEY_FIRST_NAME -> {
                        fragmentProfileBinding.profileFirstName.text = binding.editProfile.getTrimmedStringValue()
                    }
                    KEY_LAST_NAME -> {
                        fragmentProfileBinding.profileLastName.text = binding.editProfile.getTrimmedStringValue()
                    }
                    KEY_EMAIL -> {
                        fragmentProfileBinding.profileEmail.text = binding.editProfile.getTrimmedStringValue()
                    }
                    KEY_DEPT -> {
                        fragmentProfileBinding.profileDepartment.text = binding.editProfile.getTrimmedStringValue()
                    }
                    KEY_FACULTY -> {
                        fragmentProfileBinding.profileFaculty.text = binding.editProfile.getTrimmedStringValue()
                    }
                    KEY_SCHOOL -> {
                        fragmentProfileBinding.profileSchool.text = binding.editProfile.getTrimmedStringValue()
                    }
                }
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }.create()

        return dialog
    }

    fun setProfileFragmentViews(binding: FragmentProfileBinding) {
        fragmentProfileBinding = binding
    }
}