package com.daggery.nots.home.view

import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.daggery.data.entities.NoteDataEntity
import com.daggery.nots.MainViewModel
import com.daggery.nots.databinding.FragmentHomeBinding
import com.daggery.nots.home.adapter.NoteListAdapter
import com.daggery.nots.home.utils.HomeFragmentUtils
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.utils.NoteDateUtils
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    internal val viewBinding get() = _viewBinding!!

    internal val viewModel: HomeViewModel by activityViewModels()
    internal val mainViewModel: MainViewModel by activityViewModels()

    private var _fragmentUtils: HomeFragmentUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    internal var notesAdapter: NoteListAdapter? = null

    private var localNotes: List<NoteDataEntity> = listOf()
    private var checkedTagsName: List<String> = listOf()

    internal val filterBottomSheet = TagsFilterBottomSheetFragment()

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

        val initialNotes: MutableList<ContactsContract.CommonDataKinds.Note> = mutableListOf()
        notesAdapter = NoteListAdapter(initialNotes, fragmentUtils, NoteDateUtils())

        with(fragmentUtils) {
            bindsToolbar()
            bindsFab()
            bindsRecyclerView()
        }

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
                    viewModel.allNotes.collect { list ->
                        localNotes = list
                        invalidateHomeLayout(filterListWithTags(list, checkedTagsName))
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
        notesAdapter = null
    }

    // Conditionally display empty illustration and notes list
    fun changeHomeState(isNotesEmpty: Boolean) {
        if(isNotesEmpty) {
            viewBinding.apply {
                emptyNotesLayout.visibility = View.VISIBLE
                notesRecyclerview.visibility = View.GONE
            }
        } else {
            viewBinding.apply {
                emptyNotesLayout.visibility = View.GONE
                notesRecyclerview.visibility = View.VISIBLE
            }
        }
    }

    fun filterListWithTags(notes: List<ContactsContract.CommonDataKinds.Note>, tagNameList: List<String>): List<ContactsContract.CommonDataKinds.Note> {
        return  if (tagNameList.isEmpty()) { notes }
        else { notes.filter { tagNameList.intersect(it.noteTags).isNotEmpty() } }
    }

    fun invalidateHomeLayout(notes: List<ContactsContract.CommonDataKinds.Note>) {
        notesAdapter?.submitList(notes)
        changeHomeState(notes.isEmpty())
    }

}

/*


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
*/
