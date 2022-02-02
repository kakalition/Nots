package com.daggery.nots.home.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.Exception

// TODO: Implement Save Edit Functionality
// TODO: Check Observer
// TODO: Design This Fragment Layout, Maybe Add Options Menu
// TODO: In That Options, Include Change Date to Present Time
// TODO: Add Confirm Button in AddEdit
// TODO: Modularize Mode

@AndroidEntryPoint
class AddViewNoteFragment : Fragment() {

    private var _viewBinding: FragmentAddViewNoteBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels()

    private val args: AddViewNoteFragmentArgs by navArgs()

    private lateinit var fragmentUtils: AddViewNoteFragmentUtils

    private var isNewNote: Boolean? = null
    internal var isEditing: Boolean = false

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

        try {
            isNewNote = args.uuid.isBlank()
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }

        fragmentUtils = AddViewNoteFragmentUtils(this, args)
        fragmentUtils.changeToolbarTitle("View")

        fragmentUtils.populateField(args.uuid)
        if(isNewNote!!) {
            fragmentUtils.editEnvironment()
        } else {
            fragmentUtils.viewEnvironment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_view_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        fragmentUtils.hideThreeDotsMenu(menu)
        if(isNewNote!!) {
            fragmentUtils.menuAddEnvironment(menu)
        } else if(isEditing) {
            fragmentUtils.menuEditEnvironment(menu)
        } else {
            fragmentUtils.menuViewEnvironment(menu)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            // TODO: Differentiate Add and Edit
            R.id.confirm_button -> {
                fragmentUtils.onConfirmTapped()
                return true
            }
            R.id.edit_button -> {
                fragmentUtils.onEditTapped()
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

    val onEditTapped = {
        val noteBody = fragment.viewBinding.noteBody
        fragment.isEditing = true
        fragment.requireActivity().invalidateOptionsMenu()
        editEnvironment()
        fragment.viewBinding.noteBody.requestFocus()
        noteBody.setSelection(noteBody.text?.length ?: 0)
        showKeyboard(noteBody)
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

    internal fun showKeyboard(view: View ) {
        val inputMethodManager = fragment.requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    internal fun changeToolbarTitle(title: String) {
        (fragment.requireActivity() as MainActivity).changeToolbarTitle(title)
    }

    internal fun viewEnvironment() {
        fragment.viewBinding.noteTitle.isEnabled = false
        fragment.viewBinding.noteTitle.setTextColor(fragment.resources.getColor(R.color.white_surface, null))
        fragment.viewBinding.noteBody.isEnabled = false
        fragment.viewBinding.noteBody.setTextColor(fragment.resources.getColor(R.color.white_surface, null))
    }

    internal fun editEnvironment() {
        fragment.viewBinding.noteTitle.isEnabled = true
        fragment.viewBinding.noteBody.isEnabled = true
    }

    internal fun menuAddEnvironment(menu: Menu) {
        changeToolbarTitle("New Note")
        menu.findItem(R.id.confirm_button).isVisible = true
        menu.findItem(R.id.edit_button).isVisible = false
        menu.findItem(R.id.delete_button).isVisible = false
    }

    internal fun menuViewEnvironment(menu: Menu) {
        changeToolbarTitle("View")
        menu.findItem(R.id.confirm_button).isVisible = false
        menu.findItem(R.id.edit_button).isVisible = true
        menu.findItem(R.id.delete_button).isVisible = true
    }

    internal fun menuEditEnvironment(menu: Menu) {
        changeToolbarTitle("Edit")
        menu.findItem(R.id.confirm_button).isVisible = true
        menu.findItem(R.id.edit_button).isVisible = false
        menu.findItem(R.id.delete_button).isVisible = false

    }

    internal fun hideThreeDotsMenu(menu: Menu) {
        menu.findItem(R.id.reorder_button).isVisible = false
        menu.findItem(R.id.settings_button).isVisible = false
    }


    fun showFailToAddSnackBar() {
        val snackbar = Snackbar.make(
            fragment.viewBinding.addViewNoteRoot,
            "Failed to add note. Fields cannot be blank.",
            2000
        )
        snackbar.show()
    }

    internal fun populateField(uuid: String) {
        if(uuid.isBlank()) {
            val note = fragment.viewModel.getNewNote()
            fragment.viewBinding.apply {
                noteTitle.text = Editable.Factory.getInstance().newEditable(note.noteTitle)
                noteDate.text = Editable.Factory.getInstance().newEditable(note.noteDate)
                noteBody.text = Editable.Factory.getInstance().newEditable(note.noteBody)
            }
        } else {
            val noteLiveData = fragment.viewModel.getNote(uuid)
            noteLiveData.observe(fragment.viewLifecycleOwner) {
                fragment.viewBinding.apply {
                    noteTitle.text = Editable.Factory.getInstance().newEditable(it?.noteTitle ?: "")
                    noteDate.text = Editable.Factory.getInstance().newEditable(it?.noteDate ?: "")
                    noteBody.text = Editable.Factory.getInstance().newEditable(it?.noteBody ?: "")
                }
            }
        }
    }
}