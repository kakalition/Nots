package com.daggery.nots.home.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daggery.nots.NotsApplication
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListItemAdapter
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory
import com.daggery.nots.utils.NotsVibrator
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: Check if DatabaseOperation by Referring to Note UUID is Possible
// TODO: Load list when splash screen is shown
// TODO: Create Options Menu


class HomeFragment : Fragment() {

    inner class NoteLinearLayoutManager : LinearLayoutManager(
        this.context,
        VERTICAL,
        false) {
        private var canScrollHorizontallyState: Boolean = true
        fun changeScrollState(state: Boolean) {
            canScrollHorizontallyState = state
        }
        override fun canScrollVertically(): Boolean {
            return canScrollHorizontallyState && super.canScrollVertically()
        }
    }

    private var _viewBinding: FragmentHomeBinding? = null
    internal val viewBinding get() = _viewBinding!!

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory((this.activity?.application as NotsApplication).database)
    }

    private lateinit var homeFragmentUtils: HomeFragmentUtils

    private lateinit var notesLiveData: LiveData<List<Note>>
    internal var isNotesEmpty = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFragmentUtils = HomeFragmentUtils(this, viewModel)

        val adapter = NoteListItemAdapter(homeFragmentUtils)
        notesLiveData = viewModel.notes
        notesLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            isNotesEmpty = it.isEmpty()
            homeFragmentUtils.changeHomeState()
        }
        viewBinding.notesRecyclerview.layoutManager = NoteLinearLayoutManager()
        viewBinding.notesRecyclerview.adapter = adapter
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
        val action = HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(
            uuid = uuid,
            isReading = true
        )
        fragment.findNavController().navigate(action)
    }

    val noteLongClickListener: (Note) -> Unit = { note ->
        val dialogView: Int
        val positiveText: String
        val action: (DialogInterface, Int) -> Unit

        if(note.priority == 0) {
            dialogView = R.layout.dialog_prioritize
            positiveText = "Prioritize"
            action = { _, _ -> viewModel.prioritize(note) }
        } else {
            dialogView = R.layout.dialog_unprioritize
            positiveText = "Unprioritize"
            action = { _, _ -> viewModel.unprioritize(note) }
        }
        MaterialAlertDialogBuilder(fragment.requireContext(), R.style.NotsAlertDialog)
            .setView(dialogView)
            .setPositiveButton(positiveText, action)
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