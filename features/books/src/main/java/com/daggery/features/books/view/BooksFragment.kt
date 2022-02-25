package com.daggery.features.books.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.daggery.features.books.databinding.BooksFragmentBinding
import com.daggery.features.books.viewmodel.BooksViewModel

class BooksFragment : Fragment() {

    private var _viewBinding: BooksFragmentBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: BooksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = BooksFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _viewBinding = null
    }
}