package com.daggery.nots.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.data.NoteTag
import com.daggery.nots.databinding.TileItemTagBinding

class TagListAdapter : ListAdapter<NoteTag, TagListAdapter.TagListViewHolder>(TagsDiff()) {

    class TagListViewHolder(private val viewBinding: TileItemTagBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(noteTag: NoteTag) {
            viewBinding.checkbox.text = noteTag.tagName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagListViewHolder {
        return TagListViewHolder(
            TileItemTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TagListViewHolder, position: Int) {
        val noteTag = getItem(position)
        holder.bind(noteTag)
    }
}

private class TagsDiff : DiffUtil.ItemCallback<NoteTag>() {
    override fun areItemsTheSame(oldItem: NoteTag, newItem: NoteTag): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteTag, newItem: NoteTag): Boolean {
        return oldItem == newItem
    }
}
