package com.daggery.features.notes.view

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.daggery.data.common.DbNoteResult
import com.daggery.domain.entities.NoteData
import com.daggery.features.notes.R
import com.daggery.features.notes.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListAdapter
import com.daggery.nots.home.adapter.NotesItemTouchHelper
import com.daggery.nots.home.view.TagsFilterBottomSheetFragment
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.sharedassets.data.BundleKeys
import com.daggery.sharedassets.utils.NoteDateUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private var _noteDateUtils: NoteDateUtils? = null
    private val noteDateUtils get() = _noteDateUtils!!

    private var notesAdapter: NoteListAdapter? = null

    private val filterBottomSheet = TagsFilterBottomSheetFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _noteDateUtils = NoteDateUtils()

        notesAdapter = NoteListAdapter(
            dateParser = { noteDateUtils.getParsedDate(it) },
            onNoteClickListener = { onNoteClickListener(it) },
            reorderCallback = { viewModel.rearrangeNoteOrder(it) }
        )

        bindsToolbar()
        bindsFab()
        bindsRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.allNotes.collect(onReceiveDbNoteResult)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        notesAdapter = null
    }

    private fun bindsToolbar() {
        viewBinding.toolbarBinding.apply {
            toolbar.inflateMenu(R.menu.menu_home_fragment)
            toolbar.setOnMenuItemClickListener(onMenuItemClickListener)
        }
    }

    private fun bindsRecyclerView() {
        viewBinding.notesRecyclerview.apply {
            adapter = notesAdapter
        }
        val itemTouchHelper = ItemTouchHelper(NotesItemTouchHelper(notesAdapter!!))
        itemTouchHelper.attachToRecyclerView(viewBinding.notesRecyclerview)
    }

    private fun bindsFab() {
        viewBinding.fab.setOnClickListener(fabOnClickListener)
    }

    private val onReceiveDbNoteResult = { result: DbNoteResult ->
        if (result is DbNoteResult.Loading) {
            with(viewBinding) {
                emptyNotesLayout.visibility = View.GONE
                notesRecyclerview.visibility = View.GONE
                loadingSpinner.visibility = View.VISIBLE
            }
        } else if (result is DbNoteResult.Success && result.data.isNotEmpty()) {
            notesAdapter?.submitList(result.data)
            with(viewBinding) {
                emptyNotesLayout.visibility = View.GONE
                notesRecyclerview.visibility = View.VISIBLE
                loadingSpinner.setVisibilityAfterHide(View.GONE)
                loadingSpinner.hide()
            }
        } else if (result is DbNoteResult.Success && result.data.isEmpty()) {
            with(viewBinding) {
                emptyNotesLayout.visibility = View.VISIBLE
                notesRecyclerview.visibility = View.GONE
                loadingSpinner.setVisibilityAfterHide(View.GONE)
                loadingSpinner.hide()
            }
        }
    }

    private val onNoteClickListener: (NoteData) -> Unit = { note ->
        val bundle = bundleOf(BundleKeys.UUID to note.uuid)
        //findNavController().navigate(, bundle)
    }

    private val fabOnClickListener = { _ : View ->
        //val extras = FragmentNavigatorExtras(viewBinding.fab to "from_fab_to_add")
        // TODO: Navigate
        //findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = ""), extras)
    }

    private val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when(item.itemId) {
            R.id.filter_button -> {
                filterBottomSheet.show(parentFragmentManager, TagsFilterBottomSheetFragment.TAG)
                true
            }
            R.id.reorder_button -> {
                showReorderChronologicallyDialog()
                true
            }
            R.id.delete_all_notes_button -> {
                showDeleteAllDialog()
                true
            }
            R.id.manage_tags_button -> {
                //findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToManageTagsFragment())
                true
            }
            R.id.settings_button -> {
                //val destination = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
                //findNavController().navigate(destination)
                true
            }
            else -> false
        }
    }

    private fun showReorderChronologicallyDialog() {
        MaterialAlertDialogBuilder(requireContext(), com.daggery.sharedassets.R.style.NotsAlertDialog)
            .setView(R.layout.dialog_reorder_notes_chronologically)
            .setPositiveButton("Reorder") { _, _ -> viewModel.reorderNotesChronologically() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showDeleteAllDialog() {
        MaterialAlertDialogBuilder(requireContext(), com.daggery.sharedassets.R.style.NotsAlertDialog)
            .setView(R.layout.dialog_delete_all_notes)
            .setPositiveButton("Delete") { _, _ -> viewModel.deleteAllNotes() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

/*
    fun filterListWithTags(notes: List<ContactsContract.CommonDataKinds.Note>, tagNameList: List<String>): List<ContactsContract.CommonDataKinds.Note> {
        return  if (tagNameList.isEmpty()) { notes }
        else { notes.filter { tagNameList.intersect(it.noteTags).isNotEmpty() } }
    }
*/


/*
            launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mainViewModel.noteTagList.collect { list ->
                        checkedTagsName = list
                            .filter { noteTag -> noteTag.checked }
                            .map { noteTag -> noteTag.tagName }

                        invalidateHomeLayout(filterListWithTags(localNotes, checkedTagsName))
                    }
                }
            }
*/


/*
    private var localNotes: List<NoteDataEntity> = listOf()
    private var checkedTagsName: List<String> = listOf()

*/
/*
val outlinedTextColor = MaterialColors.getColor(
    fragment.requireContext(),
    com.google.android.material.R.attr.colorOnSurface,
    fragment.resources.getColor(R.color.white, null)
)
*/
/*
    fun getHomeLayoutKey(): Int {
        return fragment.mainViewModel.themeManager.homeLayoutKey
    }
*//*


    fun showDeleteDialog(note: NoteData) {
        MaterialAlertDialogBuilder(
            fragment.requireContext(),
            R.style.NotsAlertDialog
        )
            .setView(R.layout.dialog_delete_note)
            .setPositiveButton("Delete") { _, _ ->
                //fragment.viewModel.deleteNote(note)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
*/