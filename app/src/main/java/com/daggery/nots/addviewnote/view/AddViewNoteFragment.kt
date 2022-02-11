package com.daggery.nots.addviewnote.view

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.ResourceCursorAdapter
import android.widget.ScrollView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.daggery.nots.R
import com.daggery.nots.addviewnote.utils.AddViewNoteFragmentUtils
import com.daggery.nots.addviewnote.viewmodel.AddViewNoteViewModel
import com.daggery.nots.databinding.FragmentAddViewNoteBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._com_daggery_nots_addviewnote_viewmodel_AddViewNoteViewModel_HiltModules_BindsModule


// TODO: Handle Long Text Body, possibly solved by using linear layout

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

    private var screenHeight: Int = 0

    private var _fragmentUtils: AddViewNoteFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    private var _editableFactory: Editable.Factory? = null
    internal val editableFactory get() = _editableFactory!!

    private var isNewNote: Boolean? = null
    internal var isEditing: Boolean = false

    internal lateinit var uneditedNote: UneditedNote

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(isEditing) { fragmentUtils.showOnRevertConfirmation(uneditedNote) }
            else {
                this.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
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

        screenHeight = Resources.getSystem().displayMetrics.heightPixels

        _fragmentUtils = AddViewNoteFragmentUtils(this, args)
        _editableFactory = Editable.Factory()
        isNewNote = args.uuid.isBlank()

        with(fragmentUtils) {
            bindsToolbar()
            bindsFields(args.uuid)

            when {
                isNewNote == true -> { addEnvironment() }
                isEditing -> { editEnvironment() }
                else -> { viewEnvironment() }
            }
        }

        // TODO: Clean this things up
        // TODO: Maybe I can use invisible view that capture touch and focus on edit text
        var availHeight = 0f
        viewBinding.appBarFrame.post {
            availHeight += viewBinding.noteBody.y
        }

        viewBinding.noteBody.post {
            availHeight += viewBinding.noteBody.y
            viewBinding.noteBody.minHeight = (screenHeight - availHeight).toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
        _editableFactory = null
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