package com.daggery.nots.home.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.NotsApplication
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListItemAdapter
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory
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

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (this.activity?.application as NotsApplication).database
        )
    }
    private lateinit var notesLiveData: LiveData<List<Note>>
    private var isListEmpty = true
    private lateinit var vibrator: Vibrator

    private val isVerticalScrollActive: (state: Boolean) -> Unit = { state ->
        (binding.recyclerviewTest.layoutManager as NoteLinearLayoutManager).changeScrollState(state)
    }

    private val noteClickListener: (Note) -> Unit = { note ->
        val uuid = note.uuid
        val action = HomeFragmentDirections.actionHomeFragmentToAddViewNoteFragment(
            uuid = uuid,
            isReading = true
        )
        findNavController().navigate(action)
    }

    private val noteLongClickListener: (Note) -> Unit = { note ->
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
        MaterialAlertDialogBuilder(requireContext(), R.style.NotsAlertDialog)
            .setView(dialogView)
            .setPositiveButton(positiveText, action)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private val recyclerViewListener = object : RecyclerView.SimpleOnItemTouchListener() {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        notesLiveData = viewModel.notes
        val adapter = NoteListItemAdapter(noteClickListener, noteLongClickListener, isVerticalScrollActive, vibrator)
        binding.recyclerviewTest.layoutManager = NoteLinearLayoutManager()
        binding.recyclerviewTest.adapter = adapter
        notesLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            isListEmpty = it.isEmpty()
            changeHomeState()
        }

        binding.recyclerviewTest.addOnItemTouchListener(recyclerViewListener)
    }

    // Conditionally display empty illustration and notes list
    private fun changeHomeState() {
        if(isListEmpty) {
            binding.emptyNotesLayout.visibility = View.VISIBLE
            binding.recyclerviewTest.visibility = View.GONE
        } else {
            binding.emptyNotesLayout.visibility = View.GONE
            binding.recyclerviewTest.visibility = View.VISIBLE
        }
    }
}
