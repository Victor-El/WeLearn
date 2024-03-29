package me.codeenzyme.welearn.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.databinding.FragmentProfileBinding
import me.codeenzyme.welearn.model.ProfileUpdateResponse
import me.codeenzyme.welearn.model.User
import me.codeenzyme.welearn.model.UserProfileResponse
import me.codeenzyme.welearn.view.activities.AuthActivity
import me.codeenzyme.welearn.view.fragments.dialog.NetworkErrorDialog
import me.codeenzyme.welearn.view.fragments.dialog.ProfileEditDialogFragment
import me.codeenzyme.welearn.viewmodel.AuthViewModel
import me.codeenzyme.welearn.viewmodel.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    private val SELECT_IMAGE_CODE = 1000

    private lateinit var binding: FragmentProfileBinding

    private var photoUri: Uri? = null

    private val authViewModel by viewModels<AuthViewModel>()
    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun showEditDialog(textValue: String, viewTag: String) {
            val editDialog = ProfileEditDialogFragment()
            editDialog.arguments = Bundle().apply {
                putString(ProfileEditDialogFragment.KEY_PROFILE_EDIT, textValue)
                putString(ProfileEditDialogFragment.KEY_VIEW_TAG, viewTag)
            }

            editDialog.setProfileFragmentViews(binding)
            editDialog.show(activity?.supportFragmentManager!!, "Edit Dialog")
        }

        binding.updateProfile.setOnClickListener {
            val uri = photoUri
            val user = User(
                binding.profileFirstName.text.toString(),
                binding.profileLastName.text.toString(),
                binding.profileSchool.text.toString(),
                binding.profileFaculty.text.toString(),
                binding.profileDepartment.text.toString(),
                binding.profileEmail.text.toString(),
                null
            )

            lifecycleScope.launch {
                val btnTxt = binding.updateProfile.text
                binding.updateProfile.run {
                    text = getString(R.string.updating)
                    isEnabled = false
                }
                val res = userProfileViewModel.updateUserProfile(uri, user)
                binding.updateProfile.run {
                    text = btnTxt
                    isEnabled = true
                }
                when (res) {
                    is ProfileUpdateResponse.Success -> {
                        Toast.makeText(context, getString(R.string.profile_updated), Toast.LENGTH_LONG).show()
                    }
                    ProfileUpdateResponse.Failure -> {
                        Toast.makeText(context, getString(R.string.profile_not_updated), Toast.LENGTH_LONG).show()
                    }
                }

            }
        }

        binding.logoutBtn.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(activity, AuthActivity::class.java))
        }

        // set long click listener to edit text content
        binding.run {
            profileFirstName.setOnLongClickListener {
                showEditDialog((it as TextView).text.toString(), ProfileEditDialogFragment.KEY_FIRST_NAME); true
            }

            profileLastName.setOnLongClickListener {
                showEditDialog((it as TextView).text.toString(), ProfileEditDialogFragment.KEY_LAST_NAME); true
            }

            profileSchool.setOnLongClickListener {
                showEditDialog((it as TextView).text.toString(), ProfileEditDialogFragment.KEY_SCHOOL); true
            }

            profileFaculty.setOnLongClickListener {
                showEditDialog((it as TextView).text.toString(), ProfileEditDialogFragment.KEY_FACULTY); true
            }

            profileDepartment.setOnLongClickListener {
                showEditDialog((it as TextView).text.toString(), ProfileEditDialogFragment.KEY_DEPT); true
            }
        }

        // get profile data
        loadData()

        binding.profileImage.setOnLongClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            //intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"

            val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Choose photo").apply { putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent)) }, SELECT_IMAGE_CODE)
            true
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            // binding.root.visibility = View.GONE
            binding.profileShimmerSection.visibility = View.VISIBLE
            val userProfile = userProfileViewModel.getUserProfile()
            binding.profileShimmerSection.visibility = View.GONE

            when (userProfile) {
                is UserProfileResponse.Success -> {
                    Log.d("UserProfileFragment", userProfile.displayName)
                    binding.run {
                        if (userProfile.photoUri != null) {
                            // Picasso.get().load(userProfile.photoUri).into(profileImage)
                            Glide.with(requireContext())
                                .load(userProfile.photoUri)
                                .placeholder(R.drawable.ic_round_person_trans)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .listener(object: RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        profileImage.strokeColor = resources.getColorStateList(R.color.colorPrimary)
                                        profileImage.strokeWidth = resources
                                            .getDimension(R.dimen.profile_image_stroke_width)
                                        return false
                                    }
                                })
                                .into(profileImage)
                        }
                        profileDisplayName.text = userProfile.displayName
                        profileFirstName.text = userProfile.user.firstName
                        profileLastName.text = userProfile.user.lastName
                        profileDepartment.text = userProfile.user.dept
                        profileEmail.text = userProfile.user.email
                        profileFaculty.text = userProfile.user.faculty
                        profileSchool.text = userProfile.user.school
                        if (userProfile.user.subscriptionExpiry == null) {
                            subscriptionView.text = getString(R.string.not_currently_subscribed)
                        } else {
                            subscriptionView.text = SimpleDateFormat
                                .getDateInstance()
                                .format(userProfile.user.subscriptionExpiry.toDate())
                        }
                    }
                }
                UserProfileResponse.Failure -> {
                    Log.d("UserProfileFragment", "Failed")
                    Snackbar.make(binding.root, getString(R.string.profile_not_available), Snackbar.LENGTH_LONG).show()
                    val networkErrorDialog = NetworkErrorDialog()
                    networkErrorDialog.setOnRetryListener(object :
                        NetworkErrorDialog.OnRetryListener {
                        override fun retry() {
                            loadData()
                        }

                    })
                    networkErrorDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_MaterialComponents_Dialog)
                    networkErrorDialog.show(requireActivity().supportFragmentManager, "NED")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("ProfileFragment", "OnActivityResult Called")
        // super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            Log.d("ProfileFragment", "correct request")
            val uri = data?.data
            Log.d("ProfileFragment", uri?.authority)
            photoUri = uri
            Picasso.get().load(uri).into(binding.profileImage)
        }
    }

}