package me.codeenzyme.welearn.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import me.codeenzyme.welearn.databinding.FragmentProfileBinding
import me.codeenzyme.welearn.view.activities.AuthActivity
import me.codeenzyme.welearn.viewmodel.AuthViewModel

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    val SELECT_IMAGE_CODE = 1000

    lateinit var binding: FragmentProfileBinding

    val authViewModel by viewModels<AuthViewModel>()

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
        binding.logoutBtn.setOnClickListener {
            authViewModel.logout()
            startActivity(Intent(activity, AuthActivity::class.java))
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("ProfileFragment", "OnActivityResult Called")
        // super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            Log.d("ProfileFragment", "correect request")
            val uri = data?.data
            Log.d("ProfileFragment", uri?.authority)
            Picasso.get().load(uri).into(binding.profileImage)
        }
    }

}