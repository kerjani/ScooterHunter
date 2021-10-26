package com.kernacs.scooterhunter.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.kernacs.scooterhunter.R
import com.kernacs.scooterhunter.base.BaseBottomSheetDialogFragment
import com.kernacs.scooterhunter.databinding.FragmentScooterDetailsBinding
import com.kernacs.scooterhunter.util.toReadableRationale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScooterDetailsFragment : BaseBottomSheetDialogFragment() {

    private val viewModel: ScooterDetailsViewModel by viewModels()

    private lateinit var binding: FragmentScooterDetailsBinding

    override val trackedScreenName: String
        get() = "ScooterDetails"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        downloadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_scooter_details, container, false)
        binding.lifecycleOwner = this
        binding.progressIndicator.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.data.observe(this as LifecycleOwner, {
            binding.scooter = it
            binding.progressIndicator.hide()
        })
        viewModel.isLoading.observe(this as LifecycleOwner, {
            binding.progressIndicator.show()
        })
        viewModel.error.observe(this as LifecycleOwner, { error ->
            error?.let {
                val snack =
                    Snackbar.make(view, error.toReadableRationale(resources), Snackbar.LENGTH_LONG)
                snack.show()
                binding.progressIndicator.hide()
            }
        })
    }

    private fun downloadData() {
        arguments?.let {
            val id = it.getString(ARG_ID) ?: return
            viewModel.loadVehicleData(id)
        }
    }

    companion object {
        const val ARG_ID = "id"
    }

}