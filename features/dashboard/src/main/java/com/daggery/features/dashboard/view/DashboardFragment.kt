package com.daggery.features.dashboard.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.daggery.features.dashboard.databinding.FragmentDashboardBinding
import com.daggery.features.dashboard.databinding.TileCurrentBookBinding
import com.daggery.features.dashboard.databinding.TileQuickActionBinding
import com.daggery.features.dashboard.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {

    private var _viewBinding: FragmentDashboardBinding? = null
    private val viewBinding get() = _viewBinding!!

    val viewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _viewBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TileQuickActionBinding.bind(viewBinding.root).apply {
            newNote.noteTitle.text = "New Note"
            newTag.noteTitle.text = "New Tag"
        }

        TileCurrentBookBinding.bind(viewBinding.root).apply {
            currentBookTitle.text = "Dream Journal"
            currentBookDescription.text = "All about dream, especially in lucid state"
            currentBookTotalNotes.text = "14"
        }

        viewBinding.higlightedNote.apply {
            noteTitle.text = "Torch Feature"
            noteBody.text = "Lorem ipsum dolor sit amet Lorem ipsum"
        }

        viewBinding.lastWrittenNote.apply {
            noteTitle.text = "Entity Summoner"
            noteBody.text = "Legend has it, true warrior that came from the past"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}