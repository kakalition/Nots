package com.daggery.nots.home.adapter

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.daggery.nots.R
import com.daggery.nots.data.Note
import com.daggery.nots.databinding.ListItemNoteBinding
import com.daggery.nots.home.view.HomeFragmentUtils
import kotlinx.coroutines.delay

class NoteListItemAdapter(
    private val homeFragmentUtils: HomeFragmentUtils
) : ListAdapter<Note, NoteListItemAdapter.NoteViewHolder>(DiffCallback) {

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
            (binding.swipeBg.background as GradientDrawable).setColor(Color.argb(255, 78, 78, 78))
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
            false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.listItemLayout.setOnClickListener{ homeFragmentUtils.noteClickListener(current) }

        var viewAnchorX = 0f
        var isSwiping = false
        var translationValue: Float
        var swipeWeight = 0.45f
        var swipeThreshold = 0f
        val changeSwipeColorRange = -5f..5f
        holder.binding.listItemLayout.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isSwiping = false
                    viewAnchorX = event.rawX
                    swipeThreshold = (v.width / 3).toFloat()
                }
                MotionEvent.ACTION_UP -> {
                    if(!isSwiping) v.performClick()
                    homeFragmentUtils.notsVibrator.resetVibrator()
                    homeFragmentUtils.setVerticalScrollState(true)
                    v.animate()
                        .translationX(0f)
                        .setDuration(200)
                        .setInterpolator(DecelerateInterpolator())
                        .withEndAction {
                            (holder.binding.swipeBg.background as GradientDrawable).setColor(Color.argb(255, 78, 78, 78))
                        }
                        .start()
                    isSwiping = false
                }
                MotionEvent.ACTION_MOVE -> {
                    homeFragmentUtils.setVerticalScrollState(false)
                    translationValue = event.rawX - viewAnchorX
                    if(translationValue > 10 || translationValue < -10) {
                        isSwiping = true
                    }
                    v.translationX = translationValue * swipeWeight
                    when {
                        v.x in changeSwipeColorRange -> {
                            homeFragmentUtils.notsVibrator.resetVibrator()
                            (holder.binding.swipeBg.background as GradientDrawable).setColor(
                                Color.argb(255, 78, 78, 78)
                            )
                        }
                        translationValue > swipeThreshold -> {
                            homeFragmentUtils.notsVibrator.vibrate()
                            (holder.binding.swipeBg.background as GradientDrawable).setColor(
                                Color.argb(255, 255, 105, 97)
                            )
                        }
                        translationValue < -swipeThreshold -> {
                            homeFragmentUtils.notsVibrator.vibrate()
                            (holder.binding.swipeBg.background as GradientDrawable).setColor(
                                Color.argb(255, 78, 175, 201)
                            )
                        }
                    }
                }
                else -> {
                    homeFragmentUtils.setVerticalScrollState(true)
                    isSwiping = false
                }
            }
            return@setOnTouchListener true
        }
        holder.bind(current)
    }
}