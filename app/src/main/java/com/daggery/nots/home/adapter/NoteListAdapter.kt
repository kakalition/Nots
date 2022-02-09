package com.daggery.nots.home.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.ListItemNoteBinding
import com.daggery.nots.home.utils.HomeFragmentUtils

class NoteListAdapter(
    private val homeFragmentUtils: HomeFragmentUtils
) : ListAdapter<Note, NoteListAdapter.NoteViewHolder>(DiffCallback) {

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

    inner class NoteViewHolder(val binding: ListItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            with(binding) {
                (swipeBg.background as GradientDrawable).setColor(
                    homeFragmentUtils.getSwipeBgColor()
                )
                if(note.priority == 1) {
                    listItemLayout.setBackgroundResource(R.drawable.bg_note_item_priority)
                } else {
                    listItemLayout.setBackgroundResource(R.drawable.bg_note_item)
                }
                noteTitle.text = note.noteTitle
                noteBody.text = note.noteBody
                noteDate.text = note.noteDate
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : NoteViewHolder {
        return NoteViewHolder(ListItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.listItemLayout.apply {
            setOnClickListener{ homeFragmentUtils.noteClickListener(current) }
            setOnTouchListener(OnNoteItemTouchListener(homeFragmentUtils, holder, current))
        }
        holder.bind(current)
    }
}
