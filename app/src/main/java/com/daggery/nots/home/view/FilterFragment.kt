package com.daggery.nots.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private var _viewBinding: FragmentFilterBinding? = null
    private val viewBinding = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentFilterBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

}