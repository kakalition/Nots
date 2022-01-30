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
import com.daggery.nots.databinding.FragmentAddEditNoteBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: Implement Save Edit Functionality
// TODO: Check Observer
// TODO: Implement Edit Action Button
// TODO: Assert Title and Body Cannot Be Empty

class AddViewNoteFragment : Fragment() {

    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!

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

/*
    private fun setAddEditEnvironment() {
        if(args.uuid.isNotBlank()) populateField()
        binding.apply {
            noteTitle.isEnabled = true
            noteBody.isEnabled = true
            noteDate.visibility = View.GONE
        }
    }

    private fun setReadEnvironment() {
        populateField()
        binding.apply {
            noteTitle.hint = ""
            noteTitle.isEnabled = false
            noteTitle.setTextColor((resources.getColor(R.color.white_surface, null)))
            noteBody.hint = ""
            noteBody.isEnabled = false
            noteBody.setTextColor((resources.getColor(R.color.white_surface, null)))
            noteDate.visibility = View.VISIBLE
        }
    }
*/

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
        _binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logArgs()

        if(args.uuid.isNotBlank()) populateField()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_edit_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if(args.uuid.isBlank()) {
/*
            menu.add(Menu.NONE, R.id.confirm_button, Menu.NONE, "Confirm")
            menu.removeItem(R.id.edit_button)
*/
            menu.findItem(R.id.confirm_button).setVisible(true)
            menu.findItem(R.id.delete_button).setVisible(false)
        } else {
/*
            menu.add(Menu.NONE, R.id.edit_button, Menu.NONE, "Edit")
            menu.removeItem(R.id.confirm_button)
*/
            menu.findItem(R.id.confirm_button).setVisible(false)
            menu.findItem(R.id.delete_button).setVisible(true)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.confirm_button -> {
                onConfirmTapped()
                findNavController().navigateUp()
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