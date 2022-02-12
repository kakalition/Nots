package com.daggery.nots.home.adapter

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.TileNoteItemBinding
import com.daggery.nots.home.utils.HomeFragmentUtils
import com.daggery.nots.utils.NoteDateUtils
import java.util.*

class NoteListAdapter(
    val notes: MutableList<Note>,
    private val homeFragmentUtils: HomeFragmentUtils,
    private val noteDateUtils: NoteDateUtils
) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private val diffCallback = NotesDiff(notes, listOf())
    private val notesBatch = NotesBatch(notes)

    inner class NoteViewHolder(val binding: TileNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            val homeLayoutKey = homeFragmentUtils.getHomeLayoutKey()
            with(binding) {
                (swipeBg.background as GradientDrawable).setColor(
                    homeFragmentUtils.getSwipeBgColor()
                )
                when {
                    // Filled and Not Priority
                    homeLayoutKey == 0 && note.priority == 0 -> {
                        listItemLayout.setBackgroundResource(R.drawable.bg_note_item_filled)
                    }
                    // Filled and Priority
                    homeLayoutKey == 0 && note.priority == 1 -> {
                        listItemLayout.setBackgroundResource(R.drawable.bg_note_item_filled_priority)
                    }
                    // Outlined and Not Priority
                    homeLayoutKey == 1 && note.priority == 0 -> {
                        listItemLayout.setBackgroundResource(R.drawable.bg_note_item_outlined)
                        noteTitle.setTextColor(homeFragmentUtils.outlinedTextColor)
                        noteBody.setTextColor(homeFragmentUtils.outlinedTextColor)
                        noteDate.setTextColor(homeFragmentUtils.outlinedTextColor)
                    }
                    // Outlined and Priority
                    homeLayoutKey == 1 && note.priority == 1 -> {
                        listItemLayout.setBackgroundResource(R.drawable.bg_note_item_outlined_priority)
                        noteTitle.setTextColor(homeFragmentUtils.outlinedTextColor)
                        noteBody.setTextColor(homeFragmentUtils.outlinedTextColor)
                        noteDate.setTextColor(homeFragmentUtils.outlinedTextColor)
                    }
                }

                noteTitle.text = note.noteTitle
                noteBody.text = note.noteBody
                noteDate.text = noteDateUtils.getParsedDate(note.noteDate)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : NoteViewHolder {
        return NoteViewHolder(TileNoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.binding.listItemLayout.apply {
            setOnClickListener{ homeFragmentUtils.noteClickListener(current) }
            setOnTouchListener(OnNoteItemTouchListener(homeFragmentUtils, holder, current))
        }
        holder.bind(current)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun submitList(updatedList: List<Note>) {
        diffCallback.newList = updatedList
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        notes.clear()
        notes.addAll(updatedList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun onItemMoved(fromPos: Int, toPos: Int) {
        Collections.swap(notes, fromPos, toPos)
        notesBatch.updateBatch(fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }

    fun updateDatabase() {
        homeFragmentUtils.rearrangeNoteOrder(notesBatch.notesBatch.toMutableList())
    }
}

class NotesBatch(private var _notesBatch: MutableList<Note>) {

    fun initNotesBatch(newNotesBatch: MutableList<Note>) {
        _notesBatch = newNotesBatch
    }

    val notesBatch get() = _notesBatch.toList()

    fun updateBatch(firstIndex: Int, secondIndex: Int) {
        val firstNote = notesBatch[firstIndex]
        val secondNote = notesBatch[secondIndex]

        val tempFirstNoteOrder = firstNote.noteOrder
        val tempSecondNoteOrder = secondNote.noteOrder

        val firstNoteIndex = _notesBatch.indexOf(firstNote)
        val secondNoteIndex = _notesBatch.indexOf(secondNote)

        _notesBatch[firstNoteIndex] = firstNote.copy(noteOrder = tempSecondNoteOrder)
        _notesBatch[secondNoteIndex] = secondNote.copy(noteOrder = tempFirstNoteOrder)

        notesBatch.forEachIndexed { index, note ->
            Log.d("LOL $index", note.toString())
        }
    }

    fun getNotesBatchAndReset(): List<Note> {
        val temp = notesBatch
        _notesBatch.clear()
        return temp
    }
}

class NotesDiff(var oldList: List<Note>, var newList: List<Note>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.uuid == newItem.uuid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}
