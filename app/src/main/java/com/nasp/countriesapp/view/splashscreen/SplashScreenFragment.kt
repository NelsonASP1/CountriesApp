package com.nasp.countriesapp.view.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nasp.countriesapp.R
import com.nasp.countriesapp.databinding.FragmentSplashscreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var binding: FragmentSplashscreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSplashscreenBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoadingDone.observe(viewLifecycleOwner) {
            when (it) {
                SplashScreenStatus.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.animationView.visibility  = View.VISIBLE
                }
                SplashScreenStatus.SUCCESS -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    binding.animationView.visibility  = View.INVISIBLE
                    binding.progressCircular.setProgress(100, true)
                    openCountries()
                }
                SplashScreenStatus.ERROR -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    binding.animationView.visibility  = View.INVISIBLE
                    AlertDialog.Builder(requireContext())
                        .setCancelable(false)
                        .setTitle(R.string.dialog_loading_error_title)
                        .setMessage(R.string.dialog_loading_error_local_data_message)
                        .setPositiveButton(R.string.start_anyway_label) { dialog, _ ->
                            dialog.dismiss()

                            openCountries()
                        }
                        .setNegativeButton(R.string.retry_label) { dialog, _ ->
                            dialog.dismiss()

                            loadCountries()
                        }.show()
                }
                SplashScreenStatus.FAILED_BUT_NO_COUNTRIES -> {
                    binding.progressCircular.visibility = View.INVISIBLE
                    binding.animationView.visibility  = View.INVISIBLE
                    AlertDialog.Builder(requireContext())
                        .setCancelable(false)
                        .setTitle(R.string.dialog_loading_error_title)
                        .setMessage(R.string.dialog_loading_error_message)
                        .setPositiveButton(R.string.retry_label) { dialog, _ ->
                            dialog.dismiss()

                            loadCountries()
                        }
                        .setNegativeButton(R.string.exit_app_label) { dialog, _ ->
                            dialog.dismiss()

                            activity?.finish()
                        }.show()
                }
                else -> error("Enum null because of Java")
            }
        }

        loadCountries()
    }

    private fun loadCountries() {
        binding.progressCircular.visibility = View.VISIBLE
        viewModel.loadCountries()
    }

    private fun openCountries() {
        findNavController().navigate(SplashScreenFragmentDirections
            .actionSplashscreenFragmentToCountryFragment() )
    }
}