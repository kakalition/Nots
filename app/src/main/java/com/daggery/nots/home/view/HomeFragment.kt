package com.daggery.nots.home.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.MainActivity
import com.daggery.nots.NotsApplication
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListItemAdapter
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.utils.NotsVibrator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered

// TODO: Check if DatabaseOperation by Referring to Note UUID is Possible
// TODO: Load list when splash screen is shown
// TODO: Create Options Menu

@AndroidEntryPoint
class HomeFragment : Fragment() {

    inner class NoteLinearLayoutManager : LinearLayoutManager(
        this.context,
        VERTICAL,
        false
    ) {
        private var canScrollVerticallyState: Boolean = true
        fun changeScrollState(state: Boolean) {
            canScrollVerticallyState = state
        }

        override fun canScrollVertically(): Boolean {
            return canScrollVerticallyState && super.canScrollVertically()
        }
    }

    private var _viewBinding: FragmentHomeBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels()

    private lateinit var fragmentUtils: HomeFragmentUtils

    private lateinit var notesLiveData: LiveData<List<Note>>
    internal var isNotesEmpty = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instantiate Fragment Utils Class
        fragmentUtils = HomeFragmentUtils(this, findNavController())

        // Prepare Toolbar
        viewBinding.toolbarBinding.apply {
            toolbarTitle.text = "Nots"
            toolbar.inflateMenu(R.menu.menu_home_fragment)
            toolbar.setOnMenuItemClickListener(fragmentUtils.onMenuItemClickListener)
        }

        // Prepare RecyclerView
        val adapter = NoteListItemAdapter(fragmentUtils)
        notesLiveData = viewModel.notes
        notesLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            isNotesEmpty = it.isEmpty()
            fragmentUtils.changeHomeState()
        }
        viewBinding.notesRecyclerview.layoutManager = NoteLinearLayoutManager()
        viewBinding.notesRecyclerview.adapter = adapter

        // OnClickListener
        viewBinding.fab.setOnClickListener(fragmentUtils.fabOnClickListener)
    }
}


class HomeFragmentUtils(
    private val fragment: HomeFragment,
    private val navController: NavController
) {
    val notsVibrator = NotsVibrator(fragment.requireActivity())
    val setVerticalScrollState: (state: Boolean) -> Unit = { state ->
        (fragment.viewBinding.notesRecyclerview.layoutManager as HomeFragment.NoteLinearLayoutManager).changeScrollState(state)
    }

    val noteClickListener: (Note) -> Unit = { note ->
        val uuid = note.uuid
        val action = HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = uuid)
        fragment.findNavController().navigate(action)
    }

    val fabOnClickListener = { _ : View ->
        val extras = FragmentNavigatorExtras(fragment.viewBinding.fab to "from_fab_to_add")
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(uuid = ""), extras)
    }

    val onMenuItemClickListener: (MenuItem) -> Boolean = { item: MenuItem ->
        when(item.itemId) {
            R.id.reorder_button -> {
                true
            }
            R.id.settings_button -> {
                val destination = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
                fragment.findNavController().navigate(destination)
                true
            }
            else -> false
        }
    }

    fun prioritize(note: Note) {
        fragment.viewModel.prioritize(note)
    }

    fun unprioritize(note: Note) {
        fragment.viewModel.unprioritize(note)
    }

    fun deleteNote(note: Note) {
        MaterialAlertDialogBuilder(fragment.requireContext(), ThemeOverlay_Material3_MaterialAlertDialog_Centered)
            .setIcon(R.drawable.ic_delete)
            .setTitle("Delete Note")
            .setMessage("Are you sure want to delete this note?\nThis action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                fragment.viewModel.deleteNote(note)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Conditionally display empty illustration and notes list
    fun changeHomeState() {
        if(fragment.isNotesEmpty) {
            fragment.viewBinding.emptyNotesLayout.visibility = View.VISIBLE
            fragment.viewBinding.notesRecyclerview.visibility = View.GONE
        } else {
            fragment.viewBinding.emptyNotesLayout.visibility = View.GONE
            fragment.viewBinding.notesRecyclerview.visibility = View.VISIBLE
        }
    }
}