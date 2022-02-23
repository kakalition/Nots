package com.daggery.nots.home.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentManageTagsBinding
import com.daggery.nots.home.adapter.TagListAdapter
import com.daggery.nots.managetags.utils.ManageTagsFragmentUtils
import com.daggery.nots.home.viewmodel.ManageTagsViewModel
import com.daggery.nots.managetags.view.AddEditTagBottomSheetFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

// TODO: Show Context Action Mode when Any of list is selected

@AndroidEntryPoint
class ManageTagsFragment : Fragment() {

    private var _viewBinding: FragmentManageTagsBinding? = null
    internal val viewBinding get() = _viewBinding!!

    val viewModel: ManageTagsViewModel by activityViewModels()

    private var _fragmentUtils: ManageTagsFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    val newTagsDialog = AddEditTagBottomSheetFragment()
    var actionMode: ActionMode? = null
    private val tagsActionModeCallback = TagsActionModeCallback(this)

    private var _tagListAdapter: TagListAdapter? = null
    private val tagListAdapter get() = _tagListAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Timber.plant(Timber.DebugTree())
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
                viewModel.checkedTagList.collect {
                    if(actionMode == null && it.isNotEmpty()) {
                        actionMode = requireActivity().startActionMode(tagsActionModeCallback)
                    } else if(it.isEmpty()) {
                        actionMode?.finish()
                    }
                    tagsActionModeCallback.setCheckedCount(it.size)
                    actionMode?.title = "${viewModel.checkedTagList.value.size} selected"
                    actionMode?.invalidate()
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

    fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.dialog_delete_tags)
            .setPositiveButton("Delete Tags") { _, _ ->
                viewModel.deleteTags()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

class TagsActionModeCallback(private val fragment: ManageTagsFragment) : ActionMode.Callback {
    private var checkedCount: Int = 0
    fun setCheckedCount(value: Int) { checkedCount = value }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_filter_fragment_action_mode, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return when {
            checkedCount == 1 -> {
                menu?.findItem(R.id.edit_button)?.isVisible = true
                menu?.findItem(R.id.delete_button)?.isVisible = true
                true
            }
            checkedCount > 1 -> {
                menu?.findItem(R.id.edit_button)?.isVisible = false
                menu?.findItem(R.id.delete_button)?.isVisible = true
                true
            }
            else -> { false }
        }
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.edit_button -> {
                fragment.newTagsDialog.show(
                    fragment.requireActivity().supportFragmentManager,
                    AddEditTagBottomSheetFragment.TAG
                )
                // Disable this item temporarily to avoid exception
                fragment.viewLifecycleOwner.lifecycleScope.launch {
                    item.isEnabled = false
                    delay(500)
                    item.isEnabled = true
                }
                true
            }
            R.id.delete_button -> {
                fragment.showDeleteDialog()
                true
            }
            else -> { false }
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        fragment.actionMode = null
    }

}
