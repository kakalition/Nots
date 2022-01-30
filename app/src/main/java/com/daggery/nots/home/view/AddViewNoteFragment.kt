package com.daggery.nots.home.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daggery.nots.NotsApplication
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentAddViewNoteBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

// TODO: Implement Save Edit Functionality
// TODO: Check Observer
// TODO: Implement Edit Action Button
// TODO: Assert Title and Body Cannot Be Empty

class AddViewNoteFragment : Fragment() {

    private var _binding: FragmentAddViewNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (this.activity?.application as NotsApplication).database
        )
    }


    private val onConfirmTapped = {
        val noteTitle = binding.noteTitle.text.toString()
        val noteBody = binding.noteBody.text.toString()

        if(noteTitle.isBlank() && noteBody.isBlank()) {
            showFailToAddSnackBar()
        } else {
            viewModel.addNote(noteTitle, noteBody)
            findNavController().navigateUp()
        }
    }

    private val onDeleteTapped = {
        MaterialAlertDialogBuilder(requireContext(), R.style.NotsAlertDialog)
            .setView(R.layout.dialog_delete)
            .setPositiveButton("Delete") { dialog, which ->
                val note = viewModel.getNote(args.uuid)
                note.observe(viewLifecycleOwner) {
                    it?.let {
                        Log.d("LOL: getNote", it.toString() )
                        viewModel.deleteNote(it)
                        findNavController().navigateUp()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private val args: AddViewNoteFragmentArgs by navArgs()

    private fun showFailToAddSnackBar() {
        val snackbar = Snackbar.make(
            binding.addViewNoteRoot,
            "Failed to add note. Fields cannot be blank.",
            2000
        )
        snackbar.show()
    }

    private fun populateField() {
        val noteLiveData = viewModel.getNote(args.uuid)
        var note: Note?
        noteLiveData.observe(viewLifecycleOwner) {
            note = it
            binding.apply {
                noteTitle.text = Editable.Factory.getInstance().newEditable(note?.noteTitle ?: "")
                noteDate.text = Editable.Factory.getInstance().newEditable(note?.noteDate ?: "")
                noteBody.text = Editable.Factory.getInstance().newEditable(note?.noteBody ?: "")
            }
        }
    }

    private fun logArgs() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddViewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logArgs()

        if(args.uuid.isNotBlank()) {
            populateField()
        } else {
            binding.noteDate.text = viewModel.getCurrentDate()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_edit_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if(args.uuid.isBlank()) {
            menu.findItem(R.id.confirm_button).setVisible(true)
            menu.findItem(R.id.delete_button).setVisible(false)
        } else {
            menu.findItem(R.id.confirm_button).setVisible(false)
            menu.findItem(R.id.delete_button).setVisible(true)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.confirm_button -> {
                onConfirmTapped()
                return true
            }
            R.id.delete_button -> {
                onDeleteTapped()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}