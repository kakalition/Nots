package com.daggery.nots.home.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainActivity
import com.daggery.nots.NotsApplication
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentAddEditNoteBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar

// TODO: Implement Save Edit Functionality
// TODO: Add save action button

class AddEditNoteFragment : Fragment() {

    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!

    private var note: LiveData<Note>? = null

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (this.activity?.application as NotsApplication).database
        )
    }

    private val onConfirmTapped = {
        viewModel.addNote(
            binding.noteTitle.text.toString(),
            binding.noteBody.text.toString()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_edit_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.confirm_button -> {
                onConfirmTapped()
                findNavController().navigateUp()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}