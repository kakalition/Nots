package com.daggery.nots.addviewnote.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.daggery.nots.R
import com.daggery.nots.addviewnote.utils.AddViewNoteFragmentUtils
import com.daggery.nots.addviewnote.utils.NoteUtils
import com.daggery.nots.addviewnote.viewmodel.AddViewNoteViewModel
import com.daggery.nots.databinding.FragmentAddViewNoteBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddViewNoteFragment : Fragment() {

    private var _viewBinding: FragmentAddViewNoteBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: AddViewNoteViewModel by activityViewModels()

    internal val args: AddViewNoteFragmentArgs by navArgs()

    private var _fragmentUtils: AddViewNoteFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    private var _assignTagsBottomSheetFragment: AssignTagsBottomSheetFragment? = null
    internal val assignTagsBottomSheetFragment get() = _assignTagsBottomSheetFragment!!

    private var _noteUtils: NoteUtils? = null
    private val noteUtils get() = _noteUtils!!

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            fragmentUtils.saveNote()
        }
    }

    private val updateTagsCallback: (newTags: List<String>) -> Unit = {
        Log.d("LOL updateTags", it.toString())
        viewModel.updateCache(tags = it)
        noteUtils.bindsChips(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
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
        //Timber.d(viewModel.noteCache.toString())
        Timber.d(args.uuid.toString())
        Log.d("LOL", args.uuid)

        _fragmentUtils = AddViewNoteFragmentUtils(this)
        _noteUtils = NoteUtils(this)

        _assignTagsBottomSheetFragment = AssignTagsBottomSheetFragment(updateTagsCallback)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(viewModel) {
                    if (noteCache == null) {
                       if (args.uuid.isNotBlank()) {
                            getNote(args.uuid).collect {
                                saveNoteCache(it)
                                noteUtils.bindsFields(noteCache, "args")
                            }
                        } else {
                            getBlankNote().collect {
                                saveNoteCache(it)
                                noteUtils.bindsFields(noteCache, "empty")
                            }
                        }
                    } else {
                        noteUtils.bindsFields(noteCache, "cache")
                    }
                }
            }
        }

        viewBinding.customLinearLayout.setFragmentUtils(fragmentUtils)

        with(fragmentUtils) {
            bindsToolbar()
            if (args.uuid.isBlank()) addEnvironment()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.updateCache(
            title = viewBinding.noteTitle.text.toString(),
            body = viewBinding.noteBody.text.toString(),
        )
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