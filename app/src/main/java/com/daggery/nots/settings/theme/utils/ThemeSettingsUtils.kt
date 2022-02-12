package com.daggery.nots.settings.theme.utils

import android.util.Log
import android.view.View
import androidx.annotation.ColorRes
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainActivity
import com.daggery.nots.R
import com.daggery.nots.databinding.TileActiveThemeBinding
import com.daggery.nots.databinding.TileInactiveThemeBinding
import com.daggery.nots.databinding.TileMaterialYouThemeBinding
import com.daggery.nots.settings.theme.view.ThemeSettingsFragment
import com.daggery.nots.settings.theme.view.ThemeSettingsFragmentDirections
import com.daggery.nots.utils.ThemeEnum

data class TileThemeData(
    val title: String,
    @ColorRes val surfaceColorRes: Int,
    @ColorRes val secondaryColorRes: Int,
    val onClickListener: ((View) -> Unit)?
)

private fun TileActiveThemeBinding.bind(themeText: String) {
    themeTitle.text = themeText
}

fun TileInactiveThemeBinding.bind(fragment: ThemeSettingsFragment, tileThemeData: TileThemeData) {
    val shouldHide = tileThemeData.title == fragment.fragmentUtils.getActiveThemeName()
    if(shouldHide) {
        fragment.viewBinding.gridlayout.removeView(this.root)
    } else {
        root.setOnClickListener(tileThemeData.onClickListener)
        root.background.setTint(
            fragment.resources.getColor(tileThemeData.surfaceColorRes, null)
        )
        secondaryColor.drawable.setTint(
            fragment.resources.getColor(tileThemeData.secondaryColorRes, null)
        )
        themeTitle.text = tileThemeData.title
    }
}

fun TileMaterialYouThemeBinding.bind(fragment: ThemeSettingsFragment, onClickListener: ((View) -> Unit)) {
    val shouldHide = fragment.fragmentUtils.getActiveThemeName() == "Material You Theme"
    if (shouldHide) {
        root.visibility = View.GONE
    }
    root.setOnClickListener(onClickListener)
}


class ThemeSettingsUtil(private val fragment: ThemeSettingsFragment) {

    private val navigationClickListener: (View) -> Unit = { view: View ->
        fragment.findNavController().navigateUp()
    }

    private fun getNavigationDirection(themeEnum: ThemeEnum): NavDirections {
        return ThemeSettingsFragmentDirections
            .actionThemeSettingsFragmentToPreviewThemeFragment(themeEnum)
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

    private fun getActiveThemeKey(): Int {
        return fragment.viewModel.themeKey
    }

    fun getActiveThemeName(): String {
        return fragment.viewModel.getThemeName()
    }

    fun bindsCurrentTheme() {
        with(fragment.viewBinding.currentTheme) {
            when(getActiveThemeKey()) {
                R.style.MaterialYouTheme -> bind("Material You")
                R.style.NordTheme -> bind("Nord")
                R.style.SteelBlueTheme -> bind("Steel Blue")
                R.style.RoyalLavenderTheme -> bind("Royal Lavender")
                R.style.HeatherBerryTheme -> bind("Heather Berry")
                else -> bind("Dark")
            }
        }
    }
}