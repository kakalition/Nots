package com.daggery.nots.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.transition.TransitionManager
import com.daggery.nots.MainActivity
import com.daggery.nots.NotsApplication
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListItemAdapter
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.home.viewmodel.HomeViewModelFactory
import java.util.*

// TODO: Load list when splash screen is shown
// TODO: Implement note click listener -> open notes showing date, change action button to edit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory(
            (this.activity?.application as NotsApplication).database
        )
    }
    private lateinit var notesLiveData: LiveData<List<Note>>
    private var isListEmpty = true

    private val noteClickListener: (Note) -> Unit = { note ->
        val uuid = note.uuid
        val action = HomeFragmentDirections.actionHomeFragmentToAddEditNoteFragment(
            uuid = uuid,
            isReading = true
        )
        findNavController().navigate(action)
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

        notesLiveData = viewModel.notes
        val adapter = NoteListItemAdapter(noteClickListener)
        binding.recyclerviewTest.adapter = adapter
        notesLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            isListEmpty = it.isEmpty()
            changeHomeState()
        }
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
