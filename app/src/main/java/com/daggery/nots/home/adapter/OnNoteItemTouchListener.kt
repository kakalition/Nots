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
)
    : View.OnTouchListener {

    private var isSwiping = false
    private var shouldChangePriority = false
    private var shouldDelete = false
    private var translationValue = 0f
    private var viewAnchorX = 0f
    private val swipeWeight = 0.45f
    private var swipeThreshold = 0f
    private var neutralRange: ClosedFloatingPointRange<Float> = 0f..0f

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isSwiping = false
                viewAnchorX = event.rawX
                swipeThreshold = (view?.width?.div(4))?.toFloat() ?: 0f
                neutralRange = -swipeThreshold..swipeThreshold
            }
            MotionEvent.ACTION_UP -> {
                if(!isSwiping) view?.performClick()
                with(homeFragmentUtils) {
                    notsVibrator.resetVibrator()
                    setVerticalScrollState(true)
                }
                view?.animate()
                    ?.translationX(0f)
                    ?.setDuration(200)
                    ?.setInterpolator(DecelerateInterpolator())
                    ?.withEndAction {
                        (holder.binding.swipeBg.background as GradientDrawable).setColor(Color.argb(255, 78, 78, 78))
                    }
                    ?.start()
                isSwiping = false
                if(shouldChangePriority && current.priority == 0) {
                    homeFragmentUtils.prioritize(current)
                } else if(shouldChangePriority && current.priority != 0){
                    homeFragmentUtils.unprioritize(current)
                } else if(shouldDelete) {
                    homeFragmentUtils.showDeleteDialog(current)
                } else {
                    shouldChangePriority = false
                    shouldDelete = false
                }
            }
            // TODO: Optimize
            // TODO: Cancel Request if Swipe is Less than Threshold
            MotionEvent.ACTION_MOVE -> {
                homeFragmentUtils.setVerticalScrollState(false)
                translationValue = event.rawX - viewAnchorX
                if(translationValue > 10 || translationValue < -10) {
                    isSwiping = true
                }
                view?.translationX = translationValue * swipeWeight
                when {
                    view?.x ?: 0f in neutralRange -> {
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
                            Color.argb(255, 255, 105, 97)
                        )
                        shouldDelete = true
                    }
                    translationValue < -swipeThreshold -> {
                        homeFragmentUtils.notsVibrator.vibrate()
                        (holder.binding.swipeBg.background as GradientDrawable).setColor(
                            Color.argb(255, 78, 175, 201)
                        )
                        shouldChangePriority = true
                    }
                }
            }
            else -> {
                homeFragmentUtils.setVerticalScrollState(true)
                isSwiping = false
            }
        }
        return true
    }
}
