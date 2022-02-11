package com.daggery.nots.home.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daggery.nots.MainViewModel
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListAdapter
import com.daggery.nots.home.utils.HomeFragmentUtils
import com.daggery.nots.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Check if DatabaseOperation by Referring to Note UUID is Possible

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels()
    internal val mainViewModel: MainViewModel by activityViewModels()
    private var _fragmentUtils: HomeFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    internal var notesLinearLayoutManager: NoteLinearLayoutManager? = null
    internal var notesAdapter: NoteListAdapter? = null

    internal lateinit var notesLiveData: LiveData<List<Note>>
    internal val notesObserver: (List<Note>) -> Unit = { noteList ->
        notesAdapter?.submitList(noteList)
        fragmentUtils.changeHomeState(noteList.isEmpty())
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

        _fragmentUtils = HomeFragmentUtils(this, findNavController())
        notesLinearLayoutManager =  NoteLinearLayoutManager(requireContext())
        notesAdapter = NoteListAdapter(fragmentUtils)

        with(fragmentUtils) {
            bindsToolbar()
            bindsFab()
            bindsRecyclerView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
        notesLinearLayoutManager = null
        notesAdapter = null
        notesLiveData.removeObserver(notesObserver)
    }
}

class NoteLinearLayoutManager(context: Context) : LinearLayoutManager(
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
