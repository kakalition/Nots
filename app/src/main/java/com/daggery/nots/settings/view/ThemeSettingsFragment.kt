package com.daggery.nots.settings.view

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentThemeSettingsBinding

class ThemeSettingsFragment : Fragment() {

    private lateinit var _binding: FragmentThemeSettingsBinding
    val binding get () = _binding

    private val navigationClickListener: (View) -> Unit = { view: View ->
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeSettingsBinding.inflate(inflater, container, false)
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
        binding.currentTheme.apply {
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_card_selected, null)
            themeTitle.setTextColor(resources.getColor(R.color.white_surface, null))
            themeTitle.text = "Default Black"
        }

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

        binding.azalea.apply {
            primaryColor.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_color, null)
            primaryColor.background.setTint(resources.getColor(R.color.azalea_primary, null))
            secondaryColor.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_color, null)
            secondaryColor.background.setTint(resources.getColor(R.color.azalea_secondary, null))
            surfaceColor.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_theme_color, null)
            surfaceColor.background.setTint(resources.getColor(R.color.azalea_surface, null))
            themePortrait.setImageResource(R.drawable.azalea)
            themePortrait.setColorFilter(Color.parseColor("#33000000"))
            themeTitle.setTextColor(resources.getColor(R.color.white, null))
            themeTitle.text = "Azalea"
            root.setOnClickListener {
                requireActivity().setTheme(R.style.AzaleaTheme)
                findNavController().run {
                    popBackStack()
                    navigate(R.id.themeSettingsFragment)
                }
            }
        }
    }
}