package com.daggery.nots.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import com.daggery.nots.NotsApplication
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentAddEditNoteBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory

// TODO: Implement Save Edit Functionality

class AddEditNoteFragment : Fragment() {

    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!

    private var note: LiveData<Note>? = null

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (this.activity?.application as NotsApplication).database
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noteBodyDate.visibility = View.GONE
    }

}