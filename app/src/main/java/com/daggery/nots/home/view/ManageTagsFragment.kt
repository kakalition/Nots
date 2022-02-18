package com.daggery.nots.home.view

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentManageTagsBinding
import com.daggery.nots.home.adapter.TagListAdapter
import com.daggery.nots.home.utils.ManageTagsFragmentUtils
import com.daggery.nots.home.viewmodel.FilterViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Add Frequently Used Tags and All Tags Section
// TODO: Refactor this fragment into ManageTagsFragment
// TODO: When filter menu button in home fragment is clicked, show modal bottom sheet that shows all filters that user have

@AndroidEntryPoint
class ManageTagsFragment : Fragment() {

    private var _viewBinding: FragmentManageTagsBinding? = null
    internal val viewBinding get() = _viewBinding!!

    private val viewModel: FilterViewModel by activityViewModels()

    private var _fragmentUtils: ManageTagsFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    val newTagsDialog = NewTagsDialogFragment()
    var actionMode: ActionMode? = null

    private var _tagListAdapter: TagListAdapter? = null
    private val tagListAdapter get() = _tagListAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentManageTagsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = ManageTagsFragmentUtils(this)
        _tagListAdapter = TagListAdapter()

        fragmentUtils.bindsToolbar()

        with(viewBinding) {
            tagsRecyclerView.adapter = tagListAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tagList.collect {
                    tagListAdapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
        _tagListAdapter = null
    }
}

// TODO: For Chips

/*
                viewModel.tagList.collect {
                    // Clear all children when list is updated
                    viewBinding.chipGroup.removeAllViews()
                    it.forEach { noteTag ->
                        val chip = layoutInflater.inflate(R.layout.chip_filter, viewBinding.chipGroup, false) as Chip
                        chip.text = noteTag.tagName
                        chip.isChecked = noteTag.checked
                        // TODO: Check this behavior
                        // TODO: Ensure when checking, chipgroup layout doesn't change
                        chip.ensureAccessibleTouchTarget(48)
                        chip.setOnLongClickListener {
                            when(actionMode) {
                                null -> {
                                    actionMode = requireActivity().startActionMode(fragmentUtils.actionModeCallback)
                                    view.isSelected = true
                                    true
                                }
                                else -> false
                            }
                        }
                        viewBinding.chipGroup.addView(chip)
                    }
                }
*/