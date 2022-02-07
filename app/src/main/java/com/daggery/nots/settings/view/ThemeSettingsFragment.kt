package com.daggery.nots.settings.view

import android.animation.ArgbEvaluator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.daggery.nots.MainActivity
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentThemeSettingsBinding
import com.daggery.nots.databinding.TileThemeCardBinding
import com.daggery.nots.home.viewmodel.HomeViewModel
import com.daggery.nots.utils.GeneralUtils
import com.daggery.nots.utils.ThemeEnum
import com.google.android.material.color.MaterialColors

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

class ThemeSettingsFragment : Fragment() {

    private val generalUtils = GeneralUtils()
    private lateinit var _binding: FragmentThemeSettingsBinding
    val binding get() = _binding

    val viewModel: HomeViewModel by activityViewModels()

    private val navigationClickListener: (View) -> Unit = { view: View ->
        findNavController().navigateUp()
    }

    private val defaultDarkTile = TileThemeData(
        title = "Default Dark",
        primaryColorRes = R.color.default_dark_primary,
        secondaryColorRes = R.color.default_dark_secondary,
        surfaceColorRes = R.color.default_dark_surface,
        themePortraitRes = R.drawable.default_black_portrait,
        onClickListener = { view ->
            findNavController().run {
                // TODO: call setTheme before activity setContentView
                // (requireActivity() as MainActivity).updateTheme(R.style.DefaultDarkTheme)
                val navigation = ThemeSettingsFragmentDirections
                    .actionThemeSettingsFragmentToViewThemeFragment(ThemeEnum.DEFAULT_DARK)
                navigate(navigation)
            }
        }
    )

    private val azaleaTile = TileThemeData(
        title = "Azalea",
        primaryColorRes = R.color.azalea_primary,
        secondaryColorRes = R.color.azalea_secondary,
        surfaceColorRes = R.color.azalea_surface,
        themePortraitRes = R.drawable.azalea_portrait,
        onClickListener = { _ ->
            findNavController().run {
                // (requireActivity() as MainActivity).updateTheme(R.style.AzaleaTheme)
                val navigation = ThemeSettingsFragmentDirections
                    .actionThemeSettingsFragmentToViewThemeFragment(ThemeEnum.AZALEA)
                navigate(navigation)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeSettingsBinding.inflate(inflater, container, false)
        generalUtils.prepareStatusBar(activity = requireActivity(), themeKey = viewModel.themeKey)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prepare Toolbar
        binding.toolbarBinding.apply {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener(navigationClickListener)
        }

        // Theme Layout Binding
        viewModel.themeKey.let {
            when(it) {
                R.style.AzaleaTheme -> binding.currentTheme.bind(this, azaleaTile)
                else -> binding.currentTheme.bind(this, defaultDarkTile)
            }
        }

        binding.defaultDark.bind(this, defaultDarkTile)

        binding.defaultWhite.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.white_surface, null))
            themeTitle.setTextColor(resources.getColor(R.color.black, null))
            themeTitle.text = "Default White"
        }

        binding.paleBlue.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.pale_blue, null))
            themeTitle.setTextColor(resources.getColor(R.color.white, null))
            themeTitle.text = "Pale Blue"
        }

        binding.jungleMist.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card, null)
            root.background.setTint(resources.getColor(R.color.jungle_mist, null))
            themeTitle.setTextColor(resources.getColor(R.color.white, null))
            themeTitle.text = "Jungle Mist"
        }

        binding.azalea.bind(this, azaleaTile)
    }
}