package com.daggery.nots.home.view

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentAddViewNoteBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.observeOnce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlin.Exception
import com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered

// TODO: Implement Save Edit Functionality
// TODO: Check Observer
// TODO: Design This Fragment Layout, Maybe Add Options Menu
// TODO: In That Options, Include Change Date to Present Time
// TODO: Handle Long Text Body, possibly solved by using linear layout

@AndroidEntryPoint
class AddViewNoteFragment : Fragment() {

    private var _viewBinding: FragmentAddViewNoteBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels()

    private val args: AddViewNoteFragmentArgs by navArgs()

    private lateinit var fragmentUtils: AddViewNoteFragmentUtils

    private var isNewNote: Boolean? = null
    internal var isViewing: Boolean = true

    internal lateinit var originalNote: OriginalNote

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(!isViewing) {
                fragmentUtils.showOnRevertConfirmation(originalNote)
            }
            else {
                this.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 700
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().getColor(R.color.black_surface))
            drawingViewId = R.id.fragment_container_view
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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

        /* Checking UUID Received is Valid or Not
         * If valid, it's existing note, if not it's new note
         */
        try {
            isNewNote = args.uuid.isBlank()
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }

        // Instantiating Fragment Utils Class
        fragmentUtils = AddViewNoteFragmentUtils(this, args)

        // Prepare Toolbar
        viewBinding.toolbarBinding.apply {
            toolbar.inflateMenu(R.menu.menu_add_view_fragment)
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(fragmentUtils.navigationClickListener)
            toolbar.setOnMenuItemClickListener(fragmentUtils.onMenuItemClickListener)
        }

        // Setting Fragment Environment
        fragmentUtils.populateField(args.uuid)
        when {
            isNewNote == true -> {
                fragmentUtils.addEnvironment()
            }
            isViewing -> {
                fragmentUtils.viewEnvironment()
            }
            else -> {
                fragmentUtils.editEnvironment()
            }
        }
    }

}

class AddViewNoteFragmentUtils(
    private val fragment: AddViewNoteFragment,
    private val args: AddViewNoteFragmentArgs
) {
    internal fun showOnRevertConfirmation(originalNote: OriginalNote) {
        val isNoteTitleSame = fragment.viewBinding.noteTitle.text.toString() == originalNote.noteTitle
        val isNoteBodySame = fragment.viewBinding.noteBody.text.toString() == originalNote.noteBody
        if(isNoteTitleSame && isNoteBodySame) {
            revertChanges(originalNote)
        } else {
            MaterialAlertDialogBuilder(
                fragment.requireContext(),
                ThemeOverlay_Material3_MaterialAlertDialog_Centered
            )
                .setView(R.layout.dialog_revert_confirmation)
                .setPositiveButton("Revert") { _, _ ->
                    revertChanges(originalNote)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    val onConfirmTapped = {
        val noteTitle = fragment.viewBinding.noteTitle.text.toString()
        val noteBody = fragment.viewBinding.noteBody.text.toString()

        if (noteTitle.isBlank() && noteBody.isBlank()) {
            showFailToAddSnackBar()
        } else if (args.uuid.isNotBlank()) {
            val noteLiveData = fragment.viewModel.getNote(args.uuid)
            noteLiveData.observeOnce(fragment) {
                val note = it?.copy(noteTitle = noteTitle, noteBody = noteBody)
                Log.d("LOL: note", note?.toString() ?: "null")
                note?.let {
                    fragment.viewModel.updateNote(it)
                    viewEnvironment()
                }
            }
        } else {
            fragment.viewModel.addNote(noteTitle, noteBody)
            fragment.findNavController().navigateUp()
        }
    }

    // TODO: Refactor
    // TODO: Hide Keyboard When Confirm is Tapped
    val onEditTapped = {
        val noteBody = fragment.viewBinding.noteBody
        fragment.isViewing = false
        editEnvironment()

        // Show Keyboard with Pointer at The End of Text
        fragment.viewBinding.noteBody.requestFocus()
        noteBody.setSelection(noteBody.text?.length ?: 0)
        showKeyboard(noteBody)
    }

    val navigationClickListener: (View) -> Unit = { view ->
        if(!fragment.isViewing) {
            viewEnvironment()
            revertChanges(fragment.originalNote)
        }
        else fragment.findNavController().navigateUp()
    }

    val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when(item.itemId) {
            R.id.confirm_button -> {
                onConfirmTapped()
                true
            }
            R.id.edit_button -> {
                onEditTapped()
                true
            }
            R.id.delete_button -> {
                true
            }
            else -> false
        }
    }

    internal fun showKeyboard(view: View ) {
        val inputMethodManager = fragment.requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    internal fun addEnvironment() {
        fragment.viewBinding.toolbarBinding.toolbarTitle.text = "New Note"
        fragment.viewBinding.noteTitle.isEnabled = true
        fragment.viewBinding.noteBody.isEnabled = true
        menuAddEnvironment()
    }

    internal fun viewEnvironment() {
        fragment.isViewing = true
        fragment.viewBinding.toolbarBinding.toolbarTitle.text = "View"
        fragment.viewBinding.noteTitle.isEnabled = false
        fragment.viewBinding.noteBody.isEnabled = false
        menuViewEnvironment()
    }

    internal fun editEnvironment() {
        fragment.isViewing = false
        fragment.viewBinding.toolbarBinding.toolbarTitle.text = "Edit"
        fragment.viewBinding.noteTitle.isEnabled = true
        fragment.viewBinding.noteBody.isEnabled = true
        menuEditEnvironment()
    }

    internal fun menuAddEnvironment() {
        fragment.viewBinding.toolbarBinding.toolbar.menu.apply {
            findItem(R.id.confirm_button).isVisible = true
            findItem(R.id.edit_button).isVisible = false
            findItem(R.id.delete_button).isVisible = false
        }
    }

    internal fun menuViewEnvironment() {
        fragment.viewBinding.toolbarBinding.toolbar.menu.apply {
            findItem(R.id.confirm_button).isVisible = false
            findItem(R.id.edit_button).isVisible = true
            findItem(R.id.delete_button).isVisible = true
        }
    }

    internal fun menuEditEnvironment() {
        fragment.viewBinding.toolbarBinding.toolbar.menu.apply {
            findItem(R.id.confirm_button).isVisible = true
            findItem(R.id.edit_button).isVisible = false
            findItem(R.id.delete_button).isVisible = false
        }
    }

    internal fun revertChanges(originalNote: OriginalNote) {
        fragment.viewBinding.apply {
            noteTitle.text = Editable.Factory.getInstance().newEditable(originalNote.noteTitle)
            noteBody.text = Editable.Factory.getInstance().newEditable(originalNote.noteBody)
        }
        viewEnvironment()
    }

    private fun showFailToAddSnackBar() {
        val snackbar = Snackbar.make(
            fragment.viewBinding.addViewNoteRoot,
            "Failed to add note. Fields cannot be blank.",
            2000
        )
        snackbar.show()
    }

    // TODO: Can Be Optimized
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
                it?.let {
                    fragment.originalNote = OriginalNote(it.noteTitle, it.noteBody)
                }
                fragment.viewBinding.apply {
                    noteTitle.text = Editable.Factory.getInstance().newEditable(it?.noteTitle ?: "")
                    noteDate.text = Editable.Factory.getInstance().newEditable(it?.noteDate ?: "")
                    noteBody.text = Editable.Factory.getInstance().newEditable(it?.noteBody ?: "")
                }
            }
        }
    }
}

data class OriginalNote(
    val noteTitle: String,
    val noteBody: String
)