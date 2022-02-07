package com.daggery.nots.utils

import android.content.Context
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.daggery.nots.R
import com.google.android.material.color.MaterialColors

class GeneralUtils {
    fun prepareStatusBar(activity: FragmentActivity, context: Context? = null, @StyleRes themeKey: Int) {
        activity.window.apply {
            // Set Status Bar Color
            statusBarColor = MaterialColors.getColor(
                context ?: activity,
                com.google.android.material.R.attr.colorSurface,
                activity.resources.getColor(R.color.transparent, null)
            )

            // Set Status Bar Icon Color
            when(themeKey) {
                R.style.DefaultDarkTheme -> decorView.systemUiVisibility = 0
                R.style.AzaleaTheme -> decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}