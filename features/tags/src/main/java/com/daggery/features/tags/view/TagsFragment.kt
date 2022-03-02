package com.daggery.features.tags.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.daggery.features.tageditorsheet.view.TagEditorSheetFragment
import com.daggery.features.tags.R
import com.daggery.features.tags.adapter.TagListAdapter
import com.daggery.features.tags.databinding.FragmentManageTagsBinding
import com.daggery.features.tags.viewmodel.TagsViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Configure ActionMode Color

@AndroidEntryPoint
class ManageTagsFragment : Fragment() {

    private var _viewBinding: FragmentManageTagsBinding? = null
    private val viewBinding get() = _viewBinding!!

    val viewModel: TagsViewModel by activityViewModels()

    val newTagsDialog = TagEditorSheetFragment()
    var actionMode: ActionMode? = null
    private val tagsActionModeCallback = TagsActionModeCallback(this)

    private var _tagListAdapter: TagListAdapter? = null
    internal val tagListAdapter get() = _tagListAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentManageTagsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindsToolbar()

        _tagListAdapter = TagListAdapter(
            MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorPrimary, Color.parseColor("#FFFAFAFA"))
        )
        viewBinding.tagsRecyclerView.adapter = tagListAdapter

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
        _tagListAdapter = null
    }

    internal fun animateStatusBarColor(colorFrom: Int, colorTo: Int, duration: Long) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = duration
        // TODO: Adjust Interpolator
        colorAnimation.interpolator = DecelerateInterpolator()
        colorAnimation.addUpdateListener { animator ->
            requireActivity().window.statusBarColor = animator.animatedValue as Int
        }
        colorAnimation.start()
    }

    private val menuItemClickListener = { item: MenuItem ->
        when(item.itemId) {
            R.id.add_tags_button -> {
                newTagsDialog.show(
                    requireActivity().supportFragmentManager,
                    TagEditorSheetFragment.TAG
                )
                true
            }
            else -> false
        }
    }

    private fun bindsToolbar() {
/*
        with(viewBinding.toolbarBinding.toolbar) {
            setNavigationIcon(SharedR.drawable.ic_back)
            setNavigationOnClickListener { findNavController().navigateUp() }
            inflateMenu(R.menu.menu_filter_fragment)
            menu.findItem(R.id.add_tags_button).icon.setTint(
                MaterialColors.getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorOnSurface,
                    Color.parseColor("#FF212121")
                )
            )
            setOnMenuItemClickListener(menuItemClickListener)
            title = "Manage Tags"
        }
*/
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
        fragment.animateStatusBarColor(
            Color.parseColor("#FF84D9FF"),
            Color.parseColor("#FF212121"),
            300
        )
        mode?.menuInflater?.inflate(R.menu.menu_filter_fragment_action_mode, menu)
        menu?.findItem(R.id.edit_button)?.icon?.setTint(Color.parseColor("#FFFAFAFA"))
        menu?.findItem(R.id.delete_button)?.icon?.setTint(Color.parseColor("#FFFAFAFA"))
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
                    TagEditorSheetFragment.TAG
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
        fragment.animateStatusBarColor(
            Color.parseColor("#FF212121"),
            Color.parseColor("#FF84D9FF"),
            400
        )
        fragment.actionMode = null
        fragment.viewModel.clearCheckedTags()
    }

}

