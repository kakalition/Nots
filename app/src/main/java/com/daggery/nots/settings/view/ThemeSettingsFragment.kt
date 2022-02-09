package com.daggery.nots.settings.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainViewModel
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentThemeSettingsBinding
import com.daggery.nots.databinding.TileThemeCardBinding
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

// TODO: Unify Theme Settings, add or remove theme should be easy and in one place only

class ThemeSettingsFragment : Fragment() {

    companion object {
        private val defaultDarkTile = TileThemeData(
            title = "Default Dark",
            primaryColorRes = R.color.default_dark_primary,
            secondaryColorRes = R.color.default_dark_secondary,
            surfaceColorRes = R.color.default_dark_surface,
            themePortraitRes = R.drawable.default_black_portrait,
            onClickListener = null
        )

        private val nordTile = TileThemeData(
            title = "Nord",
            primaryColorRes = R.color.nord_primary,
            secondaryColorRes = R.color.nord_secondary,
            surfaceColorRes = R.color.nord_surface,
            themePortraitRes = R.drawable.nord_portrait,
            onClickListener = null
        )

        private val azaleaTile = TileThemeData(
            title = "Azalea",
            primaryColorRes = R.color.azalea_primary,
            secondaryColorRes = R.color.azalea_secondary,
            surfaceColorRes = R.color.azalea_surface,
            themePortraitRes = R.drawable.azalea_portrait,
            onClickListener = null
        )
    }


    private var _viewBinding: FragmentThemeSettingsBinding? = null
    val viewBinding get() = _viewBinding!!

    val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentThemeSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prepare Toolbar
        viewBinding.toolbarBinding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(navigationClickListener)
        }

        // Current Theme Layout Binding
        viewModel.themeKey.let {
            when(it) {
                R.style.AzaleaTheme -> viewBinding.currentTheme.bind(this, azaleaTile)
                R.style.NordTheme -> viewBinding.currentTheme.bind(this, nordTile)
                else -> viewBinding.currentTheme.bind(this, defaultDarkTile)
            }
        }

        viewBinding.defaultDark.bind(this, defaultDarkTile.copy {
            navigateToPreview(ThemeEnum.DEFAULT_DARK)
        })

        viewBinding.defaultWhite.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.white_surface, null))
            themeTitle.setTextColor(resources.getColor(R.color.black, null))
            themeTitle.text = "Default White"
        }

        viewBinding.nord.bind(this, nordTile.copy {
            navigateToPreview(ThemeEnum.NORD)
        })

        viewBinding.paleBlue.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.pale_blue, null))
            themeTitle.setTextColor(resources.getColor(R.color.white, null))
            themeTitle.text = "Pale Blue"
        }

        viewBinding.jungleMist.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.jungle_mist, null))
            themeTitle.setTextColor(resources.getColor(R.color.white, null))
            themeTitle.text = "Jungle Mist"
        }

        viewBinding.azalea.bind(this, azaleaTile.copy {
            navigateToPreview(ThemeEnum.AZALEA)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private val navigationClickListener: (View) -> Unit = { view: View ->
        findNavController().navigateUp()
    }

    private fun getNavigationDirection(themeEnum: ThemeEnum): NavDirections {
        return ThemeSettingsFragmentDirections
            .actionThemeSettingsFragmentToViewThemeFragment(themeEnum)
    }

    private fun navigateToPreview(themeEnum: ThemeEnum) {
        findNavController().navigate(getNavigationDirection(themeEnum))
    }

}