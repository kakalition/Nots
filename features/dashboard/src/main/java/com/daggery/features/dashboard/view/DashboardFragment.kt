package com.daggery.features.dashboard.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.daggery.domain.entities.NoteData
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

        bindCurrentBook()
        bindDashboardNote(viewModel.dashboardNote)
        bindLastWrittenNotes(viewModel.lastWrittenNotes)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun bindCurrentBook() {
        TileCurrentBookBinding.bind(viewBinding.root).apply {
            currentBookTitle.text = "Dream Journal"
            currentBookDescription.text = "All about dream, especially in lucid state"
            detailTotalNotes.detailTitle.text = "Total Notes"
            detailTotalNotes.detailValue.text = "36"
            detailTotalTags.detailTitle.text = "Total Tags"
            detailTotalTags.detailValue.text = "8"
            detailTotalDate.detailTitle.text = "Date Created"
            detailTotalDate.detailValue.text = "March 13, 2021"
        }
    }

    private fun bindDashboardNote(value: NoteData) {
        viewBinding.higlightedNote.apply {
            noteTitle.text = value.noteTitle
            noteBody.text = value.noteBody
        }
    }

    private fun bindLastWrittenNotes(value: List<NoteData>) {
        if(value.size >= 1) {
            viewBinding.lastWrittenNoteOne.root.visibility = View.VISIBLE
            viewBinding.lastWrittenNoteOne.noteIndex.text = value[0].noteOrder.toString()
            viewBinding.lastWrittenNoteOne.noteTitle.text = value[0].noteTitle
            viewBinding.lastWrittenNoteOne.noteBody.text = value[0].noteBody
        }
        if(value.size >= 2) {
            viewBinding.lastWrittenNoteTwo.root.visibility = View.VISIBLE
            viewBinding.lastWrittenNoteTwo.noteIndex.text = value[1].noteOrder.toString()
            viewBinding.lastWrittenNoteTwo.noteTitle.text = value[1].noteTitle
            viewBinding.lastWrittenNoteTwo.noteBody.text = value[1].noteBody
        }
        if(value.size == 3) {
            viewBinding.lastWrittenNoteThree.root.visibility = View.VISIBLE
            viewBinding.lastWrittenNoteThree.noteIndex.text = value[2].noteOrder.toString()
            viewBinding.lastWrittenNoteThree.noteTitle.text = value[2].noteTitle
            viewBinding.lastWrittenNoteThree.noteBody.text = value[2].noteBody
        }
    }
}