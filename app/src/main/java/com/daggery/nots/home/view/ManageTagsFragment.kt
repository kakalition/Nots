package com.daggery.nots.home.view

import android.os.Bundle
import android.view.ActionMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.daggery.nots.databinding.FragmentManageTagsBinding
import com.daggery.nots.home.adapter.TagListAdapter
import com.daggery.nots.home.utils.ManageTagsFragmentUtils
import com.daggery.nots.home.viewmodel.FilterViewModel
import com.daggery.nots.home.viewmodel.ManageTagsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Show Context Action Mode when Any of list is selected

@AndroidEntryPoint
class ManageTagsFragment : Fragment() {

    private var _viewBinding: FragmentManageTagsBinding? = null
    internal val viewBinding get() = _viewBinding!!

    private val viewModel: ManageTagsViewModel by activityViewModels()

    private var _fragmentUtils: ManageTagsFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    val newTagsDialog = NewTagBottomSheetFragment()
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
                viewModel.checkedTags.collect {
                    if(actionMode == null && it.isNotEmpty()) {
                        actionMode = requireActivity().startActionMode(fragmentUtils.actionModeCallback)
                    } else if(it.isEmpty()) {
                        actionMode?.finish()
                    }
                    actionMode?.title = "${viewModel.checkedTags.value.size} selected"
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.manageTagsList.collect {
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