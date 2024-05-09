package com.novacodestudios.havadis.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.databinding.FragmentSearchBinding
import com.novacodestudios.havadis.databinding.FragmentSettingsBinding
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.presentation.MainViewModel
import com.novacodestudios.havadis.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment :BaseFragment<FragmentSettingsBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater,container,false)

    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        lifecycleScope.launch {
            launch {
                viewModel.getWifiOnly().collectLatest {
                    binding.switchImageWifi.isChecked=it
                }
            }
            launch {
                viewModel.getDarkMode().collectLatest {
                    binding.switchDarkMode.isChecked=  it
                }
            }
            launch {
                viewModel.getShowNotification().collectLatest {
                    binding.switchNotification.isChecked=  it
                }
            }
        }



        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "switchDarkMode: $isChecked")
            viewModel.setDarkMode(isChecked)
        }

        binding.switchImageWifi.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "switchImageWifi: $isChecked")
            viewModel.setWifiOnly(isChecked)
        }
        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setShowNotification(isChecked)
        }
    }

    companion object{
        private const val TAG="SettingsFragment"
    }
}