package com.daggery.nots.addviewnote.view

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daggery.nots.R
import com.daggery.nots.addviewnote.utils.AddViewNoteFragmentUtils
import com.daggery.nots.addviewnote.utils.NoteUtils
import com.daggery.nots.addviewnote.viewmodel.AddViewNoteViewModel
import com.daggery.nots.databinding.FragmentAddViewNoteBinding
import com.daggery.nots.observeOnce
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddViewNoteFragment : Fragment() {

    private var _viewBinding: FragmentAddViewNoteBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: AddViewNoteViewModel by activityViewModels()

    private val args: AddViewNoteFragmentArgs by navArgs()

    private var _fragmentUtils: AddViewNoteFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    private var _assignTagsBottomSheetFragment: AssignTagsBottomSheetFragment? = null
    internal val assignTagsBottomSheetFragment get() = _assignTagsBottomSheetFragment!!

    private var _noteUtils: NoteUtils? = null
    private val noteUtils get() = _noteUtils!!

    var isNewNote: Boolean? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when (isNewNote) {
                true -> { fragmentUtils.onBackPressedWhenNewNote() }
                false -> {
                    fragmentUtils.updateNoteNavigateUp()
                }
                else -> {
                    this.isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getMaterialTransformTransition()
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

        _fragmentUtils = AddViewNoteFragmentUtils(this, args)
        isNewNote = args.uuid.isBlank()
        _noteUtils = NoteUtils(this)

        _assignTagsBottomSheetFragment = AssignTagsBottomSheetFragment()
        viewModel.getNote(args.uuid).observeOnce(viewLifecycleOwner) {
            assignTagsBottomSheetFragment.assignTagNameList(it?.noteTags ?: listOf())
        }

        viewBinding.customLinearLayout.setFragmentUtils(fragmentUtils)
        noteUtils.bindsFields(args.uuid)

        with(fragmentUtils) {
            bindsToolbar()
            if(isNewNote == true) addEnvironment() else editEnvironment()
        }

        with(viewBinding) {
            noteTitle.setOnFocusChangeListener { _, hasFocus ->
                fragmentUtils.titleHasFocus = hasFocus
            }
            noteTitle.addTextChangedListener(fragmentUtils.titleTextWatcher)

            noteBody.setOnFocusChangeListener { _, hasFocus ->
                fragmentUtils.bodyHasFocus = hasFocus
            }
        }

        viewBinding.emptySpace.setOnClickListener {
            viewBinding.noteBody.setSelection(viewBinding.noteBody.length())
            viewBinding.noteBody.requestFocus()
            fragmentUtils.showKeyboard(viewBinding.noteBody)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
        _noteUtils = null
    }

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
}