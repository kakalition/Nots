package com.daggery.features.dashboard.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.daggery.features.dashboard.databinding.FragmentDashboardBinding
import com.daggery.features.dashboard.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    private var _viewBinding: FragmentDashboardBinding? = null
    private val viewBinding get() = _viewBinding!!

    val viewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _viewBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}