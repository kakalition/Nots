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
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

data class UneditedNote(
    val noteTitle: String,
    val noteBody: String
)

@AndroidEntryPoint
class AddViewNoteFragment : Fragment() {

    private var _viewBinding: FragmentAddViewNoteBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: AddViewNoteViewModel by activityViewModels()

    private val args: AddViewNoteFragmentArgs by navArgs()

    private var _fragmentUtils: AddViewNoteFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

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
        _noteUtils = NoteUtils(this)
        isNewNote = args.uuid.isBlank()

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
            viewBinding.noteBody.requestFocus()
            viewBinding.noteBody.setSelection(viewBinding.noteBody.length())
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