package com.daggery.nots.home.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.daggery.nots.R
import com.daggery.nots.addviewnote.utils.AddViewNoteFragmentUtils
import com.daggery.nots.databinding.FragmentAddViewNoteBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

// TODO: Check Observer
// TODO: Design This Fragment Layout, Maybe Add Options Menu
// TODO: In That Options, Include Change Date to Present Time
// TODO: Handle Long Text Body, possibly solved by using linear layout

data class UneditedNote(
    val noteTitle: String,
    val noteBody: String
)

@AndroidEntryPoint
class AddViewNoteFragment : Fragment() {

    private var _viewBinding: FragmentAddViewNoteBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels()

    private val args: AddViewNoteFragmentArgs by navArgs()

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

        _fragmentUtils = AddViewNoteFragmentUtils(this, args)
        _editableFactory = Editable.Factory()
        isNewNote = args.uuid.isBlank()

        bindsToolbar()

        // Setting Fragment Environment
        fragmentUtils.populateField(args.uuid)
        when {
            isNewNote == true -> { fragmentUtils.addEnvironment() }
            isEditing -> { fragmentUtils.viewEnvironment() }
            else -> { fragmentUtils.editEnvironment() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
        _editableFactory = null
    }

    private fun bindsToolbar() {
        viewBinding.toolbarBinding.toolbar.apply {
            inflateMenu(R.menu.menu_add_view_fragment)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener(fragmentUtils.navigationClickListener)
            setOnMenuItemClickListener(fragmentUtils.onMenuItemClickListener)
        }
    }

    private fun getMaterialTransformTransition(): MaterialContainerTransform {
        return MaterialContainerTransform().apply {
            duration = 700
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().getColor(R.color.black_surface))
            drawingViewId = R.id.fragment_container_view
        }
    }
}
