package com.daggery.nots.home.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daggery.nots.MainActivity
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
// TODO: Design This Fragment Layout, Maybe Add Options Menu
// TODO: In That Options, Include Change Date to Present Time
// TODO: Add Confirm Button in AddEdit
// TODO: Modularize Mode

class AddViewNoteFragment : Fragment() {

    private var _viewBinding: FragmentAddViewNoteBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory((this.activity?.application as NotsApplication).database)
    }
    private val args: AddViewNoteFragmentArgs by navArgs()

    private lateinit var fragmentUtils: AddViewNoteFragmentUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentAddViewNoteBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).changeToolbarTitle("View")

        fragmentUtils = AddViewNoteFragmentUtils(this, args)

        if(args.uuid.isNotBlank()) {
            fragmentUtils.populateField()
        } else {
            viewBinding.noteDate.text = viewModel.getCurrentDate()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_view_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.reorder_button).isVisible = false
        menu.findItem(R.id.settings_button).isVisible = false
        if(args.uuid.isBlank()) {
            menu.findItem(R.id.confirm_button).isVisible = true
            menu.findItem(R.id.delete_button).isVisible = false
        } else {
            menu.findItem(R.id.confirm_button).isVisible = false
            menu.findItem(R.id.delete_button).isVisible = true
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.confirm_button -> {
                fragmentUtils.onConfirmTapped()
                return true
            }
            R.id.delete_button -> {
                fragmentUtils.onDeleteTapped()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}

class AddViewNoteFragmentUtils(
    private val fragment: AddViewNoteFragment,
    private val args: AddViewNoteFragmentArgs
) {
    val onConfirmTapped = {
        val noteTitle = fragment.viewBinding.noteTitle.text.toString()
        val noteBody = fragment.viewBinding.noteBody.text.toString()

        if(noteTitle.isBlank() && noteBody.isBlank()) {
            showFailToAddSnackBar()
        } else {
            fragment.viewModel.addNote(noteTitle, noteBody)
            fragment.findNavController().navigateUp()
        }
    }

    val onDeleteTapped = {
        MaterialAlertDialogBuilder(fragment.requireContext(), R.style.NotsAlertDialog)
            .setView(R.layout.dialog_delete)
            .setPositiveButton("Delete") { dialog, which ->
                val note = fragment.viewModel.getNote(args.uuid)
                note.observe(fragment.viewLifecycleOwner) {
                    it?.let {
                        Log.d("LOL: getNote", it.toString() )
                        fragment.viewModel.deleteNote(it)
                        fragment.findNavController().navigateUp()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    fun showFailToAddSnackBar() {
        val snackbar = Snackbar.make(
            fragment.viewBinding.addViewNoteRoot,
            "Failed to add note. Fields cannot be blank.",
            2000
        )
        snackbar.show()
    }

    internal fun populateField() {
        val noteLiveData = fragment.viewModel.getNote(args.uuid)
        var note: Note?
        noteLiveData.observe(fragment.viewLifecycleOwner) {
            note = it
            fragment.viewBinding.apply {
                noteTitle.text = Editable.Factory.getInstance().newEditable(note?.noteTitle ?: "")
                noteDate.text = Editable.Factory.getInstance().newEditable(note?.noteDate ?: "")
                noteBody.text = Editable.Factory.getInstance().newEditable(note?.noteBody ?: "")
            }
        }
    }
}