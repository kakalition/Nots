package com.daggery.nots.home.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
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

// TODO: Check if DatabaseOperation by Referring to Note UUID is Possible
// TODO: Load list when splash screen is shown
// TODO: Create Options Menu

@AndroidEntryPoint
class HomeFragment : Fragment() {

    inner class NoteLinearLayoutManager : LinearLayoutManager(
        this.context,
        VERTICAL,
        false) {
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
        setHasOptionsMenu(true)
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

        (requireActivity() as MainActivity).resetToolbarTitle()

        fragmentUtils = HomeFragmentUtils(this, viewModel)

        val adapter = NoteListItemAdapter(fragmentUtils)
        notesLiveData = viewModel.notes
        notesLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            isNotesEmpty = it.isEmpty()
            fragmentUtils.changeHomeState()
        }
        viewBinding.notesRecyclerview.layoutManager = NoteLinearLayoutManager()
        viewBinding.notesRecyclerview.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_view_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.edit_button).isVisible = false
        menu.findItem(R.id.reorder_button).isVisible = true
        menu.findItem(R.id.settings_button).isVisible = true
        menu.findItem(R.id.delete_button).isVisible = false
        menu.findItem(R.id.confirm_button).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.reorder_button -> {
                return true
            }

            R.id.settings_button -> {
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}


class HomeFragmentUtils(
    private val fragment: HomeFragment,
    private val viewModel: HomeViewModel
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