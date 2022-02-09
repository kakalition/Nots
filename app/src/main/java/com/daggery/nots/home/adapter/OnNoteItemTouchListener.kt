package com.daggery.nots.home.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.daggery.nots.data.Note
import com.daggery.nots.home.utils.HomeFragmentUtils

class OnNoteItemTouchListener(
    private val homeFragmentUtils: HomeFragmentUtils,
    private val holder: NoteListAdapter.NoteViewHolder,
    private val current: Note
) : View.OnTouchListener {

    private var isSwiping = false
    private var shouldChangePriority = false
    private var shouldDelete = false
    private var translationValue = 0f
    private var viewAnchorX = 0f
    private val swipeWeight = 0.45f
    private var swipeThreshold = 0f
    private var neutralRange: ClosedFloatingPointRange<Float> = 0f..0f

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                onActionDownState(event, view)
            }

            MotionEvent.ACTION_MOVE -> {
                onActionMoveState(view, event)
                onActionMoveActions(view)
            }

            MotionEvent.ACTION_UP -> {
                if(!isSwiping) view.performClick()

                playNeutralItemAnimation(view)
                onActionUpState()
                onActionUpActions()
            }

            else -> {
                onUnspecifiedEventState()
            }
        }
        return true
    }

    private fun onActionDownState(event: MotionEvent, view: View) {
        isSwiping = false
        viewAnchorX = event.rawX
        swipeThreshold = (view.width.div(4)).toFloat()
        neutralRange = -swipeThreshold..swipeThreshold
    }

    private fun onActionMoveState(view: View, event: MotionEvent) {
        homeFragmentUtils.setVerticalScrollState(false)
        translationValue = event.rawX - viewAnchorX
        if(translationValue > 10 || translationValue < -10) {
            isSwiping = true
        }
        view.translationX = translationValue * swipeWeight
    }

    private fun onActionUpState() {
        with(homeFragmentUtils) {
            isSwiping = false
            notsVibrator.resetVibrator()
            setVerticalScrollState(true)
        }
    }

    private fun onUnspecifiedEventState() {
        homeFragmentUtils.setVerticalScrollState(true)
        isSwiping = false
    }

    private fun onActionMoveActions(view: View) {
        when {
            view.x in neutralRange -> {
                homeFragmentUtils.notsVibrator.resetVibrator()
                (holder.binding.swipeBg.background as GradientDrawable).setColor(
                    homeFragmentUtils.getSwipeBgColor()
                )
                shouldChangePriority = false
                shouldDelete = false
            }
            translationValue > swipeThreshold -> {
                homeFragmentUtils.notsVibrator.vibrate()
                (holder.binding.swipeBg.background as GradientDrawable).setColor(
                    homeFragmentUtils.getDeleteBgColor()
                )
                shouldDelete = true
            }
            translationValue < -swipeThreshold -> {
                homeFragmentUtils.notsVibrator.vibrate()
                (holder.binding.swipeBg.background as GradientDrawable).setColor(
                    homeFragmentUtils.getPrioritizeColor()
                )
                shouldChangePriority = true
            }
        }
    }

    private fun onActionUpActions() {
        when {
            shouldChangePriority && current.priority == 0 -> {
                homeFragmentUtils.prioritize(current)
            }
            shouldChangePriority && current.priority != 0 -> {
                homeFragmentUtils.unprioritize(current)
            }
            shouldDelete -> {
                homeFragmentUtils.showDeleteDialog(current)
            }
            else -> {
                shouldChangePriority = false
                shouldDelete = false
            }
        }
    }

    private fun playNeutralItemAnimation(view: View) {
        return view.animate()
            .translationX(0f)
            .setDuration(200)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                (holder.binding.swipeBg.background as GradientDrawable).setColor(
                    homeFragmentUtils.getSwipeBgColor()
                )
            }
            .start()
    }
}
