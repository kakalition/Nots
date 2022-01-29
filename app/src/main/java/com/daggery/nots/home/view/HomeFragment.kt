package com.daggery.nots.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.daggery.nots.MainActivity
import com.daggery.nots.NotsApplication
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (this.activity?.application as NotsApplication).database
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
