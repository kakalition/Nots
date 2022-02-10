package com.daggery.nots.settings.theme.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.daggery.nots.MainViewModel
import com.daggery.nots.databinding.FragmentThemeSettingsBinding
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.darkThemeTile
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.heatherBerryTile
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.nordTile
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.royalLavenderTile
import com.daggery.nots.settings.theme.data.TileThemeDataSource.Companion.steelBlueTile
import com.daggery.nots.settings.theme.utils.ThemeSettingsUtil
import com.daggery.nots.settings.theme.utils.bind
import com.daggery.nots.utils.ThemeEnum

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

        // Hide Material You under Android S (SDK 31)
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            with(viewBinding.materialYou) { root.visibility = View.GONE }
        }

        viewBinding.materialYou.bind(this) {
            fragmentUtils.navigateToPreview(ThemeEnum.MATERIAL_YOU)
        }

        viewBinding.darkTheme.bind(this, darkThemeTile.copy {
            fragmentUtils.navigateToPreview(ThemeEnum.DARK_THEME)
        })

        viewBinding.nord.bind(this, nordTile.copy {
            fragmentUtils.navigateToPreview(ThemeEnum.NORD)
        })

        viewBinding.steelBlue.bind(this, steelBlueTile.copy {
            fragmentUtils.navigateToPreview(ThemeEnum.STEEL_BLUE)
        })

        viewBinding.royalLavender.bind(this, royalLavenderTile.copy {
            fragmentUtils.navigateToPreview(ThemeEnum.ROYAL_LAVENDER)
        })

        viewBinding.heatherBerry.bind(this, heatherBerryTile.copy {
            fragmentUtils.navigateToPreview(ThemeEnum.HEATHER_BERRY)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
    }
}