package com.daggery.nots.home.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListItemAdapter
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.utils.NotsVibrator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
import com.google.android.material.color.MaterialColors

// TODO: Check if DatabaseOperation by Referring to Note UUID is Possible
// TODO: Load list when splash screen is shown
// TODO: Create Options Menu

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels()
    private lateinit var fragmentUtils: HomeFragmentUtils

    internal lateinit var noteLinearLayoutManager: NoteLinearLayoutManager

    private lateinit var notesLiveData: LiveData<List<Note>>

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
        noteLinearLayoutManager =  NoteLinearLayoutManager(requireContext())

        // Binds Toolbar
        viewBinding.toolbarBinding.apply {
            toolbarTitle.text = "Nots"
            toolbar.inflateMenu(R.menu.menu_home_fragment)
            toolbar.setOnMenuItemClickListener(fragmentUtils.onMenuItemClickListener)
        }

        // Binds RecyclerView
        val notesAdapter = NoteListItemAdapter(fragmentUtils)
        notesLiveData = viewModel.notes
        notesLiveData.observe(viewLifecycleOwner) {
            notesAdapter.submitList(it)
            fragmentUtils.changeHomeState(it.isEmpty())
        }
        viewBinding.notesRecyclerview.apply {
            layoutManager = noteLinearLayoutManager
            adapter = notesAdapter
        }

        // OnClickListener
        viewBinding.fab.setOnClickListener(fragmentUtils.fabOnClickListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}

internal class NoteLinearLayoutManager(context: Context) : LinearLayoutManager(
    context,
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

class HomeFragmentUtils(
    private val fragment: HomeFragment,
    private val navController: NavController
) {
    val notsVibrator = NotsVibrator(fragment.requireActivity())

    val setVerticalScrollState: (state: Boolean) -> Unit = { state ->
        fragment.noteLinearLayoutManager.changeScrollState(state)
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

    fun showDeleteDialog(note: Note) {
        MaterialAlertDialogBuilder(
            fragment.requireContext(),
            R.style.NotsAlertDialog
        )
            .setView(R.layout.dialog_delete)
            .setPositiveButton("Delete") { _, _ ->
                fragment.viewModel.deleteNote(note)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun getSwipeBgColor(): Int {
        return ColorUtils.setAlphaComponent(
            MaterialColors.getColor(
                fragment.requireContext(),
                com.google.android.material.R.attr.colorPrimary,
                Color.parseColor("#FFFFFFFF")
            ),
            200
        )
    }
    // Conditionally display empty illustration and notes list
    fun changeHomeState(isNotesEmpty: Boolean) {
        if(isNotesEmpty) {
            fragment.viewBinding.apply {
                emptyNotesLayout.visibility = View.VISIBLE
                notesRecyclerview.visibility = View.GONE
            }
        } else {
            fragment.viewBinding.apply {
                emptyNotesLayout.visibility = View.GONE
                notesRecyclerview.visibility = View.VISIBLE
            }
        }
    }
}