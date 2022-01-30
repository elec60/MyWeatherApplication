package com.mousavi.hashem.weatherapp.presentation.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.mousavi.hashem.weatherapp.R
import com.mousavi.hashem.weatherapp.data.local.Preferences
import com.mousavi.hashem.weatherapp.databinding.FragmentMainBinding
import com.mousavi.hashem.weatherapp.domain.entity.Weather
import com.mousavi.hashem.weatherapp.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainFragment : BaseFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTomorrowWeather()
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

    }

    private fun observers() {
        lifecycleScope.launchWhenStarted {
            viewModel.weather.collectLatest { weather ->
                showData(weather)
            }
        }
    }

    private fun showData(weather: Weather) {
        with(weather) {
            binding.ivIcon.load(icon)
            binding.tvCityName.text = city
            binding.tvTemp.text = theTemp
            binding.tvMinTemp.text = minTemp
            binding.tvMaxTemp.text = maxTemp
            binding.tvWeatherStateName.text = weatherStateName
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        viewModel.getTomorrowWeather()
    }

    override fun onStart() {
        super.onStart()
        activity?.getSharedPreferences(Preferences.NAME_OF_SHARED_PREF, Context.MODE_PRIVATE)
            ?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        activity?.getSharedPreferences(Preferences.NAME_OF_SHARED_PREF, Context.MODE_PRIVATE)
            ?.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}