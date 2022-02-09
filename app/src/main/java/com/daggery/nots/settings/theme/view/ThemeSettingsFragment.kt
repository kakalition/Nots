package com.daggery.nots.settings.theme.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import com.daggery.nots.MainViewModel
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentThemeSettingsBinding
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.azaleaTile
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.defaultDarkTile
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.nordTile
import com.daggery.nots.settings.theme.utils.ThemeSettingsUtil
import com.daggery.nots.settings.theme.utils.bind
import com.daggery.nots.utils.ThemeEnum

// TODO: Unify Theme Settings, add or remove theme should be easy and in one place only

class ThemeSettingsFragment : Fragment() {

    private var _viewBinding: FragmentThemeSettingsBinding? = null
    val viewBinding get() = _viewBinding!!

    val viewModel: MainViewModel by activityViewModels()

    private var _fragmentUtils: ThemeSettingsUtil? = null
    val fragmentUtils get() = _fragmentUtils!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentThemeSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = ThemeSettingsUtil(this)

        with(fragmentUtils) {
            revertStatusBarColor()
            bindsToolbar()
            bindsCurrentTheme()
        }

        viewBinding.defaultDark.bind(this, defaultDarkTile.copy {
            fragmentUtils.navigateToPreview(ThemeEnum.DEFAULT_DARK)
        })

        viewBinding.defaultWhite.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.white_surface, null))
            themeTitle.setTextColor(resources.getColor(R.color.black, null))
            themeTitle.text = getString(R.string.theme_steel_blue)
        }

        viewBinding.nord.bind(this, nordTile.copy {
            fragmentUtils.navigateToPreview(ThemeEnum.NORD)
        })

        viewBinding.paleBlue.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.pale_blue, null))
            themeTitle.setTextColor(resources.getColor(R.color.white, null))
            themeTitle.text = getString(R.string.theme_royal_lavender)
        }

        viewBinding.jungleMist.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.jungle_mist, null))
            themeTitle.setTextColor(resources.getColor(R.color.white, null))
            themeTitle.text = getString(R.string.theme_heather_berry)
        }

        viewBinding.azalea.bind(this, azaleaTile.copy {
            fragmentUtils.navigateToPreview(ThemeEnum.AZALEA)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
    }

}