package com.daggery.features.books.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daggery.domain.entities.BookData
import com.daggery.features.books.databinding.TileBookBinding

class BooksAdapter : ListAdapter<BookData, BooksAdapter.BookViewHolder>(diffUtil) {

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<BookData>() {
            override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean {
                return oldItem == newItem
            }
        }
    }

    class BookViewHolder(private val viewBinding: TileBookBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(bookData: BookData) {
            with(viewBinding) {
                bookTitle.text = bookData.title
                bookDescription.text = bookData.description
                bookDate.text = bookData.date.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            TileBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentData = getItem(position)
        holder.bind(currentData)
    }
}