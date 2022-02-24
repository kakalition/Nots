package com.daggery.nots

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip

fun Sequence<Chip>.extractChecked(): List<String> {
    return this.toList()
        .filter { it.isChecked }
        .map { it.text.toString() }
}

fun View.setMargin(
    resources: Resources,
    leftMargin: Int? = null,
    topMargin: Int? = null,
    rightMargin: Int? = null,
    bottomMargin: Int? = null,
) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    var leftDp: Float? = null
    leftMargin?.let {
        leftDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            it.toFloat(),
            resources.displayMetrics
        )
    }
    var topDp: Float? = null
    topMargin?.let {
        topDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            it.toFloat(),
            resources.displayMetrics
        )
    }
    var rightDp: Float? = null
    rightMargin?.let {
        rightDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            it.toFloat(),
            resources.displayMetrics
        )
    }
    var bottomDp: Float? = null
    bottomMargin?.let {
        bottomDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            it.toFloat(),
            resources.displayMetrics
        )
    }
    params.setMargins(
        leftDp?.toInt() ?: params.leftMargin,
        topDp?.toInt() ?: params.topMargin,
        rightDp?.toInt() ?: params.rightMargin,
        bottomDp?.toInt() ?: params.bottomMargin,
    )
    layoutParams = params
}