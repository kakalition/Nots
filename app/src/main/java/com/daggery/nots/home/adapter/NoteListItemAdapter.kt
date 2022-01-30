package com.daggery.nots.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.ListItemNoteBinding

class NoteListItemAdapter(private val noteClickListener: (Note) -> Unit)
    : ListAdapter<Note, NoteListItemAdapter.NoteViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

        }
    }

    class NoteViewHolder(val binding: ListItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            if(note.priority == 1) {
                binding.listItemLayout.setBackgroundResource(R.drawable.list_item_note_bg_priority)
            } else binding.listItemLayout.setBackgroundResource(R.drawable.list_item_note_bg)
            binding.noteTitle.text = note.noteTitle
            binding.noteBody.text = note.noteBody
            binding.noteDate.text = note.noteDate
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : NoteViewHolder {
        return NoteViewHolder(ListItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: NoteListItemAdapter.NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            noteClickListener(current)
        }
        holder.bind(current)
    }
}