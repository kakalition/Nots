package com.daggery.nots.settings.theme.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daggery.nots.R
import com.daggery.nots.databinding.FragmentPreviewThemeBinding
import com.daggery.nots.settings.theme.utils.PreviewThemeUtils
import com.daggery.nots.settings.theme.utils.bind
import com.daggery.nots.utils.ThemeEnum
import com.daggery.nots.utils.ThemeEnum.*
import com.google.android.material.color.MaterialColors

// TODO: REFACTOR
class PreviewThemeFragment : Fragment() {

    private var _viewBinding: FragmentPreviewThemeBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var _fragmentUtils: PreviewThemeUtils? = null
    private val fragmentUtils get() = _fragmentUtils!!

    internal val args: PreviewThemeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val themeContext = themeContextGetter(args.themeEnum)
        statusBarColorSetter(themeContext)
        _viewBinding = FragmentPreviewThemeBinding.inflate(
            inflater.cloneInContext(themeContext),
            container,
            false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fragmentUtils = PreviewThemeUtils(this, args)

        viewBinding.previewBinding.toolbarBinding.apply {
            toolbarTitle.text = "Preview"
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        viewBinding.previewBinding.previewOne.bind(fragmentUtils, "One")
        viewBinding.previewBinding.previewTwo.bind(fragmentUtils, "Two")
        viewBinding.previewBinding.previewThree.bind(fragmentUtils, "Three")
        viewBinding.previewBinding.previewFour.bind(fragmentUtils, "Four")

        viewBinding.previewBinding.applyThemeButton.setOnClickListener { _ ->
            fragmentUtils.applyTheme()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _fragmentUtils = null
    }

    private fun themeContextGetter(themeEnum: ThemeEnum): Context {
        return when(themeEnum) {
            AZALEA -> { ContextThemeWrapper(activity, R.style.AzaleaTheme) }
            NORD -> { ContextThemeWrapper(activity, R.style.NordTheme) }
            else -> { ContextThemeWrapper(activity, R.style.DefaultDarkTheme) }
        }
    }

    private fun statusBarColorSetter(context: Context) {
        requireActivity().window.statusBarColor =
            MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorSurface,
                resources.getColor(R.color.transparent, null)
            )
    }
}