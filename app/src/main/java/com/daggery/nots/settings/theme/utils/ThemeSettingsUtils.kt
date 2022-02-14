package com.daggery.nots.settings.theme.utils

import android.os.Build
import android.view.View
import androidx.core.view.get
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainActivity
import com.daggery.nots.R
import com.daggery.nots.databinding.TileActiveThemeBinding
import com.daggery.nots.databinding.TileInactiveThemeBinding
import com.daggery.nots.databinding.TileMaterialYouThemeBinding
import com.daggery.nots.setMargin
import com.daggery.nots.settings.theme.data.TileThemeData
import com.daggery.nots.settings.theme.data.TileThemeDataSource
import com.daggery.nots.settings.theme.view.ThemeSettingsFragment
import com.daggery.nots.settings.theme.view.ThemeSettingsFragmentDirections
import com.daggery.nots.utils.ThemeEnum


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

    private val navigationClickListener: (View) -> Unit = { _: View ->
        fragment.findNavController().navigateUp()
    }

    private fun getNavigationDirection(themeEnum: ThemeEnum): NavDirections {
        return ThemeSettingsFragmentDirections
            .actionThemeSettingsFragmentToPreviewThemeFragment(themeEnum)
    }

    private fun navigateToPreview(themeEnum: ThemeEnum) {
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
        return fragment.viewModel.themeManager.themeKey
    }

    fun getActiveThemeName(): String {
        return fragment.viewModel.themeManager.getThemeName()
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

    fun bindsMaterialYouIfAvailable() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            fragment.viewBinding.materialYou.root.visibility = View.GONE
        }
    }

    private fun adjustAvailableThemeMargin() {
        with(fragment.viewBinding) {
            val maxIndexOfGridLayout = gridlayout.childCount - 1
            val innerMargin = 11
            gridlayout[0].setMargin(fragment.resources, 0, 0, innerMargin, innerMargin)
            gridlayout[1].setMargin(fragment.resources, innerMargin, 0, 0, innerMargin)
            gridlayout[2].setMargin(fragment.resources, 0, innerMargin, innerMargin, innerMargin)
            gridlayout[3].setMargin(fragment.resources, innerMargin, innerMargin, 0, innerMargin)

            // Whether to show last index if Material You is active
            if(maxIndexOfGridLayout == 4) {
                gridlayout[4]
                    .setMargin(fragment.resources, 0, innerMargin, innerMargin, innerMargin)
            }
        }
    }

    fun bindsAvailableTheme() {
        with(fragment.viewBinding) {
            materialYou.bind(fragment) {
                navigateToPreview(ThemeEnum.MATERIAL_YOU)
            }

            darkTheme.bind(fragment, TileThemeDataSource.darkThemeTile.copy {
                navigateToPreview(ThemeEnum.DARK_THEME)
            })

            nord.bind(fragment, TileThemeDataSource.nordTile.copy {
                navigateToPreview(ThemeEnum.NORD)
            })

            steelBlue.bind(fragment, TileThemeDataSource.steelBlueTile.copy {
                navigateToPreview(ThemeEnum.STEEL_BLUE)
            })

            royalLavender.bind(fragment, TileThemeDataSource.royalLavenderTile.copy {
                navigateToPreview(ThemeEnum.ROYAL_LAVENDER)
            })

            heatherBerry.bind(fragment, TileThemeDataSource.heatherBerryTile.copy {
                navigateToPreview(ThemeEnum.HEATHER_BERRY)
            })
            adjustAvailableThemeMargin()
        }
    }
}