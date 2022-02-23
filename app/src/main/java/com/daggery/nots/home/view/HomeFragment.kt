package com.daggery.nots.home.view

import android.os.Bundle
import android.provider.ContactsContract
import android.transition.Visibility
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.daggery.data.common.DbNoteResult
import com.daggery.data.entities.NoteDataEntity
import com.daggery.domain.entities.NoteData
import com.daggery.nots.MainViewModel
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListAdapter
import com.daggery.nots.home.adapter.NotesItemTouchHelper
import com.daggery.nots.home.utils.HomeFragmentUtils
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.utils.NoteDateUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels()
    internal val mainViewModel: MainViewModel by activityViewModels()

    private var _noteDateUtils: NoteDateUtils? = null
    private val noteDateUtils get() = _noteDateUtils!!

    private var notesAdapter: NoteListAdapter? = null

    private var localNotes: List<NoteDataEntity> = listOf()
    private var checkedTagsName: List<String> = listOf()

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

        bindsToolbar()
        bindsFab()
        bindsRecyclerView()

        val initialNotes: MutableList<NoteData> = mutableListOf()

        notesAdapter = NoteListAdapter(
            notes = initialNotes,
            dateParser = { noteDateUtils.getParsedDate(it) },
            onNoteClickListener = { onNoteClickListener },
            reorderCallback = { viewModel.rearrangeNoteOrder(it) }
        )

        viewLifecycleOwner.lifecycleScope.launch {

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

    fun bindsToolbar() {
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
            }
        } else if (result is DbNoteResult.Success && result.data.isEmpty()) {
            with(viewBinding) {
                emptyNotesLayout.visibility = View.VISIBLE
                notesRecyclerview.visibility = View.GONE
                loadingSpinner.setVisibilityAfterHide(View.GONE)
            }
        }
    }

/*
    fun filterListWithTags(notes: List<ContactsContract.CommonDataKinds.Note>, tagNameList: List<String>): List<ContactsContract.CommonDataKinds.Note> {
        return  if (tagNameList.isEmpty()) { notes }
        else { notes.filter { tagNameList.intersect(it.noteTags).isNotEmpty() } }
    }
*/


    val onNoteClickListener: (NoteData) -> Unit = { note ->
        val uuid = note.uuid
        val action = HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = uuid)
        findNavController().navigate(action)
    }

    private val fabOnClickListener = { _ : View ->
        val extras = FragmentNavigatorExtras(viewBinding.fab to "from_fab_to_add")
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = ""), extras)
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
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToManageTagsFragment())
                true
            }
            R.id.settings_button -> {
                val destination = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
                findNavController().navigate(destination)
                true
            }
            else -> false
        }
    }

    private fun showReorderChronologicallyDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.NotsAlertDialog)
            .setView(R.layout.dialog_reorder_notes_chronologically)
            .setPositiveButton("Reorder") { _, _ -> viewModel.reorderNotesChronologically() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showDeleteAllDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.NotsAlertDialog)
            .setView(R.layout.dialog_delete_all_notes)
            .setPositiveButton("Delete") { _, _ -> viewModel.deleteAllNotes() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

}