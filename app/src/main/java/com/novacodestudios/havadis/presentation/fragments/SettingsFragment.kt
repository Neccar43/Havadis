package com.novacodestudios.havadis.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.lifecycle.lifecycleScope
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.databinding.FragmentSettingsBinding
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.presentation.MainViewModel
import com.novacodestudios.havadis.presentation.base.BaseFragment
import com.novacodestudios.havadis.util.Country
import com.novacodestudios.havadis.util.Language
import com.novacodestudios.havadis.util.capitalizeFirstLetter
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

        setCountryMenu()
        setLanguageMenu()

    }

    private fun setCountryMenu(){
        val countryMenu=binding.tvCountry
        val listPopupWindow = ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle)
        listPopupWindow.anchorView = countryMenu
        val items = Country.entries.map { it.name.replace("_", " ").capitalizeFirstLetter() }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, items)
        listPopupWindow.setAdapter(adapter)

        lifecycleScope.launch {
            viewModel.getCountry().collectLatest {
                it?.let { country->
                    countryMenu.text=country.name.replace("_", " ").capitalizeFirstLetter()
                }
            }
        }

        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, id: Long ->
            val selectedCountry = items[position]
            Log.d(TAG, "menu: $selectedCountry")
            viewModel.setCountry(Country.entries[position])
            countryMenu.text= selectedCountry
            listPopupWindow.dismiss()
        }

        countryMenu.setOnClickListener { listPopupWindow.show() }
    }

    private fun setLanguageMenu(){
        val languageMenu=binding.tvLanguage
        val listPopupWindow = ListPopupWindow(requireContext(), null, androidx.appcompat.R.attr.listPopupWindowStyle)
        listPopupWindow.anchorView = languageMenu
        val items = Language.entries.map { it.name.replace("_", " ").capitalizeFirstLetter() }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, items)
        listPopupWindow.setAdapter(adapter)

        lifecycleScope.launch {
            viewModel.getLanguage().collectLatest {
                if (it != null) {
                    languageMenu.text=it.name.replace("_", " ").capitalizeFirstLetter()
                }
            }
        }

        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, id: Long ->
            val selectedLanguage = items[position]
            Log.d(TAG, "setLanguageMenu: $selectedLanguage")
            viewModel.setLanguage(Language.entries[position])
            languageMenu.text= selectedLanguage
            listPopupWindow.dismiss()
        }

        languageMenu.setOnClickListener { listPopupWindow.show() }
    }

    companion object{
        private const val TAG="SettingsFragment"
    }
}