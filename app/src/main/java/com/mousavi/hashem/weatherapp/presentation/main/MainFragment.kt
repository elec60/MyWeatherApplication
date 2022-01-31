package com.mousavi.hashem.weatherapp.presentation.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.mousavi.hashem.weatherapp.R
import com.mousavi.hashem.weatherapp.data.local.Preferences
import com.mousavi.hashem.weatherapp.databinding.FragmentMainBinding
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import com.mousavi.hashem.weatherapp.presentation.BaseFragment
import com.mousavi.hashem.weatherapp.util.dateFormat
import com.mousavi.hashem.weatherapp.util.showGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainFragment : BaseFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.getSharedPreferences(Preferences.NAME_OF_SHARED_PREF, Context.MODE_PRIVATE)
            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        observers()
    }

    private fun listeners() {
        binding.ibSettings.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_citiesFragment)
        }

        binding.tvCityName.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_citiesFragment)
        }

    }

    private fun observers() {
        lifecycleScope.launchWhenStarted {
            viewModel.weatherState.collectLatest { state ->
                binding.loading.root.showGone(state.loading)
                showData(state.weather)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                checkError(error = it)
                viewModel.resetError()
            }
        }
    }

    private fun checkError(error: String) {
        if (error.isNotBlank()) {
            AlertDialog.Builder(context ?: return)
                .setCancelable(false)
                .setMessage(error)
                .setPositiveButton(getString(R.string.retry)) { _, _ ->
                    viewModel.getTomorrowWeather()
                }
                .setNeutralButton(getString(R.string.exit)) { _, _ ->
                    activity?.finish()
                }.show()
        }
    }

    private fun showData(weather: Weather) {
        if (weather == Weather.Default) return
        with(weather) {
            binding.tvDate.text = dateFormat(applicableDate)
            binding.ivIcon.load(icon)
            binding.tvCityName.text = city
            binding.tvTemp.text = theTemp
            binding.tvMinTemp.text = minTemp
            binding.tvMaxTemp.text = maxTemp
            binding.tvWeatherStateName.text = weatherStateName
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == Preferences.KEY_CITY_NAME) {
            viewModel.getTomorrowWeather()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.getSharedPreferences(Preferences.NAME_OF_SHARED_PREF, Context.MODE_PRIVATE)
            ?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}