package com.daggery.features.noteeditor.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.daggery.domain.entities.NoteData
import com.daggery.features.noteeditor.R
import com.daggery.features.noteeditor.data.NoteValidity
import com.daggery.features.noteeditor.databinding.FragmentAddViewNoteBinding
import com.daggery.features.noteeditor.viewmodel.NoteEditorViewModel
import com.daggery.sharedassets.data.BundleKeys
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// TODO: Handle Receive Args

@AndroidEntryPoint
class AddViewNoteFragment : Fragment() {

    private var _viewBinding: FragmentAddViewNoteBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val editorViewModel: NoteEditorViewModel by activityViewModels()

    private var _assignTagsSheetFragment: AssignTagsSheetFragment? = null
    private val assignTagsBottomSheetFragment get() = _assignTagsSheetFragment!!

    private val editableFactory = Editable.Factory()

    private val args = object {
        lateinit var uuid: String
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            saveNote()
        }
    }

    private val updateTagsCallback: (newTags: List<String>) -> Unit = {
        editorViewModel.noteCache.updateCache(tags = it)
        bindsChips(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Retrieves Arguments
        with(args) {
            uuid = arguments?.getString(BundleKeys.UUID) ?: ""
        }

        _viewBinding = FragmentAddViewNoteBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _assignTagsSheetFragment = AssignTagsSheetFragment(updateTagsCallback)

        bindsToolbar()

        Log.d("LOL UUID AddView", args.uuid)

        if (args.uuid.isBlank()) addEnvironment()
        viewBinding.customLinearLayout.setCallback(hideKeyboard, clearNoteTypingFocus)
        viewBinding.noteTitle.addTextChangedListener(titleTextWatcher)
        viewBinding.emptySpace.setOnClickListener(onEmptySpaceClickListener)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED, fieldsBinder)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        editorViewModel.noteCache.updateCache(
            title = viewBinding.noteTitle.text.toString(),
            body = viewBinding.noteBody.text.toString(),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editorViewModel.noteCache.deleteNoteCache()
        _viewBinding = null
    }

/*
    private fun getMaterialTransformTransition(): MaterialContainerTransform {
        return MaterialContainerTransform().apply {
            duration = 700
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(
                MaterialColors.getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorSurface,
                    Color.parseColor("#FF212121")
                )
            )
            drawingViewId = R.id.fragment_container_view
        }
    }
*/

    private fun bindsToolbar() {
        viewBinding.addViewToolbarBinding.toolbar.apply {
            inflateMenu(R.menu.menu_add_view_fragment)
            setNavigationIcon(com.daggery.sharedassets.R.drawable.ic_back)
            setNavigationOnClickListener(navigationClickListener)
            setOnMenuItemClickListener(onMenuItemClickListener)
        }
    }

    private fun bindsChips(chipsName: List<String>) {
        if(chipsName.isEmpty()) {
            viewBinding.chipGroup.visibility = View.GONE
        } else {
            viewBinding.chipGroup.visibility = View.VISIBLE
            viewBinding.chipGroup.removeAllViews()
            chipsName.forEach { noteTag ->
                val chip = layoutInflater.inflate(R.layout.chip_note_item, viewBinding.chipGroup, false) as Chip
                chip.apply {
                    text = noteTag
                    isCheckable = false
                    isClickable = false
                    isFocusable = false
                }
                // TODO: Check this behavior
                // TODO: Ensure when checking, chipgroup layout doesn't change
                // chip.ensureAccessibleTouchTarget(48)
                viewBinding.chipGroup.addView(chip)
            }
        }
    }

    private fun bindsFields(note: NoteData?) {
        with(viewBinding) {
            noteTitle.text = editableFactory.newEditable(note?.noteTitle ?: "")
            noteDate.text = editorViewModel.noteDateUtils.getParsedDate(
                note?.noteDate ?: editorViewModel.noteDateUtils.getRawCurrentDate()
            )
            noteBody.text = editableFactory.newEditable(note?.noteBody ?: "")
            bindsChips(note?.noteTags ?: listOf())
        }
    }

    private val fieldsBinder: suspend CoroutineScope.() -> Unit = {
        val noteData: NoteData?

        if (editorViewModel.noteCache.value == null) {
            if (args.uuid.isNotBlank()) {
                noteData = editorViewModel.getNote(args.uuid)
            } else {
                noteData = editorViewModel.getBlankNote()
            }
        } else {
            noteData = editorViewModel.noteCache.value
        }

        noteData?.let {
            editorViewModel.noteCache.saveNoteCache(it)
        }

        bindsFields(noteData)
    }

    private val onEmptySpaceClickListener = { _: View ->
        viewBinding.noteBody.setSelection(viewBinding.noteBody.length())
        viewBinding.noteBody.requestFocus()
        showKeyboard(viewBinding.noteBody)
    }

    private fun showKeyboard(view: View) {
        val inputMethodManager = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    private val hideKeyboard = { view: View ->
        val inputMethodManager = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    private val clearNoteTypingFocus = {
        with(viewBinding) {
            noteTitle.clearFocus()
            noteBody.clearFocus()
        }
    }

    private fun addEnvironment() {
        viewBinding.apply {
            noteTitle.requestFocus()
            showKeyboard(noteTitle)
        }
    }

    private val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when (item.itemId) {
            R.id.delete_button -> {
                if(args.uuid.isBlank()) { findNavController().navigateUp() }
                else {
                    editorViewModel.noteCache.value?.let {
                        editorViewModel.deleteNote(it)
                    }
                    findNavController().navigateUp()
                }
                true
            }
            R.id.assign_tags -> {
                assignTagsBottomSheetFragment.assignTagNameList(editorViewModel.noteCache.value?.noteTags ?: listOf())
                assignTagsBottomSheetFragment.show(
                    parentFragmentManager,
                    AssignTagsSheetFragment.TAG
                )
                true
            }
            else -> false
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(viewBinding.root, text, Snackbar.LENGTH_SHORT).show()
    }


    private val navigationClickListener: (View) -> Unit = {
        saveNote()
    }

    fun saveNote() {
        val newNote = editorViewModel.noteCache.value?.copy(
            noteTitle = viewBinding.noteTitle.text.toString(),
            noteBody = viewBinding.noteBody.text.toString()
        )

        Log.d("LOL newnote", newNote.toString())

        var validity: NoteValidity? = null

        newNote?.let {
            validity = editorViewModel.assertNoteValidity(newNote)
        }

        Log.d("LOL validity", validity.toString())

        newNote?.let {
            when(editorViewModel.assertNoteValidity(it)) {
                NoteValidity.VALID -> {
                    if (args.uuid.isNotBlank()) { editorViewModel.updateNote(it) }
                    else { editorViewModel.addNote(it) }
                    editorViewModel.noteCache.deleteNoteCache()
                    findNavController().navigateUp()
                }
                NoteValidity.TITLE_EMPTY -> {
                    showSnackBar("Title is empty")
                }
                NoteValidity.BODY_EMPTY -> {
                    showSnackBar("Body is empty")
                }
                NoteValidity.TITLE_BODY_EMPTY -> {
                    if (args.uuid.isNotBlank()) { showSnackBar("Title and body is empty") }
                    else {
                        editorViewModel.noteCache.deleteNoteCache()
                        findNavController().navigateUp()
                    }
                }
                NoteValidity.UNKNOWN -> { findNavController().navigateUp() }
            }
        }
    }


    private val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        // Focus to note body when pressing enter while text line is two
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.count { it == '\n' } == 1) {
                viewBinding.noteBody.setSelection(viewBinding.noteBody.length())
                viewBinding.noteBody.requestFocus()
            }
        }

        // Remove last blank newline
        override fun afterTextChanged(s: Editable?) {
            if (s?.count { it == '\n' } == 1) {
                s.delete(s.lastIndexOf('\n'), s.length)
            }
        }
    }
}