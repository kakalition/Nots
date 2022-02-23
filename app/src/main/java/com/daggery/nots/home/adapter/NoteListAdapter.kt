package com.daggery.nots.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daggery.domain.entities.NoteData
import com.daggery.nots.R
import com.daggery.nots.databinding.TileNoteItemBinding
import com.google.android.material.chip.Chip
import java.util.*

class NoteListAdapter(
    private val dateParser: (Long) -> String,
    private val onNoteClickListener: (NoteData) -> Unit,
    private val reorderCallback: (List<NoteData>) -> Unit,) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private val notes: MutableList<NoteData> = mutableListOf()
    private val diffCallback = NotesDiff(notes, listOf())
    private val notesBatch = NotesBatch(notes.toMutableList())

    inner class NoteViewHolder(val binding: TileNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteData) {
            with(binding) {
                when (note.priority) {
                    0 -> { listItemLayout.setBackgroundResource(R.drawable.bg_note_item_filled) }
                    1 -> { listItemLayout.setBackgroundResource(R.drawable.bg_note_item_filled_priority) }
                }

                noteTitle.text = note.noteTitle
                noteBody.text = note.noteBody
                noteDate.text = "Date: " + dateParser(note.noteDate)
                note.noteTags.forEach {
                    val chip = LayoutInflater.from(chipGroup.context).inflate(R.layout.chip_note_item, chipGroup, false) as Chip
                    chip.text = it
                    chip.isCheckable = false
                    chip.isClickable = false
                    chip.isFocusable = false
                    chipGroup.addView(chip)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : NoteListAdapter.NoteViewHolder {
        return NoteViewHolder(TileNoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.binding.listItemLayout.setOnClickListener{ onNoteClickListener(current) }
        holder.bind(current)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun submitList(updatedList: List<NoteData>) {
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
        reorderCallback(notesBatch.batch.toMutableList())
    }
}

class NotesBatch(private var _notesBatch: MutableList<NoteData>) {

    val batch get() = _notesBatch.toList()

    fun updateBatch(firstIndex: Int, secondIndex: Int) {
        val firstNote = batch[firstIndex]
        val secondNote = batch[secondIndex]

        val tempFirstNoteOrder = firstNote.noteOrder
        val tempSecondNoteOrder = secondNote.noteOrder

        val firstNoteIndex = _notesBatch.indexOf(firstNote)
        val secondNoteIndex = _notesBatch.indexOf(secondNote)

        _notesBatch[firstNoteIndex] = firstNote.copy(noteOrder = tempSecondNoteOrder)
        _notesBatch[secondNoteIndex] = secondNote.copy(noteOrder = tempFirstNoteOrder)
    }
}

class NotesDiff(private var oldList: List<NoteData>, var newList: List<NoteData>) : DiffUtil.Callback() {

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
