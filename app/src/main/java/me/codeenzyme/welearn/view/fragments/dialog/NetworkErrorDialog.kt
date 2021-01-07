package me.codeenzyme.welearn.view.fragments.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import me.codeenzyme.welearn.databinding.DialogNoNetworkLayoutBinding

class NetworkErrorDialog: DialogFragment() {

    private lateinit var binding: DialogNoNetworkLayoutBinding

    private var onRetry: OnRetryListener? = null

    fun setOnRetryListener(listener: OnRetryListener) {
        onRetry = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogNoNetworkLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.networkRetryBtn.setOnClickListener {
            onRetry?.retry()
            dismiss()
        }
    }



    interface OnRetryListener {
        fun retry()
    }

}