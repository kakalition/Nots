package com.daggery.nots.settings.theme.utils

import android.graphics.Color
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainActivity
import com.daggery.nots.R
import com.daggery.nots.databinding.TileThemeCardBinding
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.azaleaTile
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.defaultDarkTile
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.nordTile
import com.daggery.nots.settings.view.ThemeSettingsFragment
import com.daggery.nots.settings.view.ThemeSettingsFragmentDirections
import com.daggery.nots.utils.ThemeEnum

data class TileThemeData(
    val title: String,
    @ColorRes val primaryColorRes: Int,
    @ColorRes val secondaryColorRes: Int,
    @ColorRes val surfaceColorRes: Int,
    @DrawableRes val themePortraitRes: Int,
    val onClickListener: ((View) -> Unit)?
)

fun TileThemeCardBinding.bind(
    fragment: Fragment,
    tileThemeData: TileThemeData
) {
    primaryColor.background = ResourcesCompat.getDrawable(fragment.resources, R.drawable.bg_theme_color, null)
    primaryColor.background.setTint(fragment.resources.getColor(tileThemeData.primaryColorRes, null))
    secondaryColor.background = ResourcesCompat.getDrawable(fragment.resources, R.drawable.bg_theme_color, null)
    secondaryColor.background.setTint(fragment.resources.getColor(tileThemeData.secondaryColorRes, null))
    surfaceColor.background = ResourcesCompat.getDrawable(fragment.resources, R.drawable.bg_theme_color, null)
    surfaceColor.background.setTint(fragment.resources.getColor(tileThemeData.surfaceColorRes, null))
    themePortrait.setImageResource(tileThemeData.themePortraitRes)
    themePortrait.setColorFilter(Color.parseColor("#33000000"))
    themeTitle.setTextColor(fragment.resources.getColor(R.color.white, null))
    themeTitle.text = tileThemeData.title
    root.setOnClickListener(tileThemeData.onClickListener)
}

class ThemeSettingsUtil(private val fragment: ThemeSettingsFragment) {

    private val navigationClickListener: (View) -> Unit = { view: View ->
        fragment.findNavController().navigateUp()
    }

    private fun getNavigationDirection(themeEnum: ThemeEnum): NavDirections {
        return ThemeSettingsFragmentDirections
            .actionThemeSettingsFragmentToViewThemeFragment(themeEnum)
    }

    internal fun navigateToPreview(themeEnum: ThemeEnum) {
        fragment.findNavController().navigate(getNavigationDirection(themeEnum))
    }

    fun revertStatusBarColor() {
        (fragment.requireActivity() as MainActivity).statusBarColorSetter()
    }

    fun bindsToolbar() {
        fragment.viewBinding.toolbarBinding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(navigationClickListener)
        }
    }

    fun bindsCurrentTheme() {
        with(fragment) {
            viewModel.themeKey.let {
                when(it) {
                    R.style.AzaleaTheme -> viewBinding.currentTheme.bind(this, azaleaTile)
                    R.style.NordTheme -> viewBinding.currentTheme.bind(this, nordTile)
                    else -> viewBinding.currentTheme.bind(this, defaultDarkTile)
                }
            }
        }
    }

}