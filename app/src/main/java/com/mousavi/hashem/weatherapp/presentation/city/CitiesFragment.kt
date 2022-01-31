package com.mousavi.hashem.weatherapp.presentation.city

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mousavi.hashem.weatherapp.R
import com.mousavi.hashem.weatherapp.databinding.FragmentCitiesBinding
import com.mousavi.hashem.weatherapp.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CitiesFragment : BaseFragment() {

    private var _binding: FragmentCitiesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CitiesViewModel by viewModels()


    private lateinit var adapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cities = context!!.resources.getStringArray(R.array.cities)

        adapter = CitiesAdapter(cities.toList()) { city ->
            viewModel.setCity(city)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCitiesBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter
        observers()
        startAnimation()
        listeners()
    }

    private fun listeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun startAnimation() {
        val animator = ObjectAnimator.ofFloat(binding.recycler, "translationX", -50f, 0f).apply {
            duration = 500L
        }
        animator.start()
    }

    private fun observers() {
        lifecycleScope.launchWhenStarted {
            viewModel.citySet.collectLatest {
                if (it) {
                    onBackPressed()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        findNavController().popBackStack()
    }

}